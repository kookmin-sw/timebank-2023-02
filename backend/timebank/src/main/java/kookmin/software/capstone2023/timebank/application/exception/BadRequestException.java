package kookmin.software.capstone2023.timebank.application.exception;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(ApplicationErrorCode.BAD_REQUEST, message);
    }
}
