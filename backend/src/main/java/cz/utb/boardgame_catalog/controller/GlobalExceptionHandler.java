package cz.utb.boardgame_catalog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle invalid parameters
     * 
     * @param ex the exception
     * @return a ResponseEntity with a 400 status code and an error message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String expectedType = ex.getRequiredType().getSimpleName();
        String actualValue = ex.getValue().toString();
        return ResponseEntity.status(400)
                .body("Invalid parameter, expected: " + expectedType + ", but got: " + actualValue);
    }
}