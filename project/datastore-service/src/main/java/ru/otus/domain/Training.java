package ru.otus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("plannedtraining")
@ToString
@Getter
@AllArgsConstructor
@Builder
public class Training {

    @Column("id")
    @Id
    private final Long id;

    @Column("idtype")
    private final Long idType;

    @Column("idCoach")
    private final Long idCoach;

    @Column("datastart")
    private final LocalDateTime localDateTime;

    @Column("minstudy")
    private final Long minColStudy;

    @Column("maxstudy")
    private final Long maxColStudy;


    @MappedCollection(idColumn = "idtype")
    private final TypeOfTraining typeOfTraining;

    private final Coach coach;

    @PersistenceCreator
    public Training(Long id, Long idType, Long idCoach, LocalDateTime localDateTime, Long minColStudy, Long maxColStudy) {
        this(id, idType, idCoach, localDateTime, minColStudy, maxColStudy, null, null);
    }




//
//    public Training(Long id, Long idType, Long idCoach, LocalDateTime localDateTime, Long minColStudy, Long maxColStudy, TypeOfTraining typeOfTraining, Coach coach) {
//        this.id = id;
//        this.idType = idType;
//        this.idCoach = idCoach;
//        this.localDateTime = localDateTime;
//        this.minColStudy = minColStudy;
//        this.maxColStudy = maxColStudy;
//        this.typeOfTraining = typeOfTraining;
//        this.coach = coach;
//    }
}
