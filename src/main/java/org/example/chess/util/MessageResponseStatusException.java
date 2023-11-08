package org.example.chess.util;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageResponseStatusException extends ResponseStatusException {

    private final String message;

    public static MessageResponseStatusException getNonNullIdException(Long value) {
        return new MessageResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrectly made request.",
                "Field: id. Error: must be blank. Value: " + value);
    }

    public static MessageResponseStatusException getNullIdException() {
        return new MessageResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrectly made request.",
                "Field: id. Error: must not be blank. Value: null");
    }

    public static MessageResponseStatusException getNotFoundException(String name, Long value) {
        return new MessageResponseStatusException(HttpStatus.NOT_FOUND, "The required object was not found.",
                name + " with id=" + value + " was not found");
    }

    public static MessageResponseStatusException getConflictException(String message) {
        return new MessageResponseStatusException(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.", message);
    }

    public static MessageResponseStatusException getForbiddenException() {
        return new MessageResponseStatusException(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.", "Forbidden.");
    }

    public MessageResponseStatusException(HttpStatus status, String reason, String message) {
        super(status, reason);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(message, this.getCause());
    }
}
