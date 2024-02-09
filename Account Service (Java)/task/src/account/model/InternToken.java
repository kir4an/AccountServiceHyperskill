//package account.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//
//@Entity
//@Table
//public class InternToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column
//    private String token;
//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_intern_id")
//    private UserIntern userIntern;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public UserIntern getUserIntern() {
//        return userIntern;
//    }
//
//    public void setUserIntern(UserIntern userIntern) {
//        this.userIntern = userIntern;
//    }
//}
