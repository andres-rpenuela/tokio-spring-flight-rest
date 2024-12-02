-- liquibase formatted sql
-- changeset andres.rpenuela:create-flight context=development

DROP TABLE IF EXISTS flights;
CREATE TABLE flights
(
    id                      bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    created                 datetime     DEFAULT CURRENT_TIMESTAMP,
    `number`                varchar(255) NOT NULL UNIQUE COMMENT 'flight number',
    airport_departure_id    varchar(3)   NOT NULL COMMENT 'departure airport' REFERENCES airports(acronym),
    airport_arrival_id      varchar(3)   NOT NULL COMMENT 'arrival airport' REFERENCES airports(acronym),
    departure_time          TIMESTAMP    NOT NULL COMMENT 'departure/output time',
    status                  varchar(50)  NOT NULL COMMENT 'status of the flight',
    capacity                INT          NOT NULL COMMENT 'capacity passengers for this flight' CHECK(capacity >= 0),
    occupancy               INT          NOT NULL COMMENT 'occupancy passengers for this flight, it is calculated form the passengers list' CHECK(capacity >= 0 and occupancy <= capacity),
    resource_id             bigint(20) COMMENT 'resource id fk',
    version                 INT          default (1),
    PRIMARY KEY (id),
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- init data
INSERT INTO flights (id, `number`, airport_departure_id, airport_arrival_id, departure_time, status, capacity,
                     occupancy)
VALUES (1, 'BCN0001', 'BCN', 'GLA',now(), 'SCHEDULED', 100, 30),
       (2, 'BCN0002', 'BCN', 'GLA',now()+20, 'CANCELLED', 30, 30);
