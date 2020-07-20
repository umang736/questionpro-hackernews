--liquibase formatted sql

--changeset umgupta:3
CREATE TABLE comment
( id bigint,
  author_id varchar(50),
  parent_story_id bigint,
  parent_comment_id bigint,
  child_comments_count smallint not null,
  text varchar(10000),
  create_time datetime not null,
  deleted TINYINT(1) not null default false,
  data_fetched_at datetime not null,
  CONSTRAINT comment_pk PRIMARY KEY (id),
  CONSTRAINT fk_comment_story
    FOREIGN KEY (parent_story_id)
    REFERENCES story(id),
  CONSTRAINT fk_comment_comment
    FOREIGN KEY (parent_comment_id)
    REFERENCES comment(id)
);
--rollback drop table comment;