# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        bigint not null,
  title_de                  varchar(255),
  title_en                  varchar(255),
  description_de            varchar(255),
  description_en            varchar(255),
  technologies              varchar(255),
  development_start         timestamp,
  development_end           timestamp,
  main_image                bytea,
  download_link             varchar(255),
  constraint pk_project primary key (id))
;

create table project_type (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_project_type primary key (id))
;

create table users (
  id                        bigint not null,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_users primary key (id))
;


create table project_project_type (
  project_id                     bigint not null,
  project_type_id                bigint not null,
  constraint pk_project_project_type primary key (project_id, project_type_id))
;
create sequence project_seq;

create sequence project_type_seq;

create sequence users_seq;




alter table project_project_type add constraint fk_project_project_type_proje_01 foreign key (project_id) references project (id) on delete restrict on update restrict;

alter table project_project_type add constraint fk_project_project_type_proje_02 foreign key (project_type_id) references project_type (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists project;

drop table if exists project_project_type;

drop table if exists project_type;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists project_seq;

drop sequence if exists project_type_seq;

drop sequence if exists users_seq;

