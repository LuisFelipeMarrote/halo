package com.pengwingscorp.halo.task.web.dto;


import jakarta.validation.constraints.NotBlank;

public record ChangeTaskStatusRequest(
        @NotBlank(message = "Task status is required.")
        String status
) {}
