package rdublin.portal.calories.meal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class MealDto {
    private Integer id;

    private Integer userId;

    private LocalDate mealDate;

    private LocalTime mealTime;

    private String meal;

    private Integer calories;

}
