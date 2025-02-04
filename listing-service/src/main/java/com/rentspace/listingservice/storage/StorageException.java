package com.rentspace.listingservice.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class StorageException extends RuntimeException{
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
