package edu.bbte.idde.controller;

import edu.bbte.idde.controller.dto.ErrorResponseDto;
import edu.bbte.idde.repository.EntityNotFoundException;
import edu.bbte.idde.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleEntityNotFound(EntityNotFoundException ex) {
        return new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleServiceException(ServiceException ex) {
        return new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                message
        );
    }

    // fallback
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGenericException(Exception ex) {
        return new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
    }
}
