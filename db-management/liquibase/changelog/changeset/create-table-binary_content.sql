--liquibase formatted sql
--changeset denMg:3

CREATE TABLE IF NOT EXISTS binary_content (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY,
  bytes bytea,
  CONSTRAINT binary_content_pk PRIMARY KEY (id)
);

--rollback DROP TABLE binary_content;
