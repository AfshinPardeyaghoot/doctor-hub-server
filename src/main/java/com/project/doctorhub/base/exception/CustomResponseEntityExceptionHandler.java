package com.project.doctorhub.base.exception;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.base.dto.HttpResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<HttpResponse<?>> handleNotFoundException(NotFoundException ex) {
        ex.printStackTrace();
        HttpResponseStatus status = new HttpResponseStatus(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(
                new HttpResponse<>(status),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpException.class)
    public final ResponseEntity<HttpResponse<?>> handleHttpException(HttpException ex) {
        ex.printStackTrace();
        HttpResponseStatus status = new HttpResponseStatus(
                ex.getMessage(),
                ex.getStatus()
        );
        return new ResponseEntity<>(
                new HttpResponse<>(status),
                ex.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<HttpResponse<?>> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        HttpResponseStatus status = new HttpResponseStatus(
                ".خطا در پزدازش اطلاعات!\nلطفابا پشتیبانی تماس بگیرید",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(
                new HttpResponse<>(status),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ex.printStackTrace();
        HttpResponseStatus httpResponseStatus = new HttpResponseStatus(
                ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
                status
        );
        return new ResponseEntity<>(
                new HttpResponse<>(httpResponseStatus),
                status
        );
    }


}

