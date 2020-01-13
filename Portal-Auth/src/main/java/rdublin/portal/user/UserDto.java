package rdublin.portal.user;

import org.apache.logging.log4j.util.Strings;
import rdublin.portal.commons.domain.AuditedEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDto extends AuditedEntity {
    private Integer id;
    private String name;
    private String email;
    private Date dob;
    private String gender;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + (dob == null ? "-" : new SimpleDateFormat("yyyy-MM-dd").format(dob)) + '\'' +
                ", gender='" + (Strings.isBlank(gender) ? "NA" : gender) + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
