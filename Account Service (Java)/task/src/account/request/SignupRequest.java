package account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String lastname;
    @NotBlank
    @NotNull
    @Pattern(regexp=".+@acme\\.com")
    private String email;
    @NotBlank
    @Size(min = 12,message = "Password length must be 12 chars minimum!")
    private String password;

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
