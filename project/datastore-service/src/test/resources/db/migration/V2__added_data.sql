insert into study (telegram_UID, firstName,secondName) values ('55168803','Vladimir','Yelkin');
insert into study (telegram_UID, firstName,secondName) values ('55168805','Имя2','Фамилия2');
insert into study (telegram_UID, firstName,secondName) values ('55112806','Имя3','Фамилия3');

insert into coach (telegram_UID, firstName,secondName) values ('55112804','TrenerИмя111','TrenerФамилия111');
insert into coach (telegram_UID, firstName,secondName) values ('6528770845','Новый','Тренер');

insert into typetraining (id, nameoftraining, duration) values (1, 'ОФП', '01:00:00');
insert into typetraining (id, nameoftraining, duration) values (2, 'Бег 10км.', '01:20:00');

insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (1, '2023-08-01 19:00:00.000', 1, 3, 10);
insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (2, '2023-08-02 20:00:00.000', 2, 5, 10);
insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (1, '2023-08-02 18:00:00.000', 1, 5, 10);
insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (2, '2023-08-03 10:00:00.000', 2, 5, 10);
insert into plannedtraining (idtype, datastart, idcoach, minstudy, maxstudy) values (2, '2023-07-31 12:00:00.000', 1, 5, 10);

insert into studyontranining (idtraining,idstudy) values (1,1);
insert into studyontranining (idtraining,idstudy) values (2,1);