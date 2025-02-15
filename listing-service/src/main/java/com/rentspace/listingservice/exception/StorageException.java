package com.rentspace.listingservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class StorageException extends RuntimeException{
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
