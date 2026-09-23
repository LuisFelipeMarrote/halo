package com.pengwingscorp.halo.task.web.dto;

import jakarta.validation.constraints.NotNull;

public record TaskRequest(
        @NotNull(message = "Task title is required.")
        String title,
        String description
) {}
