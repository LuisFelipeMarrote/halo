package com.pengwingscorp.halo.project.web.dto;

import java.util.Date;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        Date createAt
) {}
