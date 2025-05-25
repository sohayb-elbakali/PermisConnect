package com.perm.aop.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception details
        logger.error("Unhandled exception occurred: ", ex);

        // Return a generic error response
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(),
                                   HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
