package com.pengwingscorp.halo.exception;

public class InvalidStringContent extends RuntimeException{

    public InvalidStringContent(String field) {
        super("Invalid content in field: '" + field + "'");
    }
}
