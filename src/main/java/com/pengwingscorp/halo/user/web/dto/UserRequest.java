package com.pengwingscorp.halo.user.web.dto;

import com.pengwingscorp.halo.user.domain.UserRole;

public record UserRequest(String login, String password, UserRole role) {
}
