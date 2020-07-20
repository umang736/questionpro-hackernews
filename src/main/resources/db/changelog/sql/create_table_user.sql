--liquibase formatted sql

--changeset umgupta:1
create table user (
 id varchar(50),
 create_time datetime not null,
 CONSTRAINT user_pk PRIMARY KEY (id)
);
--rollback drop table user;