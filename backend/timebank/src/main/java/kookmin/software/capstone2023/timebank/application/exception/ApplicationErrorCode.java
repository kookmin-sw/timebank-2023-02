package kookmin.software.capstone2023.timebank.application.exception;

import lombok.Getter;

@Getter
public enum ApplicationErrorCode {
    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다."),
    UNAUTHORIZED("UNAUTHORIZED", ""),
    FORBIDDEN("FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND("NOT_FOUND", "존재하지 않는 리소스입니다."),
    CONFLICT("CONFLICT", "이미 존재하는 리소스입니다."),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류입니다.");

    private final String value;
    private final String message;

    ApplicationErrorCode(String value, String message) {
        this.value = value;
        this.message = message;
    }
}
