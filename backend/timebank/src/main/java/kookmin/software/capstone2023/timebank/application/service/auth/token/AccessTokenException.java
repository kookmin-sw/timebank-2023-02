package kookmin.software.capstone2023.timebank.application.service.auth.token;

import kookmin.software.capstone2023.timebank.application.exception.ApplicationErrorCode;
import kookmin.software.capstone2023.timebank.application.exception.ApplicationException;

public class AccessTokenException {
    public static class InvalidTokenException extends ApplicationException {
        public InvalidTokenException() {
            super(ApplicationErrorCode.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    public static class TokenExpiredException extends ApplicationException {
        public TokenExpiredException() {
            super(ApplicationErrorCode.UNAUTHORIZED, "만료된 토큰입니다.");
        }
    }
}
