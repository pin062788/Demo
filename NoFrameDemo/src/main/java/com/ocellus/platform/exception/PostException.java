package com.ocellus.platform.exception;

public class PostException extends Exception {

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Exception e) {
        super(message, e);
    }

}
