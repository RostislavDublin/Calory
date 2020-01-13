package rdublin.portal.auth;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthClientRepository extends CrudRepository<AuthClientDetails, Integer> {
    Optional<AuthClientDetails> findByClientId(String clientId);
}
