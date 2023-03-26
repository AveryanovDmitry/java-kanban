package com.praktikum.app.services.http.exceptions;

public class KVTaskClientException extends RuntimeException {
    public KVTaskClientException(String message, Throwable exception) {
        System.out.println(message);
        exception.printStackTrace();
    }
}

