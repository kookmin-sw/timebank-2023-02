package kookmin.software.capstone2023.timebank.application.exception;

public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super(ApplicationErrorCode.CONFLICT, message);
    }
}
