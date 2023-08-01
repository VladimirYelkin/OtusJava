package ru.otus.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

import java.time.LocalTime;

@Table("typetraining")
@ToString
@Getter
public class TypeOfTraining {

    @Id
    private final Long id;

    @Column("nameoftraining")
    private final String name;

    @Column("duration")
    private final LocalTime duration;

    @PersistenceCreator
    public TypeOfTraining(Long id,@NonNull String name,@NonNull LocalTime duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public TypeOfTraining(@NonNull String name,@NonNull LocalTime duration) {
        this(null,name, duration);
    }
}
