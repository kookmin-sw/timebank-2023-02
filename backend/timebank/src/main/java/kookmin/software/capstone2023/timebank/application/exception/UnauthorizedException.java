package kookmin.software.capstone2023.timebank.application.exception;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(ApplicationErrorCode.UNAUTHORIZED, message);
    }
    public UnauthorizedException() {
        super(ApplicationErrorCode.UNAUTHORIZED, null);
    }
}
