-- liquibase formatted sql
-- changeset andres.rpenuela:create-flight-booking context=development

DROP TABLE IF EXISTS flight_bookings;
CREATE TABLE flight_bookings
(
    id  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id varchar(13) NOT NULL,
    flight_id bigint(20) unsigned NOT NULL,
    created    TIMESTAMP DEFAULT now() NULL,
    `locator`  varchar(255) not null unique comment 'Locator id',
    CONSTRAINT flight_bookings_fk_flights FOREIGN KEY (flight_id) REFERENCES flights(id),
    CONSTRAINT flight_bookings_fk_users FOREIGN KEY (user_id) REFERENCES users(id),
    primary key (id)
);
