package com.ra.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        System.out.println("Validation error occurred: " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));


        /*
         * [
         * Field error in object 'userRequestDTO' on field 'firstName': rejected value [];
         * codes [NotBlank.userRequestDTO.firstName,NotBlank.firstName,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userRequestDTO.firstName,firstName]; arguments []; default message [firstName]];
         * default message [First name is required]
         * ]
         * */

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            errorResponse.setError("Payload validation error");
        } else if (e instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" " + 1));
            errorResponse.setError("Parameter validation error");
        }

        errorResponse.setMessage(message);
        return errorResponse;
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    // that means the server response will be 500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(Exception e, WebRequest request) {
        System.out.println("Validation error occurred: " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        if (e instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setMessage("Failed to convert value of type");
            errorResponse.setError("Type mismatch error");
        }


        /*
         * Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Method parameter 'pageSize': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: ""10""]
         * */

        return errorResponse;
    }


}
