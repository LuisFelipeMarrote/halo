package com.pengwingscorp.halo.exception;

public record ErrorResponse(
        String code,
        String message
) {}