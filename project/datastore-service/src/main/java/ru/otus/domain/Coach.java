package ru.otus.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table("coach")
public class Coach {
    private static final Logger log = LoggerFactory.getLogger(Coach.class);

    @Id
    private final Long id;

    @NonNull
    @Column("telegram_uid")
    private final String telegramUID;

    @NonNull
    @Column("firstname")
    private final String firstName;

    @NonNull
    @Column("secondname")
    private final String secondName;

    @PersistenceCreator
    public Coach(Long id, @NonNull String telegramUID, @NonNull String firstName, @NonNull String secondName) {
        this.id = id;
        this.telegramUID = telegramUID;
        this.firstName = firstName;
        this.secondName = secondName;
        log.info("create coach from full constructor: {}",this.toString());
    }

    public Coach(@NonNull String telegramUID, @NonNull String firstName, @NonNull String secondName) {
        this(null, telegramUID, firstName, secondName);
        log.info("create coach from constructor w|o id: {}",this.toString());

    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getTelegramUID() {
        return telegramUID;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getSecondName() { return secondName; }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", telegramUID='" + telegramUID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
