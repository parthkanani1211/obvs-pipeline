package ai.obvs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OTPVerificationFailedException extends RuntimeException {
    public OTPVerificationFailedException(String message) {
        super(message);
    }

}
