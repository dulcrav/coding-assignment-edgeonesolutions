package pl.marcin.zubrzycki.rewards.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException {
    public UserNotFound(String userId) {
        super("User with ID: " + userId + " not found.");
    }
}