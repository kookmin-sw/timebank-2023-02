package kookmin.software.capstone2023.timebank.application.exception;

public class ForbiddenException extends ApplicationException {
    public ForbiddenException(String message) {
        super(ApplicationErrorCode.FORBIDDEN, message);
    }
}
