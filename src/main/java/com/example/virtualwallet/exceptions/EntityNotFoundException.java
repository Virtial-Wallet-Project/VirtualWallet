package com.example.virtualwallet.exceptions;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }
}
