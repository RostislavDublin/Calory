package rdublin.portal.auth.oauth2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static rdublin.portal.auth.oauth2.PortalAccessTokenClaims.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthorizationServerIntegrationTest {

    public static final String TEST_USERNAME = "Admin";
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_AUTHORITY = "ROLE_ADMIN";

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private TokenStore tokenStore;

    @Test
    public void whenAdminAuthorizes_thenAccessTokenWithProperData() throws Exception {

        String tokenValue = obtainAccessToken(TEST_USERNAME, TEST_PASSWORD);

        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        Map<String, Object> details = (Map<String, Object>) auth.getDetails();

        String reason = "Access token wrong content";
        collector.checkThat(reason, details, hasEntry(CLAIM_KEY_USER_NAME, TEST_USERNAME));
        collector.checkThat(reason, details, hasEntry(CLAIM_KEY_ENABLED, true));
        collector.checkThat(reason, details, hasEntry(CLAIM_KEY_ACCOUNT_NON_EXPIRED, true));
        collector.checkThat(reason, details, hasEntry(CLAIM_KEY_CREDENTIALS_NON_EXPIRED, true));
        collector.checkThat(reason, details, hasEntry(CLAIM_KEY_ACCOUNT_NON_LOCKED, true));

        boolean hasRoleAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(TEST_AUTHORITY));

        collector.checkThat(reason, hasRoleAdmin, is(true));

    }

    /**
     * Performs a request to the auth/token endpoint to get OAuth2 authorization and access_token
     * @param username username
     * @param password password
     * @return access token
     * @throws Exception exception
     */
    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "browser");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("browser", "secret"))
                .accept("application/json;charset=UTF-8"))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
