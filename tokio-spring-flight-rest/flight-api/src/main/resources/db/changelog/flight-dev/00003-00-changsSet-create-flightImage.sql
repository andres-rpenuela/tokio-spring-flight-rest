-- liquibase formatted sql
-- changeset andres.rpenuela:create-flight-image context=development

create table resources
(
    id              bigint(20)   not null primary key auto_increment,
    resource_id     varchar(36)  not null comment 'public id of the image',
    size_resource   int          not null comment 'size of the image in kb' CHECK(size_resource > 0),
    file_name       varchar(255) not null comment 'original filename',
    content_type    varchar(255) comment 'content type of the image'
);
