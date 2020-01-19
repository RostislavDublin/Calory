package rdublin.portal.calories.meal;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    List<Meal> findAllByUserAndMealPeriod(
            Integer userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo);

    void delete(int id);

    Meal findById(int id);

    Meal update(MealDto mealDto);

    Meal save(MealDto meal);

}
