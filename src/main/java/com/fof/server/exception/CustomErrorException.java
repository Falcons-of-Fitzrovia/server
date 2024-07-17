package com.fof.server.exception;

public class CustomErrorException extends RuntimeException {
    public CustomErrorException(String message) {
        super(message);
    }

}
