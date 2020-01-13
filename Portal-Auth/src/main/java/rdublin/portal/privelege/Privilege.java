package rdublin.portal.privelege;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "roles")

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "privileges", uniqueConstraints = {
        @UniqueConstraint(name = "UK_privilege_name", columnNames = {"name"})
})

public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "name", length = 64)
    private String name;
    @Column(nullable = true, name = "description", length = 128)
    private String description;

    @ManyToMany(mappedBy = "privileges")
    @ToString.Exclude
    @JsonIgnore
    private Set<Role> roles;
}
