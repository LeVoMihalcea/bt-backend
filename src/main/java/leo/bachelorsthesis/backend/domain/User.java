package leo.bachelorsthesis.backend.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "UC_email_unique", columnNames = "email")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First Name must not be blank!")
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last Name must not be blank!")
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email must not be blank!")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @NotBlank(message = "Password must not be blank!")
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "host")
    private Set<Room> roomsAdministrated;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    @OrderBy("id ASC")
    private Set<Room> roomsJoined;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
