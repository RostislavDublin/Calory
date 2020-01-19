package rdublin.portal.auth.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import rdublin.portal.user.PortalUserDetailsImpl;

import java.util.Map;

import static rdublin.portal.auth.oauth2.PortalAccessTokenClaims.*;

/**
 * Patch of DefaultUserAuthenticationConverter to set properly populated PortalUserDetails
 * with the data taken from the JWT ACCESS_TOKEN claims.
 */
public class PortalUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication auth0 = super.extractAuthentication(map);

        if (auth0 == null
                || !(auth0 instanceof UsernamePasswordAuthenticationToken)
                || !map.containsKey(USERNAME)
                || !(auth0.getPrincipal() instanceof String)
                || !map.containsKey(CLAIM_KEY_USER_ID)
        ) return auth0;

        PortalUserDetailsImpl principal = new PortalUserDetailsImpl(
                (String) auth0.getPrincipal(),
                "[PROTECTED]",
                (Boolean)map.get(CLAIM_KEY_ENABLED),
                (Boolean)map.get(CLAIM_KEY_ACCOUNT_NON_EXPIRED),
                (Boolean)map.get(CLAIM_KEY_CREDENTIALS_NON_EXPIRED),
                (Boolean)map.get(CLAIM_KEY_ACCOUNT_NON_LOCKED),
                auth0.getAuthorities(),
                (Integer) map.get(CLAIM_KEY_USER_ID));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, "N/A", auth0.getAuthorities());

        return auth;

    }
}
