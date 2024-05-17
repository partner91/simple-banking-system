package hr.hrsak.bankingdemo.exceptions.handlers;

import hr.hrsak.bankingdemo.exceptions.AccountNotFoundException;
import hr.hrsak.bankingdemo.exceptions.ErrorProcessingTransaction;
import hr.hrsak.bankingdemo.exceptions.reponses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleAccountNotFoundException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErrorProcessingTransaction.class)
    public ResponseEntity<ErrorResponse> handleErrorProcessingTransaction(AccountNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
