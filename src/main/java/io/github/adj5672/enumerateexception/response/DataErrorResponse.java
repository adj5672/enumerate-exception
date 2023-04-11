package io.github.adj5672.enumerateexception.response;

public class DataErrorResponse extends BaseErrorResponse {

    private Object data;

    public DataErrorResponse() {
    }

    public DataErrorResponse(String code, String message, Object data) {
        super(code, message);
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
