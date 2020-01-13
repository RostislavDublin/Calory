package rdublin.portal.calories.userSetting;

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
import javax.persistence.UniqueConstraint;

@Data
@EqualsAndHashCode(callSuper = false)

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_settings", uniqueConstraints = {
        @UniqueConstraint(name = "UK_user_settings_user_id", columnNames = {"user_id"})}
)
public class UserSetting extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    @Column(name = "calories_expected", nullable = false)
    private Integer caloriesExpected;

}
