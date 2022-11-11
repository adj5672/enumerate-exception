package io.github.adj5672.enumerateexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CustomRuntimeException extends RuntimeException {

    private final Map<String, Object> parameters = new HashMap<>();

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

    public CustomRuntimeException addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    protected String stringifyParameters() {
        String enumeratedParameters = parameters.entrySet().stream()
            .map(entry -> entry.getKey() + " : " + entry.getValue())
            .collect(Collectors.joining(", "));
        return StringUtils.hasText(enumeratedParameters) ? "(" + enumeratedParameters + ")" : "";
    }
}
