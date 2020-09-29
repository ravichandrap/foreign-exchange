package com.exchange.exception;

import com.exchange.controller.ExchangeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    static Logger log = LogManager.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, WebRequest webRequest) {
        log.error("handleAllExceptions: {}", e.getLocalizedMessage());

        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());

        ErrorResponse errorResponse = new ErrorResponse("Server Error", details);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DateFormatException.class)
    public final ResponseEntity<ErrorResponse> dateFormatException(DateFormatException e, WebRequest webRequest) {
        log.error("dateFormatException: {}", e.getLocalizedMessage());

        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());

        ErrorResponse errorResponse = new ErrorResponse("Invalid Date", details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
