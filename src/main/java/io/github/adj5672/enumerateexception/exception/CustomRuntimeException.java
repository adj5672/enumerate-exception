package io.github.adj5672.enumerateexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class CustomRuntimeException extends RuntimeException {

    private final Map<String, Object> parameters = new HashMap<>();
    private Object body;

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

    public abstract String getCode();

    public abstract HttpStatus getHttpStatus();

    public Supplier<CustomRuntimeException> toSupplier() {
        return () -> {
            throw this;
        };
    }

    public CustomRuntimeException addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    @Nullable
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Nullable
    public Object getBody() {
        return this.body;
    }

    public CustomRuntimeException setBody(Object body) {
        this.body = body;
        return this;
    }

    protected String stringifyParameters() {
        String enumeratedParameters = parameters.entrySet().stream()
            .map(entry -> entry.getKey() + " : " + entry.getValue())
            .collect(Collectors.joining(", "));
        return StringUtils.hasText(enumeratedParameters) ? "(" + enumeratedParameters + ")" : "";
    }
}
