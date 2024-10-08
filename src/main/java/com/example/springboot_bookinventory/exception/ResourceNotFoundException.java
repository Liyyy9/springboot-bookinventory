package com.example.springboot_bookinventory.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
        super("Book Not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
