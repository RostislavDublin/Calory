package rdublin.portal.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Access(value = AccessType.FIELD)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_client_details", uniqueConstraints = {
        @UniqueConstraint(name = "UK_auth_client_details_name", columnNames = {"client_id"})
})
public class AuthClientDetails implements ClientDetails {

    @Version
    private int version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, name = "client_id", length = 64)
    private String clientId;

    private String clientSecret;

    private String grantTypes;

    private String scopes;

    private String resources;

    private String redirectUris;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private String additionalInformation;

    @Override
    public Set<String> getResourceIds() {
        return resources != null ? new HashSet<>(Arrays.asList(resources.split(","))) : null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return scopes != null ? new HashSet<>(Arrays.asList(scopes.split(","))) : null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return grantTypes != null ? new HashSet<>(Arrays.asList(grantTypes.split(","))) : null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return redirectUris != null ? new HashSet<>(Arrays.asList(redirectUris.split(","))) : null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

}
