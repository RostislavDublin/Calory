package rdublin.portal.calories.meal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "meals", collectionResourceRel = "meals")
public interface MealRepository extends PagingAndSortingRepository<Meal, Integer> {

    @RestResource
    List<Meal> findAllByUserId(Integer userId);

    List<Meal> findAllByUserIdAndMealDateIsBetween(
            @Param("userId") Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateFrom") LocalDate dateFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateTo") LocalDate dateTo
    );

    List<Meal> findAllByUserIdAndMealDateIsBetweenAndMealTimeIsBetween(
            @Param("userId") Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateFrom") LocalDate dateFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Param("dateTo") LocalDate dateTo,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeFrom") LocalTime timeFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeTo") LocalTime timeTo
    );
    List<Meal> findAllByUserIdAndMealTimeIsBetween(
            @Param("userId") Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeFrom") LocalTime timeFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Param("timeTo") LocalTime timeTo
    );

}
