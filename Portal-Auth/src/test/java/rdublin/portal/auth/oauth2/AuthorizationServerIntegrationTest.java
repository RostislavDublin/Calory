package rdublin.portal.auth.oauth2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rdublin.portal.MockMvcAccessTokenObtainer;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
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

        String tokenValue =
                new MockMvcAccessTokenObtainer().obtainAccessToken(this.mockMvc, TEST_USERNAME, TEST_PASSWORD);

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
}
