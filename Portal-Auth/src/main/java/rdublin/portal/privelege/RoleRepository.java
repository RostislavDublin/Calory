package rdublin.portal.privelege;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {
    Role findByName(String name);
}
