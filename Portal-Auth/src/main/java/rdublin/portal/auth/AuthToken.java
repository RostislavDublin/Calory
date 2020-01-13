package rdublin.portal.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class AuthToken {
    private String token;
    private String username;
    private Set<String> roles;
    private Set<String> privileges;
}
