package rdublin.portal.user;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User findFirstByName(String name);

    boolean existsByName(String name);

}
