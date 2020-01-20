package rdublin.portal.calories.userSetting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import rdublin.portal.user.PortalUserDetails;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/userSettings")
public class UserSettingController {

    @Autowired
    UserSettingService userSettingService;

    @GetMapping
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE')")
    public List<UserSetting> findAll() {
        return userSettingService.findAll();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') " +
            "   and #currentUser.userId == @userSettingService.findByUserId(#userId).userId)")
    public UserSetting getOne(@PathVariable int userId, @AuthenticationPrincipal PortalUserDetails currentUser) {
        return userSettingService.findByUserId(userId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userSetting.userId)")
    public UserSetting save(@RequestBody UserSettingDto userSetting, @AuthenticationPrincipal Object currentUser) {
        try {
            return userSettingService.create(userSetting);
        } catch (DataIntegrityViolationException e) {
            SQLIntegrityConstraintViolationException ee = (SQLIntegrityConstraintViolationException) e.getRootCause();
            throw new ResponseStatusException(HttpStatus.CONFLICT, ee.getLocalizedMessage());
        }
    }

    @PutMapping()
    @PreAuthorize("#userSettingDto.userId != null " +
            "and hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userSettingDto.userId)")
    public UserSetting update(@RequestBody UserSettingDto userSettingDto,
                              @AuthenticationPrincipal PortalUserDetails currentUser) {
        return userSettingService.provision(userSettingDto);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userId)")
    public void delete(@PathVariable int userId, @AuthenticationPrincipal PortalUserDetails currentUser) {
        try {
            userSettingService.deleteByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }

    }

}
