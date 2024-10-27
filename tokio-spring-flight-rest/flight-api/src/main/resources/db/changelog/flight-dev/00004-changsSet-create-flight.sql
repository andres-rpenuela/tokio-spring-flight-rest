-- liquibase formatted sql
-- changeset andres.rpenuela:create-flight context=development

DROP TABLE IF EXISTS flights;

CREATE TABLE flights (
                         id                      BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
                         created                 DATETIME DEFAULT CURRENT_TIMESTAMP,
                         `number`                VARCHAR(255) NOT NULL UNIQUE COMMENT 'flight number',
                         airport_departure_id    VARCHAR(3) NOT NULL COMMENT 'departure airport',
                         airport_arrival_id      VARCHAR(3) NOT NULL COMMENT 'arrival airport',
                         departure_time          TIMESTAMP NOT NULL COMMENT 'departure/output time',
                         status                  VARCHAR(50) NOT NULL COMMENT 'status of the flight',
                         capacity                INT NOT NULL COMMENT 'capacity passengers for this flight',
                         occupancy               INT NOT NULL COMMENT 'occupancy passengers for this flight, calculated from the passengers list',
                         resource_id             BIGINT(20) COMMENT 'resource id fk',
                         version                 INT DEFAULT 1,
                         PRIMARY KEY (id),
                         FOREIGN KEY (resource_id) REFERENCES resources(id),
                         FOREIGN KEY (airport_departure_id) REFERENCES airports(acronym),
                         FOREIGN KEY (airport_arrival_id) REFERENCES airports(acronym)
);


-- init data
INSERT INTO flights (id, `number`, airport_departure_id, airport_arrival_id, departure_time, status, capacity,
                     occupancy)
VALUES (1, 'BCN0001', 'BCN', 'GLA',now(), 'SCHEDULED', 100, 30),
       (2, 'BCN0002', 'BCN', 'GLA',now(), 'CANCELLED', 30, 30);
