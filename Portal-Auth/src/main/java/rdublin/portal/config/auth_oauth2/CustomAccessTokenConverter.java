package rdublin.portal.config.auth_oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DefaultAccessTokenConverter used to set Authentication details to Null.
 * Let's create CustomAccessTokenConverter and set Authentication details with useful access token claims
 */
@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    private static final List<String> FILTERED_CLAIMS_KEYS = Arrays.asList(new String[]{"user_id", "user_name"});

    @Autowired
    UserDetailsService userService;

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

        DefaultUserAuthenticationConverter duac = new DefaultUserAuthenticationConverter();
        duac.setUserDetailsService(userService);

        this.setUserTokenConverter(duac);
    }
}
