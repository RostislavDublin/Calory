package rdublin.portal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rdublin.portal.model.ApiResponse;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse saveUser(@RequestBody UserDto user) {
        try {
            return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.", userService.save(user));
        } catch (DataIntegrityViolationException e) {
            SQLIntegrityConstraintViolationException ee = (SQLIntegrityConstraintViolationException) e.getRootCause();
            throw new ResponseStatusException(HttpStatus.CONFLICT, ee.getLocalizedMessage());
        }
    }

    @GetMapping
    public ApiResponse listUser() {
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse getOne(@PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.", userService.findById(id));
    }

    @GetMapping("/{id}/privileges")
    public ApiResponse getOnePrivileges(@PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "User authorities fetched successfully.",
                userService.getPrivileges(userService.findById(id).getRoles()));
    }

    @PutMapping("/{id}")
    public ApiResponse update(@RequestBody UserDto userDto) {
        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully.", userService.update(userDto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable int id) {
        userService.delete(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully.", null);
    }

}
