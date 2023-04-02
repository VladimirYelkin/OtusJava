package ru.otus.jdbc.mapper;

import ru.otus.annotations.ClassMetaData;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String SELECT_ALL_FROM = "select %s from %s";
    private static final String WHERE = " where %s = ?";
    private final String allFieldsSelect;
    @ClassMetaData
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        allFieldsSelect = this.entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public String getSelectAllSql() {
        return String.format(SELECT_ALL_FROM, allFieldsSelect, entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(SELECT_ALL_FROM + WHERE, allFieldsSelect,
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        return String.format("insert into %s(%s) values(%s)",
                entityClassMetaData.getName(),
                fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(",")),
                String.join(",", Collections.nCopies(fields.size(), "?")));
    }

    @Override
    public String getUpdateSql() {
        String updateFields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> String.format("%s = ?", field.getName()))
                .collect(Collectors.joining(","));
        String whereStr = String.format(WHERE, entityClassMetaData.getIdField().getName());

        return String.format("update %s set %s",
                entityClassMetaData.getName(),
                updateFields + whereStr);
    }

}
