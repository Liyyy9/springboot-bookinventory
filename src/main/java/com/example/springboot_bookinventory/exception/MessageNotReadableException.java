package com.example.springboot_bookinventory.exception;

public class MessageNotReadableException extends RuntimeException{

    public MessageNotReadableException() {
        super("Unable to read request data");
    }

}
