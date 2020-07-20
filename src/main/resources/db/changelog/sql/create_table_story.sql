--liquibase formatted sql

--changeset umgupta:2
CREATE TABLE story
( id bigint,
  author_id varchar(50),
  score bigint not null,
  create_time datetime not null,
  title varchar(255),
  url varchar(255),
  data_fetched_at datetime not null,
  CONSTRAINT story_pk PRIMARY KEY (id)
);
--rollback drop table story;