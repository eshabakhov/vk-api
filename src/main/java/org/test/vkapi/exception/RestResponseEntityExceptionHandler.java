package org.test.vkapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.test.vkapi.dto.ErrorResponseDTO;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setHttpCode((short) HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        log.warn(ex.getMessage(), ex);
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}