package rdublin.portal.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Constraints to use narrowed type PortalUserDetails for user representation.
 */
public interface PortalUserDetailsService extends UserDetailsService {
    @Override
    PortalUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
