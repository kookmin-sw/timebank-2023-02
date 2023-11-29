package kookmin.software.capstone2023.timebank.application.exception;

public class InternalServerErrorException extends ApplicationException {
    public InternalServerErrorException(String message) {
        super(ApplicationErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}
