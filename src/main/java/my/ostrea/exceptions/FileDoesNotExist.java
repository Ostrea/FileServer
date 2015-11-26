package my.ostrea.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileDoesNotExist extends RuntimeException {
    public FileDoesNotExist(String uuid) {
        super("File with" + uuid + " UUID doesn't exist.");
    }
}
