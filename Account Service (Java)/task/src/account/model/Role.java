package account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.REGON;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "Role")
public class Role implements Comparable<Role> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRoleType name;


    public Long getId() {
        return id;
    }

    public Role(UserRoleType name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRoleType getName() {
        return name;
    }

    public void setName(UserRoleType name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name.name();
    }

    @Override
    public int compareTo(Role otherRole) {
        return this.name.compareTo(otherRole.getName());
    }
}
