create table addresses
(
    id   bigserial not null primary key,
    address varchar(255)
);

alter table client add column address_id bigint;
alter table client add constraint  frnKeyAdressOfClient foreign key (address_id) references addresses(id);

create table phones
(
    id   bigserial not null primary key,
    number varchar(30),
    client_id bigint
);

alter table phones add constraint frnKeyPhonesofClient foreign key (client_id) references client(id);