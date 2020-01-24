package rdublin.portal.auth.oauth2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import rdublin.portal.user.PortalUserDetailsImpl;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static rdublin.portal.auth.oauth2.PortalAccessTokenClaims.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CustomTokenEnhancerTest.class, OAuth2Authentication.class})
@PowerMockIgnore({"javax.security.*"})
public class CustomTokenEnhancerTest {

    public static final int CLAIM_KEY_USER_ID_VAL = 12345;
    public static final String CLAIM_KEY_USER_NAME_VAL = "User 12345";
    public static final boolean CLAIM_KEY_ENABLED_VAL = true;
    public static final boolean CLAIM_KEY_ACCOUNT_NON_EXPIRED_VAL = true;
    public static final boolean CLAIM_KEY_CREDENTIALS_NON_EXPIRED_VAL = true;
    public static final boolean CLAIM_KEY_ACCOUNT_NON_LOCKED_VAL = true;

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    @Mock
    OAuth2Authentication userAuthentication;
    private CustomTokenEnhancer customTokenEnhancer;

    @Before
    public void before() {
        customTokenEnhancer = spy(new CustomTokenEnhancer());
    }

    @Test
    public void whenTokenEnhanced_thenContainsEnhancingData() {
        PortalUserDetailsImpl principal = new PortalUserDetailsImpl(CLAIM_KEY_USER_NAME_VAL, "nevermind",
                CLAIM_KEY_ENABLED_VAL, CLAIM_KEY_ACCOUNT_NON_EXPIRED_VAL, CLAIM_KEY_CREDENTIALS_NON_EXPIRED_VAL,
                CLAIM_KEY_ACCOUNT_NON_LOCKED_VAL, Collections.emptyList(), CLAIM_KEY_USER_ID_VAL);

        when(userAuthentication.getPrincipal()).thenReturn(principal);

        OAuth2Authentication authentication = new OAuth2Authentication(
                mock(OAuth2Request.class), userAuthentication
        );

        OAuth2AccessToken token = new DefaultOAuth2AccessToken("token_value");
        OAuth2AccessToken enhancedToken = customTokenEnhancer.enhance(token, authentication);

        Map<String, Object> enhancements = enhancedToken.getAdditionalInformation();

        String reason = "Should contain enhancement";
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_USER_ID, CLAIM_KEY_USER_ID_VAL));
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_USER_NAME, CLAIM_KEY_USER_NAME_VAL));
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_ENABLED, CLAIM_KEY_ENABLED_VAL));
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_ACCOUNT_NON_EXPIRED, CLAIM_KEY_ACCOUNT_NON_EXPIRED_VAL));
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_CREDENTIALS_NON_EXPIRED, CLAIM_KEY_CREDENTIALS_NON_EXPIRED_VAL));
        collector.checkThat(reason, enhancements, hasEntry(CLAIM_KEY_ACCOUNT_NON_LOCKED, CLAIM_KEY_ACCOUNT_NON_LOCKED_VAL));

    }
}
