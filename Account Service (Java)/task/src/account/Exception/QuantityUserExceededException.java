package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantityUserExceededException extends RuntimeException {
    public QuantityUserExceededException(String message) {
        super(message);
    }
}
