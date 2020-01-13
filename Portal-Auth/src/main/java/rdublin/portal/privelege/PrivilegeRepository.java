package rdublin.portal.privelege;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Integer> {

    Privilege findByName(String name);

}
