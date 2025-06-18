package com.edge.example.database;

import com.edge.example.core.DaoName;
import com.edge.example.core.ProtectedVar;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.edge.example.core.DaoField;
import com.edge.example.model.BaseModel;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MongoDBDatabase<T extends BaseModel> extends AbstractDatabase<T, Document> {

    private final MongoCollection<Document> collection;

    public MongoDBDatabase(Class<T> modelClass) throws Exception {
        String connectionString = ProtectedVar.MONGO_URL;

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("learn_java");

        String collectionName = modelClass.getAnnotation(DaoName.class).name();
        this.collection = mongoDatabase.getCollection(collectionName);

        this.modelClass = modelClass;
    }

    @Override
    protected void insertImpl(Document dbModel) {
        collection.insertOne(dbModel);
    }

    @Override
    protected Document readImpl(String id) {
        Document query = new Document("uid", id);
        FindIterable<Document> result = collection
                .find(query)
                .limit(1);

        return result.first(); // Return the first document found, or null if none found
    }

    @Override
    protected void updateImpl(Document dbModel) {
        String id = dbModel.getString("uid");
        Document query = new Document("uid", id);
        collection.replaceOne(query, dbModel);
    }

    @Override
    protected void deleteImpl(Document dbModel) {
        String id = dbModel.getString("uid");
        Document query = new Document("uid", id);
        collection.deleteOne(query);
    }

    @Override
    protected List<Document> queryImpl(String query) {
        Document queryDocument = Document.parse(query);
        FindIterable<Document> result = collection
                .find(queryDocument);
        return result.into(new java.util.ArrayList<>()); // Convert the result to a List
    }

    @Override
    protected T convertToBaseModel(Document dbModel) {

        if (dbModel == null) {
            return null; // Handle null case
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

                if (dbModel.containsKey(fieldName)) {
                    Object value = dbModel.get(fieldName);
                    field.set(model, value);
                }
            }
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Error converting Document to BaseModel", e);
        }
    }

    @Override
    protected Document convertToDbModel(T model) {

        if (model == null) {
            return null; // Handle null case
        }

        Document document = new Document();
        for (Map.Entry<String, Object> entry : model.toMap().entrySet()) {

            if (entry.getValue() == null) {
                continue; // Skip null values
            }

            if (entry.getValue() instanceof String) {
                document.append(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                document.append(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                document.append(entry.getKey(), (Double) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                document.append(entry.getKey(), (Boolean) entry.getValue());
            } else {
                // Handle other types as needed
                document.append(entry.getKey(), entry.getValue().toString());
            }
        }
        return document;
    }
}
