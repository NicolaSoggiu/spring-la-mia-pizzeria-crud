package com.experis.course.pizzeria.exception;

public class NameUniqueException extends RuntimeException {
    public NameUniqueException(String message) {
        super(message);
    }
}
