package rdublin.portal.user;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    public static final String TEST_USERNAME = "Admin";
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_AUTHORITY = "ROLE_ADMIN";
    public static final boolean TEST_ENABLED = true;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void whenAdminAuthorizes_thenGetsAccessTokenWithProperData() throws Exception {

        String accessToken = obtainAccessToken(TEST_USERNAME, TEST_PASSWORD);

        ResultActions result
                = this.mockMvc.perform(
                get("/users/current").header("Authorization", "Bearer " + accessToken))
                              .andExpect(status().isOk())
                              .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> resultMap = jsonParser.parseMap(resultString);

        String reason = "Resulting JSON should contain";
        collector.checkThat(reason, resultMap, hasEntry("username", TEST_USERNAME));
        collector.checkThat(reason, resultMap, hasEntry("enabled", TEST_ENABLED));
    }

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
