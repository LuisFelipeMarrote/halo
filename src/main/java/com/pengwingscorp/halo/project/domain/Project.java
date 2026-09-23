package com.pengwingscorp.halo.project.domain;

import com.pengwingscorp.halo.base_entities.Entity;
import com.pengwingscorp.halo.exception.InvalidStringContent;

import java.util.Date;
import java.util.UUID;

public class Project extends Entity {
    private String name;
    private String description;
    private final Date createAt;

    public Project(UUID id, String name, String description, Date createAt) {
        setId(id);
        this.name = validate(name, "name");
        this.description = validate(description, "description");
        this.createAt = createAt;
    }

    public Project(Builder builder) {
        name = format(validate(builder.nome, "nome"));
        description = format(validate(builder.description, "description"));
        createAt = builder.createAt;
    }

    private String validate(String value, String field) {
        if(value.isBlank()) throw new InvalidStringContent(field);
        return value;
    }

    private String format(String value) {
        return value.trim();
    }

    public void update(String name, String description) {
        this.name = format(validate(name, "nome"));
        this.description = format(validate(description, "description"));      }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public static class Builder{
        private String nome;
        private String description;
        private Date createAt = new Date();

        public Project.Builder setNome(String nome) {
            this.nome = nome;
            return this;
        }

        public Project.Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Project.Builder setCreateAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }


}
