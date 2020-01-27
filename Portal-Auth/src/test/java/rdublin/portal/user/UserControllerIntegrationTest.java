package rdublin.portal.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import rdublin.portal.MockMvcAccessTokenObtainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    public static final String TEST_USERNAME = "Admin";
    public static final String TEST_PASSWORD = "password";

    public String accessToken = "";
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    @Autowired
    public MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void before() throws Exception {
        accessToken = new MockMvcAccessTokenObtainer().obtainAccessToken(this.mockMvc, TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    public void whenLoggedInUserRequestsSelf_thenSelfDataReturned() throws Exception {

        ResultActions result
                = this.mockMvc.perform(
                get("/users/current").header("Authorization", "Bearer " + accessToken))
                              .andExpect(status().isOk())
                              .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = new JacksonJsonParser().parseMap(resultString);

        String reason = "Resulting JSON should contain";
        collector.checkThat(reason, resultMap, hasEntry("username", TEST_USERNAME));
    }

    @Test
    public void whenListUsersRequest_thenNonemptyListReturned() throws Exception {

        ResultActions result
                = this.mockMvc.perform(
                get("/users").header("Authorization", "Bearer " + accessToken))
                              .andExpect(status().isOk())
                              .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        List<Object> resultList = new JacksonJsonParser().parseList(resultString);

        String reason = "Resulting JSON array should contain 3 predefined users";
        collector.checkThat(reason, resultList, hasSize(greaterThanOrEqualTo(3)));
    }

    @Test
    public void whenCreateUser_thenCreatedUserDataReturned() throws Exception {

        String newUserName = "Test-" + System.currentTimeMillis();
        Map<String, Object> resultMap = createUser(newUserName);

        String reason = "Resulting JSON should contain Created User's proper data";
        collector.checkThat(reason, resultMap, hasEntry("name", newUserName));
        collector.checkThat(reason, resultMap, hasKey("id"));
    }

    @Test
    public void whenUpdateUser_thenUpdatedUserDataReturned() throws Exception {
        //Create new user first
        String newUserName = "Test-" + System.currentTimeMillis();
        Map<String, Object> createdUserResultMap = createUser(newUserName);

        String reason = "Resulting JSON should contain Created User's proper data";
        collector.checkThat(reason, createdUserResultMap, hasEntry("name", newUserName));
        collector.checkThat(reason, createdUserResultMap, hasKey("id"));

        int createdUserId = (int) createdUserResultMap.get("id");

        //Update created user Email
        Map<String, Object> updateUserMap = new HashMap<>(createdUserResultMap);
        String updateEmail = newUserName + "-updated-email-" + System.currentTimeMillis() + "@test.com";
        updateUserMap.put("email", updateEmail);

        ResultActions result
                = this.mockMvc.perform(put("/users/" + createdUserId)
                .content(asJsonString(updateUserMap)).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                              .andExpect(status().isOk())
                              .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> updatedUserResultMap = new JacksonJsonParser().parseMap(resultString);

        reason = "Resulting JSON should contain Updated User's proper data";
        collector.checkThat(reason, updatedUserResultMap, hasEntry("name", newUserName));
        collector.checkThat(reason, updatedUserResultMap, hasEntry("id", createdUserId));
        collector.checkThat(reason, updatedUserResultMap, hasEntry("email", updateEmail));
    }

    private Map<String, Object> createUser(String newUserName) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("name", newUserName);
        params.put("email", newUserName + "@test.com");
        params.put("dob", "2001-01-01");
        params.put("gender", "Mail");
        params.put("password", "password");

        ResultActions result
                = this.mockMvc.perform(
                post("/users/")
                        .content(asJsonString(params)).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                              .andExpect(status().isOk())
                              .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        return new JacksonJsonParser().parseMap(resultString);
    }

}
