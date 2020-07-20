--liquibase formatted sql

--changeset umgupta:1
create table user (
 id varchar(50),
 create_time datetime not null,
 data_fetched_at datetime not null,
 CONSTRAINT user_pk PRIMARY KEY (id)
);
--rollback drop table user;