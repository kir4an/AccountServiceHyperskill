package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectMonthException extends RuntimeException {
    public IncorrectMonthException(String message) {
        super(message);
    }
}
