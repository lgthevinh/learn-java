package com.edge.example.model;

import com.edge.example.core.DaoField;
import com.edge.example.core.DaoName;

@DaoName(name = "food")
public class Food extends BaseModel {

    @DaoField(name = "uid")
    private String foodId;

    @DaoField(name = "name")
    private String name;

    @DaoField(name = "price")
    private double price;

    public Food() {

    }

    public Food(String foodId, String name, double price) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
