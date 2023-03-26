package com.praktikum.app.services.http.exceptions;

public class KVServerLoadException extends RuntimeException {
    public KVServerLoadException(String message, Throwable exception) {
        System.out.println(message);
        exception.printStackTrace();
    }
}
