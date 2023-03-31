package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.ClassMetaData;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    // конструктор на тот случай, если нельзя изменить код вызова в HomeWork : new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager);
    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        try {
            var fieldEntityClassMetaDataAnnotation = Arrays.stream(entitySQLMetaData.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ClassMetaData.class))
                    .findFirst()
                    .map(field -> {
                        field.setAccessible(true);
                        return field;
                    })
                    .orElseThrow(() -> new RuntimeException("not find annotation" + ClassMetaData.class.getSimpleName()));
            this.entityClassMetaData = (EntityClassMetaData<T>) fieldEntityClassMetaDataAnnotation.get(this.entitySQLMetaData);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }


    @Override
    public Optional<T> findById(Connection connection, long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAll(Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(Connection connection, T obj) {

        String insertSql = entitySQLMetaData.getInsertSql();
        try {
            List<Object> params = getValuesFields(obj, entityClassMetaData.getFieldsWithoutId());
            return dbExecutor.executeStatement(connection,
                    insertSql,
                    params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        throw new UnsupportedOperationException();
    }

    private List<Object> getValuesFields(T obj, List<Field> fields) throws IllegalAccessException {
        List<Object> valuesOfFields = new ArrayList<>();
        fields.stream().forEach(consumerWrapper(field -> {
            field.setAccessible(true);
            valuesOfFields.add(field.get(obj));
        }));
        return valuesOfFields;
    }


    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {

        void accept(T t) throws E;
    }

    private <T> Consumer<T> consumerWrapper(
            ThrowingConsumer<T, Exception> throwingConsumer) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new DataTemplateException(ex);
            }
        };
    }
}
