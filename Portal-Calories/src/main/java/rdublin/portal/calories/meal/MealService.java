package rdublin.portal.calories.meal;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    List<Meal> findAllByUserAndMealPeriod(
            Integer userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo);

    void delete(int id);

    Meal findById(int id);

    Meal update(MealDto meal);

    Meal create(MealDto meal);

    /**
     * Perform "old" and "new" user&day pairs meal calories "expectation exceeded" calculation
     * and set results on the users' days' meals.
     *
     * @param oldUserId
     * @param oldMealDate
     * @param newUserId
     * @param newMealDate
     */
    @Transactional
    void calcUsersDaysExpectationsExceeded(
            int oldUserId, LocalDate oldMealDate,
            int newUserId, LocalDate newMealDate
    );

    /**
     * Perform user&day pair meal calories "expectation exceeded" calculation and set results on the user's day's meals.
     *
     * @param userId
     * @param mealDate
     */
    @Transactional
    void calcUserDayExpectationsExceeded(int userId, LocalDate mealDate);

    /**
     * Perform all user's meal days calories "expectation exceeded" calculations
     * and set results on the user's day's meals.
     *
     * @param userId
     */
    @Transactional
    void calcUserAllDaysExpectationsExceeded(int userId);
}
