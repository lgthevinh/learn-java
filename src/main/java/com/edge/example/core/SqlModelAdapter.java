package com.edge.example.core;

import com.edge.example.model.BaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlModelAdapter<T extends BaseModel> {

    private final Class<T> modelClass;

    public SqlModelAdapter(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public T adapt(ResultSet resultSet) throws SQLException {
        try {
            T model = modelClass.getDeclaredConstructor().newInstance();
            for (Field field : modelClass.getDeclaredFields()) {
                DaoField daoField = field.getAnnotation(DaoField.class);
                if (daoField != null) {
                    String columnName = daoField.name();
                    field.setAccessible(true);
                    Object value = resultSet.getObject(columnName);
                    field.set(model, value);
                }
            }
            return model;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new SQLException("Error adapting ResultSet to model", e);
        }
    }

}
