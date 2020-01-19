package rdublin.portal.calories.meal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rdublin.portal.commons.domain.AuditedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meals")
public class Meal extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "meal_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate mealDate;

    @Column(name = "meal_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime mealTime;

    @Column(name = "meal", nullable = false, length = 128)
    private String meal;

    @Column(name = "calories", nullable = false)
    private Integer calories;

}
