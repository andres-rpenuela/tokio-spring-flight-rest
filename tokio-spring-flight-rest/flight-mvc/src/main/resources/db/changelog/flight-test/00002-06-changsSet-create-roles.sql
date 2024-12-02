-- liquibase formatted sql
-- changeset andres.rpenuela:create-roles context=development

DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id   bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

insert into roles (name)
values ('USER'),
       ('ADMIN');