package my.ostrea.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CanNotFindFileWithSpecifiedName extends RuntimeException {
    public CanNotFindFileWithSpecifiedName(String name) {
        super("File with name " + name + " doesn't exist.");
    }
}
