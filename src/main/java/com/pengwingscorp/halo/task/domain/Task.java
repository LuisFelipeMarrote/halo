package com.pengwingscorp.halo.task.domain;

import com.pengwingscorp.halo.base_entities.Entity;
import com.pengwingscorp.halo.exception.InvalidStringContent;

import java.util.Date;
import java.util.UUID;

public class Task extends Entity {
    private String title;
    private String description;
    private EnumTaskStatus status;
    private UUID project_id;
    private Date createAt;

    public Task(String title, String description, EnumTaskStatus status, UUID project_id, Date createAt, UUID id) {
        this.setId(id);
        this.title = title;
        this.description = description;
        this.status = status;
        this.project_id = project_id;
        this.createAt = createAt;
    }

    public Task(Builder builder) {
        this.title = format(validate(builder.title, "title"));
        this.description = format(validate(builder.description, "description"));
        this.status = builder.status;
        this.project_id = builder.project_id;
        this.createAt = builder.createAt;
    }

    private String validate(String value, String field) {
        if(value.isBlank()) throw new InvalidStringContent(field);
        return value;
    }

    private String format(String value) {
        return value.trim();
    }


    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void changeStatus(EnumTaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status.getDescription();
    }

    public UUID getProject_id() {
        return project_id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public static class Builder{
        private String title;
        private String description;
        private EnumTaskStatus status;
        private UUID project_id;
        private Date createAt;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStatus(EnumTaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = EnumTaskStatus.valueOf(status);
            return this;
        }

        public Builder setProject_id(UUID project_id) {
            this.project_id = project_id;
            return this;
        }

        public Builder setCreateAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
