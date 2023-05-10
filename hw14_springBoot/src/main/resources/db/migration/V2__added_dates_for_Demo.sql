insert into client (name) values ('Непервый Петр Леонидович');
insert into client (name) values ('Сидоров Иван Сергеевич');

insert into addresses (address, client_id) values ('Речная 10', 1);
insert into addresses (address, client_id) values ('Горная 199', 2);
insert into phones (number, client_id) values ('8-500-11111', 1);
insert into phones (number, client_id) values ('8-505-11111', 1);
insert into phones (number, client_id) values ('8-800-22222', 2);