create table message
(
    id   bigserial    not null primary key,
    room_id  varchar(50)  not null,
    msg_text varchar(50) not null
);

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

create index idx_message_room_id on message (room_id);

--insert into message (room_id,msg_text) values ('55168803','SEDEREVET');
insert into study (telegram_UID, firstName,secondName) values ('55168803','Vladimir','Yelkin');
insert into study (telegram_UID, firstName,secondName) values ('55168804','Имя2','Фамилия2');
insert into study (telegram_UID, firstName,secondName) values ('55112806','Имя3','Фамилия3');
insert into coach (telegram_UID, firstName,secondName) values ('55112804','TrenerИмя111','TrenerФамилия111');
insert into typetraining (id, nameoftraining, duration) values (1, 'typeoftrining1', '01:15:00');
insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (1, '2023-08-01 19:00:00.000', 1, 3, 10);
insert into studyontranining (idtraining,idstudy) values (1,1);


