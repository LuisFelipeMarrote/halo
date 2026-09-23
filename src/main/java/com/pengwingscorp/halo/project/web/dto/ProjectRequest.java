package com.pengwingscorp.halo.project.web.dto;

import jakarta.validation.constraints.NotNull;

public record ProjectRequest(
        @NotNull(message = "Project name is required.")
        String name,
        String description
) {}

