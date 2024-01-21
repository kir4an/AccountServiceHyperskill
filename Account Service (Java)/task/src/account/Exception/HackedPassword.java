package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HackedPassword extends RuntimeException{
    public HackedPassword(String message){
        super(message);
    }
}
