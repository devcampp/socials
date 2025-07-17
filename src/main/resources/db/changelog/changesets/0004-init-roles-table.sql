--liquibase formatted sql

--changeset shokhrukh:0004-init-roles-table

INSERT INTO roles (name)
VALUES ('USER');

--rollback DELETE FROM roles WHERE name = 'USER';