# --- Sample dataset

# --- !Ups

insert into company (id,name) values (  1,'Apple Inc.');
insert into company (id,name) values (  2,'Thinking Machines');
insert into company (id,name) values (  3,'RCA');
insert into company (id,name) values (  4,'Netronics');
insert into company (id,name) values (  5,'Tandy Corporation');
insert into company (id,name) values (  6,'Commodore International');

insert into section (id,name) values (  1,'Горнолыжная одежда');
insert into section (id,name) values (  2,'Одежда для туризма');
insert into section (id,name) values (  3,'Снаряжение');
insert into section (id,name) values (  4,'Одежда для детей');


# --- !Downs

delete from computer;
delete from company;
