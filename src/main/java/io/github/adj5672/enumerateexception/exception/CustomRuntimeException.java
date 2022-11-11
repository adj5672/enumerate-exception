package io.github.adj5672.enumerateexception.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException() {
    }

    public CustomRuntimeException(String message) {
        super(message);
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomRuntimeException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return 0;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public Supplier<CustomRuntimeException> toSupplier() {
        return () -> {
            throw this;
        };
    }
}
