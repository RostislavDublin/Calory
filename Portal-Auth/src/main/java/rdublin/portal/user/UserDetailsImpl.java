package rdublin.portal.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {
    Integer userId;

    public UserDetailsImpl(String username, String password, boolean enabled, boolean accountNonExpired,
                           boolean credentialsNonExpired,
                           boolean accountNonLocked,
                           Collection<? extends GrantedAuthority> authorities,
                           Integer userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
