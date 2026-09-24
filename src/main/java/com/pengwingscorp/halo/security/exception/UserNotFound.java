package com.pengwingscorp.halo.security.exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super("User not found");
    }
}
