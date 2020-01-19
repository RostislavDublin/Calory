package rdublin.portal.user;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Extended Spring Security UserDetails with numeric User ID getter.
 * Numeric ID is used everywhere on the Portal for users identification (links, relations etc).
 */
public interface PortalUserDetails extends UserDetails {
    /**
     * Portal User numeric Id
     *
     * @return
     */
    Integer getUserId();
}
