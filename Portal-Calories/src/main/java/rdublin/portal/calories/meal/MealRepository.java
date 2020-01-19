package rdublin.portal.calories.meal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

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
            @Nullable @Param("userId") Integer userId,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateFrom") LocalDate dateFrom,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateTo") LocalDate dateTo,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeFrom") LocalTime timeFrom,
            @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeTo") LocalTime timeTo
    );

}
