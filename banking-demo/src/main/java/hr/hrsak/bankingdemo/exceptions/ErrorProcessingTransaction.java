package hr.hrsak.bankingdemo.exceptions;

public class ErrorProcessingTransaction extends RuntimeException{
    public ErrorProcessingTransaction(String message) {
        super(message);
    }
}
