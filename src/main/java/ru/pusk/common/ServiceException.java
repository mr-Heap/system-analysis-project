package ru.pusk.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceException extends RuntimeException{
    private final Object error;
    public ServiceException(Object error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    public ServiceException(Object error) {
        this.error = error;
    }

}
