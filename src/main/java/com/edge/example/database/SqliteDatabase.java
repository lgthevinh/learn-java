package com.edge.example.database;

import com.edge.example.core.DaoField;
import com.edge.example.model.BaseModel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class SqliteDatabase<T extends BaseModel> extends AbstractDatabase<T, ResultSet> {

    private Connection connection;

    public SqliteDatabase() {
        try {
            // Initialize SQLite connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insertImpl(ResultSet dbModel) {

    }

    @Override
    protected ResultSet readImpl(String id) {
        return null;
    }

    @Override
    protected void updateImpl(ResultSet dbModel) {

    }

    @Override
    protected void deleteImpl(ResultSet dbModel) {

    }

    @Override
    protected List<ResultSet> queryImpl(String query) {
        return null;
    }

    @Override
    protected T convertToBaseModel(ResultSet dbModel) {
        if (dbModel == null) {
            return null;
        }

        try {
            T model = modelClass.getDeclaredConstructor().newInstance();
            Field[] fields = modelClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                if (field.isAnnotationPresent(DaoField.class)) {
                    DaoField daoField = field.getAnnotation(DaoField.class);
                    fieldName = daoField.name(); // Use the annotated name
                }

                // Assuming dbModel has a method to get value by column name
                Object value = dbModel.getObject(fieldName);
                if (value != null) {
                    field.set(model, value);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected ResultSet convertToDbModel(T model) {
        return null; // SQLite does not use ResultSet for inserts, updates, or deletes
    }
}
