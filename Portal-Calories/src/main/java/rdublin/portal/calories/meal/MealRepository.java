package rdublin.portal.calories.meal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository extends PagingAndSortingRepository<Meal, Integer> {

    List<Meal> findAllByUserId(Integer userId);

    @Query("select m from Meal m where (?1 is null or m.userId = ?1) and" +
            "(?2 is null or m.mealDate >= ?2) and" +
            "(?3 is null or m.mealDate <= ?3) and" +
            "(?4 is null or m.mealTime >= ?4) and" +
            "(?5 is null or ?5 = '00:00:00' or m.mealTime <= ?5)"
    )
    List<Meal> findAllByUserAndMealPeriod(
            Integer userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo
    );

    @Query("select distinct m.mealDate from Meal m where m.userId = ?1")
    List<LocalDate> findAllUserMealDays(Integer userId);

}
