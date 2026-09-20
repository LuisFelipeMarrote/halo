package com.pengwingscorp.halo.task.web.dto;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;

import java.util.Date;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        String status,
        UUID project_id,
        Date createAt
) {}
