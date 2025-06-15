package com.edge.example;

import com.edge.example.database.DatabaseFactory;
import com.edge.example.database.IDatabase;
import com.edge.example.model.Food;

public class Main {
    public static void main(String[] args) throws Exception {
        IDatabase<Food> database = DatabaseFactory.getDatabase("mongo", Food.class);

        // Insert a new food item

        Food food = new Food();
        food.setFoodId("1");
        food.setName("Pizza");
        food.setPrice(9.99);

        database.insert(food);
        System.out.println("Food inserted: " + food.getName());

        // Read the food item by ID

        Food retrievedFood = database.read("1");
        System.out.println(retrievedFood.getClass().getName());

        // Update the food item

        retrievedFood.setPrice(10.99);
        database.update(retrievedFood);
        System.out.println("Food updated: " + retrievedFood.getName() + ", New Price: " + retrievedFood.getPrice());

        // Query the food item by name
        Food queriedFood = database.query("{name:\"Pizza\"}").get(0);
        if (queriedFood != null) {
            System.out.println("Queried Food: " + queriedFood.getName() + ", Price: " + queriedFood.getPrice());
        } else {
            System.out.println("No food found with the query.");
        }

        // Delete the food item
        database.delete(retrievedFood);
        System.out.println("Food deleted: " + retrievedFood.getName());

        System.out.println("Database operations completed successfully.");
    }
}