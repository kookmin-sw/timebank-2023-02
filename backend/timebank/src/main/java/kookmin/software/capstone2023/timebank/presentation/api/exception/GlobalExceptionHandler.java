package kookmin.software.capstone2023.timebank.presentation.api.exception;

import jakarta.validation.ConstraintViolationException;
import kookmin.software.capstone2023.timebank.application.exception.*;
import kookmin.software.capstone2023.timebank.core.LoggerExtensions;
import kookmin.software.capstone2023.timebank.presentation.api.model.ErrorResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerExtensions.logger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ApplicationErrorCode.BAD_REQUEST.getValue(),
                        ApplicationErrorCode.BAD_REQUEST.getMessage(),
                        e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = null;
        if (e.getBindingResult().getAllErrors().stream().findFirst().orElse(null) != null) {
            errorMessage = e.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ApplicationErrorCode.BAD_REQUEST.getValue(), errorMessage));
    }

    //    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse(ApplicationErrorCode.BAD_REQUEST.getValue(),
//                        e.getConstraintViolations().stream().findFirst().orElse(null)?.getMessage()));
//    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = null;
        if (e.getConstraintViolations().stream().findFirst().orElse(null) != null) {
            errorMessage = e.getConstraintViolations().stream().findFirst().get().getMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ApplicationErrorCode.BAD_REQUEST.getValue(), errorMessage));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getCode().getValue(), e.getMessage() != null ? e.getMessage() : e.getCode().getMessage()));
    }
}
