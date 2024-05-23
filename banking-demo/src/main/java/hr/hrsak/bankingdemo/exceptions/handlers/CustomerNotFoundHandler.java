package hr.hrsak.bankingdemo.exceptions.handlers;

import hr.hrsak.bankingdemo.exceptions.CustomerNotFoundException;
import hr.hrsak.bankingdemo.exceptions.reponses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerNotFoundHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND
        );
    }
}
