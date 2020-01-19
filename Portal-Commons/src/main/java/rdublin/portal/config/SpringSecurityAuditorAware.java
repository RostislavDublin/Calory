package rdublin.portal.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public Optional<String> getCurrentAuditor() {

        return Optional.ofNullable(SecurityContextHolder.getContext())
                       .map(SecurityContext::getAuthentication)
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .map(principal -> {
                           String actor;
                           if (principal instanceof UserDetails) {
                               actor = ((UserDetails) principal).getUsername();
                           } else {
                               actor = (String) principal;
                           }
                           return actor;
                       });
    }
}
