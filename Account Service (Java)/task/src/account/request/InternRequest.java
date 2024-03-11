package account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InternRequest {
    private String name;
    private String lastname;
    private String email;
    @JsonProperty("Resume")
    private String Resume;

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

    public String getResume() {
        return Resume;
    }

    public void setResume(String resume) {
        this.Resume = resume;
    }
}
