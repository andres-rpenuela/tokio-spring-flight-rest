-- liquibase formatted sql
-- changeset andres.rpenuela:create-user-with-roles context=development

DROP TABLE IF EXISTS users_with_roles;
CREATE TABLE users_with_roles
(
    user_id varchar(13) NOT NULL,
    role_id bigint(20) NOT NULL,
    CONSTRAINT users_with_names_FK FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT users_with_names_FK_1 FOREIGN KEY (role_id) REFERENCES roles (id),
    PRIMARY KEY (user_id,role_id)
);

-- init value
insert into users_with_roles (user_id, role_id)
values ((select id from users u where u.email = 'user@bla.com'), (select id from roles r where r.name = 'USER')),
       ((select id from users u where u.email = 'admin@bla.com'), (select id from roles r where r.name = 'ADMIN'));

