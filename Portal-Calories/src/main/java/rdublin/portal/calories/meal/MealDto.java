package rdublin.portal.calories.meal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import rdublin.portal.commons.domain.AuditedEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class MealDto {

    private Integer id;

    private Integer userId;

    private LocalDate mealDate;

    private LocalTime mealTime;

    private String meal;

    private Integer calories;

}
