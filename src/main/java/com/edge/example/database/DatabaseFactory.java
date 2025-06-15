package com.edge.example.database;

import com.edge.example.model.BaseModel;

public class DatabaseFactory {
    public static <T extends BaseModel> IDatabase<T> getDatabase(String type, Class<T> modelClass) throws Exception {
        return switch (type.toLowerCase()) {
            case "mongo" -> new MongoDBDatabase<>(modelClass);

            default -> throw new IllegalArgumentException("Unsupported database type: " + type);
        };
    }
}
