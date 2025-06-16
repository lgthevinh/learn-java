package com.edge.example.database;

import com.edge.example.model.BaseModel;

import java.util.List;

public abstract class AbstractDatabase<T extends BaseModel, M> implements IDatabase<T> {

    protected Class<T> modelClass;

    public void insert(T model) {
        insertImpl(convertToDbModel(model));
    }

    public T read(String id) {
        return convertToBaseModel(readImpl(id));
    }

    public void update(T model) {
        updateImpl(convertToDbModel(model));
    }

    public void delete(T model) {
        deleteImpl(convertToDbModel(model));
    }

    public List<T> query(String query) {
        List<M> dbModel = queryImpl(query);
        if (dbModel == null) {
            return null;
        }
        return dbModel.stream()
                .map(dbModelItem -> {
                    try {
                        return convertToBaseModel(dbModelItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .toList();
    }

    protected abstract void insertImpl(M dbModel);
    protected abstract M readImpl(String id);
    protected abstract void updateImpl(M dbModel);
    protected abstract void deleteImpl(M dbModel);
    protected abstract List<M> queryImpl(String query);

    protected abstract T convertToBaseModel(M dbModel);
    protected abstract M convertToDbModel(T model);
}
