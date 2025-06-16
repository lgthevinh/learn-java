package com.edge.example.model;

import com.edge.example.core.DaoField;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SqlModelAdapter<T extends BaseModel> {

    private Map<String, Object> fields;
    private Class<T> modelClass;

    public SqlModelAdapter(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public void fromResultSet(ResultSet resultSet) {

        if (resultSet == null) {
            throw new IllegalArgumentException("ResultSet cannot be null");
        }

        try {
            this.fields = new java.util.HashMap<>();
            Field[] fields = modelClass.getDeclaredFields();

            for (Field field : fields) {
                String key;
                if (field.isAnnotationPresent(DaoField.class)) {
                    DaoField daoField = field.getAnnotation(DaoField.class);
                    key = daoField.name();
                } else {
                    key = field.getName();
                }

                Object value = resultSet.getObject(key);
                if (value != null) {
                    this.fields.put(key, value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fromTModel(T model) {
        this.fields = model.toMap();
    }

    public T toTModel() {

        try {
            T model = modelClass.getDeclaredConstructor().newInstance();
            Field[] fields = modelClass.getDeclaredFields();

            for (Field field : fields) {
                String key;
                if (field.isAnnotationPresent(DaoField.class)) {
                    DaoField daoField = field.getAnnotation(DaoField.class);
                    key = daoField.name();
                } else {
                    key = field.getName();
                }

                if (this.fields.containsKey(key)) {
                    field.setAccessible(true);
                    field.set(model, this.fields.get(key));
                }
            }
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Error converting SqlModelAdapter to BaseModel", e);
        }


    }

    public Map<String, Object> getFields() {
        return fields;
    }

}
