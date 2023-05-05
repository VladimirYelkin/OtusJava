drop table if exists client;
drop table if exists addresses;
drop table if exists phones;

create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table addresses
(
    id   bigserial not null primary key,
    address varchar(255),
    client_id bigint not null references client(id)

);

create table phones
(
    id  bigserial not null primary key,
    number varchar(30),
    client_id bigint not null references client(id)
);
