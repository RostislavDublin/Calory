package rdublin.portal.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
//@Qualifier("onTheFlyUserService")
public class PortalUserDetailsServiceImpl implements PortalUserDetailsService {
    @Override
    public PortalUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PortalUserDetailsImpl(username);
    }
}
