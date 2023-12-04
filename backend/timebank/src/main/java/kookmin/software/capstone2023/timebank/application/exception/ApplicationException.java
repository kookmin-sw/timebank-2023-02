package kookmin.software.capstone2023.timebank.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    protected final ApplicationErrorCode code;
    protected final String message;

    public ApplicationException(ApplicationErrorCode code, String message) {
        super(message);
        this.code = code;
        this.message = this.getMessage();
    }
}
