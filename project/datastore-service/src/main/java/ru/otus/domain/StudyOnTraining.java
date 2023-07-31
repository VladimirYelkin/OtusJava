package ru.otus.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("studyontranining")
@ToString
@Getter
public class StudyOnTraining {

    @Id
    private final Long id;

    @Column("idtraining")
    private final Long idTraining;

    @Column("idstudy")
    private final Long idStudy;

    @PersistenceCreator
    public StudyOnTraining(Long id, Long idTraining, Long idStudy) {
        this.id = id;
        this.idTraining = idTraining;
        this.idStudy = idStudy;
    }

    public StudyOnTraining(Long idTraining, Long idStudy) {
        this(null, idTraining, idStudy);
    }
}
