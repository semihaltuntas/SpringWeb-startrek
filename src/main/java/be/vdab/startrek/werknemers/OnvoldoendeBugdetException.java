package be.vdab.startrek.werknemers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeBugdetException extends RuntimeException {
    public OnvoldoendeBugdetException() {
        super("Onvoldoende budget");
    }
}
