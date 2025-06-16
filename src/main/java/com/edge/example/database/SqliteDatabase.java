package com.edge.example.database;

import com.edge.example.core.DaoName;
import com.edge.example.model.SqlModelAdapter;
import com.edge.example.model.BaseModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class SqliteDatabase<T extends BaseModel> extends AbstractDatabase<T, SqlModelAdapter<T>> {

    private Connection connection;
    private final Class<T> modelClass;

    public SqliteDatabase(Class<T> modelClass) {
        this.modelClass = modelClass;
        try {
            // Initialize SQLite connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insertImpl(SqlModelAdapter<T> dbModel) {
        String query = "INSERT INTO " + modelClass.getAnnotation(DaoName.class).name() + " (";
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (String key : dbModel.getFields().keySet()) {
            columns.append(key).append(", ");
            placeholders.append("?, ");
        }
        query += columns.substring(0, columns.length() - 2) + ") VALUES (" + placeholders.substring(0, placeholders.length() - 2) + ")";

        try (var preparedStatement = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : dbModel.getFields().values()) {
                preparedStatement.setObject(index++, value);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SqlModelAdapter<T> readImpl(String uid) {
        String query = "SELECT * FROM " + modelClass.getAnnotation(DaoName.class).name() + " WHERE uid = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);
            ResultSet result = preparedStatement.executeQuery();

            SqlModelAdapter<T> dbModel = new SqlModelAdapter<>(modelClass);
            dbModel.fromResultSet(result);
            return dbModel;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void updateImpl(SqlModelAdapter<T> dbModel) {
        String query = "UPDATE " + modelClass.getAnnotation(DaoName.class).name() + " SET ";
        StringBuilder setClause = new StringBuilder();
        for (String key : dbModel.getFields().keySet()) {
            setClause.append(key).append(" = ?, ");
        }
        query += setClause.substring(0, setClause.length() - 2) + " WHERE uid = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : dbModel.getFields().values()) {
                preparedStatement.setObject(index++, value);
            }
            preparedStatement.setString(index, dbModel.getFields().get("uid").toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void deleteImpl(SqlModelAdapter<T> dbModel) {
        String query = "DELETE FROM " + modelClass.getAnnotation(DaoName.class).name() + " WHERE uid = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dbModel.getFields().get("uid").toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<SqlModelAdapter<T>> queryImpl(String query) {
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(query)) {
            List<SqlModelAdapter<T>> results = new java.util.ArrayList<>();
            while (resultSet.next()) {
                SqlModelAdapter<T> dbModel = new SqlModelAdapter<>(modelClass);
                dbModel.fromResultSet(resultSet);
                results.add(dbModel);
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @Override
    protected T convertToBaseModel(SqlModelAdapter<T> dbModel) {
        return dbModel.toTModel();
    }

    @Override
    protected SqlModelAdapter<T> convertToDbModel(T model) {
        SqlModelAdapter<T> dbModel = new SqlModelAdapter<>(modelClass);
        dbModel.fromTModel(model);
        return dbModel;
    }
}
