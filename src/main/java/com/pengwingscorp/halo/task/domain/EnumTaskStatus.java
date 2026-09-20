package com.pengwingscorp.halo.task.domain;

public enum EnumTaskStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String description;

    EnumTaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isValidType(String value) {
        if(value == null || value.isBlank()) return false;
        for (EnumTaskStatus status : EnumTaskStatus.values()) {
            // como o type do enum é do mesmo formato e nome da descrição
            // então se a descrição bate com o valor, logo ele consegue ser convertido.
            if (status.description.equals(value)) return true;
        }
        return false;
    }
}
