--liquibase formatted sql
--changeset denMg:1

CREATE TABLE IF NOT EXISTS raw_data (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY,
  update JSONB NOT NULL,
  CONSTRAINT raw_data_pk PRIMARY KEY (id)
);

--rollback DROP TABLE raw_data;
