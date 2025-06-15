package com.edge.example.database;

import com.edge.example.model.BaseModel;

import java.util.List;

public interface IDatabase<T extends BaseModel> {
    void insert(T model);
    T read(String id);
    void update(T model);
    void delete(T model);

    List<T> query(String query);
}
