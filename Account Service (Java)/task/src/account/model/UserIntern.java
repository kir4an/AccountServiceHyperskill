package account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User_Intern")
@RequiredArgsConstructor
public class UserIntern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;


    @Column
    @Size(min = 5)
    private String password;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Intern_role",
            joinColumns = @JoinColumn(name = "userIntern_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
    private int percentSolvedTasks;
    private int quantityAttempts;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getPercentSolvedTasks() {
        return percentSolvedTasks;
    }

    public void setPercentSolvedTasks(int percentSolvedTasks) {
        this.percentSolvedTasks = percentSolvedTasks;
    }

    public int getQuantityAttempts() {
        return quantityAttempts;
    }

    public void setQuantityAttempts(int quantityAttempts) {
        this.quantityAttempts = quantityAttempts;
    }

    public UserIntern(String name, String lastname, String email, List<Role> roles) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles;
    }
}
