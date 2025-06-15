package com.edge.example.model;

import com.edge.example.core.DaoField;
import com.edge.example.core.DaoName;

import java.util.HashMap;

@DaoName(name = "order")
public class Order extends BaseModel {

    @DaoField(name = "uid")
    private String orderId;

    @DaoField(name = "customer_id")
    private String customerId;

    @DaoField(name = "food_items")
    private final HashMap<String, Integer> foodItems = new HashMap<>();

    public Order() {

    }

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public HashMap<String, Integer> getFoodItems() {
        return foodItems;
    }

    public void addFoodItem(String foodId, int quantity) {
        foodItems.put(foodId, quantity);
    }

    public void removeFoodItem(String foodId) {
        foodItems.remove(foodId);
    }
}
