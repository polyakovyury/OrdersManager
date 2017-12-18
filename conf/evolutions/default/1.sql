# --- First database schema

# --- !Ups

create table section (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_section primary key (id))
;

create table category (
  id                        bigint not null,
  name                      varchar(255),
  has_standart_size         boolean,
  has_custom_size           boolean,
  section_id                bigint,
  constraint pk_category primary key (id))
;

create table improvement (
  id                        bigint not null,
  name                      varchar(255),
  constraint improvement primary key (id))
;

create table company (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_company primary key (id))
;

create table computer (
  id                        bigint not null,
  name                      varchar(255),
  introduced                timestamp,
  discontinued              timestamp,
  company_id                bigint,
  constraint pk_computer primary key (id))
;

create sequence section_seq start with 1000;

create sequence category_seq start with 1000;

create sequence improvement_seq start with 1000;

create sequence company_seq start with 1000;

create sequence computer_seq start with 1000;

alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_1 on computer (company_id);

alter table category add constraint fk_category_section_1 foreign key (section_id) references section (id) on delete restrict on update restrict;
create index ix_category_section_1 on category (section_id);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists section;

drop table if exists improvement;

drop table if exists category;

drop table if exists company;

drop table if exists computer;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists section_seq;

drop sequence if exists improvement_seq;

drop sequence if exists category_seq;

drop sequence if exists company_seq;

drop sequence if exists computer_seq;

