package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String simpleName;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(T investigateObject, Class<? extends Annotation> idAnnotation) {
        this((Class<T>) investigateObject.getClass(), idAnnotation);
    }

    public EntityClassMetaDataImpl(Class<T> clazz, Class<? extends Annotation> idAnnotation) {
        simpleName = clazz.getSimpleName();
        allFields = Arrays.stream(clazz.getDeclaredFields()).toList();
        idField = allFields.stream()
                .filter(field -> field.isAnnotationPresent(idAnnotation))
                .findFirst().orElseThrow(() -> new RuntimeException("isn't fond annotation" + idAnnotation.getSimpleName()));
        fieldsWithoutId = allFields.stream().filter(field -> !(field.isAnnotationPresent(idAnnotation))).toList();
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public String getName() {
        return simpleName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
