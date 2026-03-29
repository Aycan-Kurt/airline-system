package com.aycan.airlineapi.exception;

public class AlreadyCheckedInException extends RuntimeException {

    public AlreadyCheckedInException(String message) {
        super(message);
    }
}