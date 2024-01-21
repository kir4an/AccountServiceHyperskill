package account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Entity

@AllArgsConstructor
@RequiredArgsConstructor
public class SecurityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private SecurityAction action;
    private String subject;
    private String object;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SecurityAction getAction() {
        return action;
    }

    public void setAction(SecurityAction action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SecurityEvent(SecurityAction action, String subject, String object, String path) {
        this.date = new Date();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

}
