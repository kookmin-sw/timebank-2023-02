package kookmin.software.capstone2023.timebank.presentation.api.model;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final String debug;

    public ErrorResponse(String code, String message, String debug) {
        this.code = code;
        this.message = message;
        this.debug = debug;
    }

    public ErrorResponse(String code, String message) {
        this(code, message, null);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDebug() {
        return debug;
    }
}
