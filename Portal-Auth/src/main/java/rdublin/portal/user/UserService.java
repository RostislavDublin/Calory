package rdublin.portal.user;

import org.springframework.transaction.annotation.Transactional;
import rdublin.portal.privelege.Privilege;
import rdublin.portal.privelege.Role;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface UserService {
    @Transactional
    Set<Privilege> getPrivileges(@NotNull Set<Role> roles);

    List findAll();

    void delete(int id);

    User findOne(String username);

    User findById(int id);

    UserDto update(UserDto userDto);

    User save(UserDto user);
}
