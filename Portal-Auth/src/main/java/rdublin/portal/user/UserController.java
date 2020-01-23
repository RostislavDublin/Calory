package rdublin.portal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rdublin.portal.privelege.Privilege;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User saveUser(@RequestBody UserDto user) {
        try {
            return userService.create(user);
        } catch (DataIntegrityViolationException e) {
            SQLIntegrityConstraintViolationException ee = (SQLIntegrityConstraintViolationException) e.getRootCause();
            throw new ResponseStatusException(HttpStatus.CONFLICT, ee.getLocalizedMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_ALL_CRUD_PRIVILEGE')")
    public List<User> listUser() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('USER_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userId)")
    public User getOne(@PathVariable int userId, @AuthenticationPrincipal PortalUserDetails currentUser) {
        return userService.findById(userId);
    }

    @GetMapping("/{userId}/privileges")
    @PreAuthorize("hasAuthority('USER_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('USER_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userId)")
    public Set<Privilege> getOnePrivileges(@PathVariable int userId,
                                           @AuthenticationPrincipal PortalUserDetails currentUser) {
        return userService.getPrivileges(userService.findById(userId).getRoles());
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('USER_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userId)")
    public User update(@RequestBody UserDto userDto, @PathVariable int userId, @AuthenticationPrincipal
            PortalUserDetails currentUser) {
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_ALL_CRUD_PRIVILEGE')")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }

    @GetMapping("/current")
    public PortalUserDetails user(@AuthenticationPrincipal PortalUserDetails currentUser) {

        ((PortalUserDetailsImpl) currentUser).eraseCredentials();
        return currentUser;
    }
}
