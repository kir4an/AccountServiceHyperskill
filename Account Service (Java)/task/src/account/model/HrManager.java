package account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Queue;

@Entity
@Table
public class HrManager {
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

    @Column
    private String secretKey;
    @OneToMany
    private Queue<UserIntern> internQueue;

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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Queue<UserIntern> getInternQueue() {
        return internQueue;
    }

    public void setInternQueue(Queue<UserIntern> internQueue) {
        this.internQueue = internQueue;
    }
}
