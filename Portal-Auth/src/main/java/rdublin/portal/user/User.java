package rdublin.portal.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rdublin.portal.commons.domain.AuditedEntity;
import rdublin.portal.privelege.Role;

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
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UK_user_name", columnNames = {"name"}),
        @UniqueConstraint(name = "UK_user_email", columnNames = {"email"}),
})
public class User extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false, name = "name", length = 64)
    private String name;

    @Column(nullable = false, name = "email", length = 64)
    private String email;

    @Column(nullable = true)
    private Date dob;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "FK_user_roles_to_users"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseForeignKey = @ForeignKey(name = "FK_user_roles_to_roles"),
            uniqueConstraints = @UniqueConstraint(name = "UK_user_to_role",
                    columnNames = {"user_id", "role_id"})
    )
    @JsonIgnore
    private Set<Role> roles;
}
