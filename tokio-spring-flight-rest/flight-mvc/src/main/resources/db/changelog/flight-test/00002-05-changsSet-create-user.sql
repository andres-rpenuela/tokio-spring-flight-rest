-- liquibase formatted sql
-- changeset andres.rpenuela:create-user context=development

CREATE TABLE users
(
    id         varchar(13) NOT NULL UNIQUE ,
    name       varchar(100) NOT NULL,
    surname    varchar(100),
    created    TIMESTAMP DEFAULT now(),
    email      varchar(100) NOT NULL UNIQUE,
    password   varchar(100) NOT NULL,
    active     bit DEFAULT 1,
    last_login TIMESTAMP,
    CONSTRAINT users_PK PRIMARY KEY (id)
);


-- init value: https://bcrypt-generator.com/
insert into users (id, name, surname, email, password)
values ('0AVRSA787897A', 'user-name', 'user-surname', 'user@bla.com',
        '$2a$12$lWqfNW9r93S7NnsOEBtMseFV.1Wn/S9qQ0o91ND0hd0xoEDEBGXLW'), -- pwd: user (hash md5)
       ('0AVRSA787897B', 'admin-name', 'admin-surname', 'admin@bla.com',
        '$2a$12$5a6RB9Wu6Gw4V41h7x3PeuwKLwd29v/Vh6b2T.mDzqarTRurI9x2W'); -- pwd: admin (hash md5)