package rdublin.portal.calories.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rdublin.portal.user.PortalUserDetails;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/meals")
public class MealController {

    @Autowired
    MealService mealService;

    @GetMapping
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #userId)")
    public List<Meal> findAllByUserAndMealPeriod(
            @Nullable @RequestParam("userId") Integer userId,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("dateFrom") LocalDate dateFrom,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("dateTo") LocalDate dateTo,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam("timeFrom") LocalTime timeFrom,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam("timeTo") LocalTime timeTo,
            @AuthenticationPrincipal Object currentUser
    ) {
        return mealService.findAllByUserAndMealPeriod(userId, dateFrom, dateTo, timeFrom, timeTo);
    }

    @GetMapping("/{mealId}")
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') " +
            "   and #currentUser.userId == @mealService.findById(#mealId).userId)")
    public Meal getOne(@PathVariable int mealId, @AuthenticationPrincipal PortalUserDetails currentUser) {
        return mealService.findById(mealId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') and #currentUser.userId == #meal.userId)")
    public Meal save(@RequestBody MealDto meal, @AuthenticationPrincipal Object currentUser) {
        try {
            return mealService.create(meal);
        } catch (DataIntegrityViolationException e) {
            SQLIntegrityConstraintViolationException ee = (SQLIntegrityConstraintViolationException) e.getRootCause();
            throw new ResponseStatusException(HttpStatus.CONFLICT, ee.getLocalizedMessage());
        }
    }

    @PutMapping("/{mealId}")
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') " +
            "   and #currentUser.userId == @mealService.findById(#mealId).userId)" +
            "   and #currentUser.userId == #mealDto.userId")
    public Meal update(@RequestBody MealDto mealDto, @PathVariable int mealId,
                       @AuthenticationPrincipal PortalUserDetails currentUser) {
        mealDto.setId(mealId);
        try {
            return mealService.update(mealDto);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }

    }

    @DeleteMapping("/{mealId}")
    @PreAuthorize("hasAuthority('MEAL_ALL_CRUD_PRIVILEGE') " +
            "or (hasAuthority('MEAL_OWN_CRUD_PRIVILEGE') " +
            "   and #currentUser.userId == @mealService.findById(#mealId).userId)")
    public void delete(@PathVariable int mealId, @AuthenticationPrincipal PortalUserDetails currentUser) {
        try {
            mealService.delete(mealId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }

    }

}
