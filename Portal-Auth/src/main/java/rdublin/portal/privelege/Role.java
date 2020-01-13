package rdublin.portal.privelege;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rdublin.portal.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"privileges", "users"})

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "UK_role_name", columnNames = {"name"})
})

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "name", length = 64)
    private String name;
    @Column(nullable = true, name = "description", length = 128)
    private String description;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "FK_roles_privileges_to_roles"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"),
            inverseForeignKey = @ForeignKey(name = "FK_roles_privileges_to_privileges"),
            uniqueConstraints = @UniqueConstraint(name = "UK_role_to_privilege",
                    columnNames = {"role_id", "privilege_id"})
    )
    private Set<Privilege> privileges;
}
