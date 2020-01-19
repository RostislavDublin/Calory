package rdublin.portal.auth.oauth2;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import rdublin.portal.user.PortalUserDetails;

import java.util.HashMap;
import java.util.Map;

import static rdublin.portal.auth.oauth2.PortalAccessTokenClaims.*;

/**
 * Populate JWT access token with additional info which will be used by resource servers to invoke
 * accessing user UserDetails without redundant calls to the authentication server.
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {

        PortalUserDetails principal = (PortalUserDetails) authentication.getPrincipal();

        Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put(CLAIM_KEY_USER_ID, principal.getUserId());
        additionalInfo.put(CLAIM_KEY_USER_NAME, principal.getUsername());
        additionalInfo.put(CLAIM_KEY_ENABLED, principal.isEnabled());
        additionalInfo.put(CLAIM_KEY_ACCOUNT_NON_EXPIRED, principal.isAccountNonExpired());
        additionalInfo.put(CLAIM_KEY_CREDENTIALS_NON_EXPIRED, principal.isCredentialsNonExpired());
        additionalInfo.put(CLAIM_KEY_ACCOUNT_NON_LOCKED, principal.isAccountNonLocked());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
