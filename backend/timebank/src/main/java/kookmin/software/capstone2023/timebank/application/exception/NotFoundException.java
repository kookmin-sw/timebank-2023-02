package kookmin.software.capstone2023.timebank.application.exception;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(ApplicationErrorCode.NOT_FOUND, message);
    }
}
