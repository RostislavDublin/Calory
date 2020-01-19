package rdublin.portal.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class PortalUserDetailsImpl
        extends org.springframework.security.core.userdetails.User
        implements PortalUserDetails {

    private Integer userId;

    public PortalUserDetailsImpl(String username, String password, boolean enabled, boolean accountNonExpired,
                                 boolean credentialsNonExpired,
                                 boolean accountNonLocked,
                                 Collection<? extends GrantedAuthority> authorities,
                                 Integer userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    /**
     * Preliminary object population (name only);
     *
     * @param username
     */
    public PortalUserDetailsImpl(String username) {
        super(username,
                "HIDDEN",
                false,
                false,
                false,
                false,
                Collections.EMPTY_LIST);
        this.userId = null;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }
}
