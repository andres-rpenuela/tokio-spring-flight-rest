-- liquibase formatted sql
-- changeset andres.rpenuela:insert-imag-in-user context=development

ALTER TABLE users ADD resource_id bigint(20) NULL;
ALTER TABLE users ADD CONSTRAINT users_resources_FK FOREIGN KEY (resource_id) REFERENCES resources(id);
