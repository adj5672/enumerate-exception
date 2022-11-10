package com.github.danny.enumerateexception.response;

public class BaseErrorResponse {

    private int code = 0;
    private String message = "";

    public BaseErrorResponse() {
    }

    public BaseErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
