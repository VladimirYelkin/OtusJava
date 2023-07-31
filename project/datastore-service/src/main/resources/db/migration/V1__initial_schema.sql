--create table message
--(
--    id   bigserial    not null primary key,
--    room_id  varchar(50)  not null,
--    msg_text varchar(50) not null
--);

create table study
(
    id  bigserial    not null primary key,
    telegram_UID  varchar(50) unique not null,
    firstName  varchar(50)  not null,
    secondName varchar(50) not null
);

create table coach
(
    id  bigserial    not null primary key,
    telegram_UID varchar(50) unique not null,
    firstName  varchar(50)  not null,
    secondName varchar(50) not null
);


create table typetraining
(
    id  bigserial  not null primary key,
    nameoftraining varchar(50) not null,
    duration  time  not null
);


create table plannedtraining
(
   id  bigserial not null primary key,
   idtype bigint not null,
   datastart timestamp without time zone not null,
   idcoach bigint not null,
   minstudy bigint not null,
   maxstudy bigint not null
);

ALTER TABLE public.plannedtraining ADD CONSTRAINT plannedtraining_fk0 FOREIGN KEY (idtype) REFERENCES typetraining(id);
ALTER TABLE public.plannedtraining ADD CONSTRAINT plannedtraining_fk1 FOREIGN KEY (idcoach) REFERENCES coach(id);


create table studyontranining
(
    id  bigserial not null primary key,
    idtraining bigint not null,
    idstudy bigint not null
);

ALTER TABLE public.studyontranining ADD CONSTRAINT studyontranining_fk0 FOREIGN KEY (idtraining) REFERENCES plannedtraining(id);
ALTER TABLE public.studyontranining ADD CONSTRAINT studyontranining_fk1 FOREIGN KEY (idstudy) REFERENCES study(id);

ALTER TABLE public.studyontranining ADD UNIQUE (idtraining  , idstudy);



