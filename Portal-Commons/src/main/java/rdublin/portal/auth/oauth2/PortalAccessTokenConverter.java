package rdublin.portal.auth.oauth2;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

import static rdublin.portal.auth.oauth2.PortalAccessTokenClaims.FILTERED_CLAIMS_KEYS;

/**
 * DefaultAccessTokenConverter used to set Authentication details to Null.
 * Let's create CustomAccessTokenConverter and set Authentication details with useful access token claims
 */
@Component
public class PortalAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);

        Map<String, ?> filteredClaims = claims
                .entrySet().stream()
                .filter(e -> FILTERED_CLAIMS_KEYS.contains(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (o, n) -> n));

        authentication.setDetails(filteredClaims);

        return authentication;
    }

    @PostConstruct
    private void postConstruct() {

        PortalUserAuthenticationConverter uac = new PortalUserAuthenticationConverter();
        //uac.setUserDetailsService(userService);

        this.setUserTokenConverter(uac);
    }
}
