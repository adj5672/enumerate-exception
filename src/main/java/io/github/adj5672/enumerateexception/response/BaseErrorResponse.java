package io.github.adj5672.enumerateexception.response;

public class BaseErrorResponse {

    private String code = "";
    private String message = "";

    public BaseErrorResponse() {
    }

    public BaseErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
