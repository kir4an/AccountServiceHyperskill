package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserLockedException extends RuntimeException {
    public UserLockedException(String message) {
        super(message);
    }
}
