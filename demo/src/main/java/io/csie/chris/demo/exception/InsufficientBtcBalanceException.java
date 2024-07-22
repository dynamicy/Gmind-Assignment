package io.csie.chris.demo.exception;

public class InsufficientBtcBalanceException extends RuntimeException {
    public InsufficientBtcBalanceException(String message) {
        super(message);
    }
}