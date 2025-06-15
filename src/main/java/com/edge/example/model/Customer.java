package com.edge.example.model;

import com.edge.example.core.DaoField;
import com.edge.example.core.DaoName;

@DaoName(name = "customer")
public class Customer extends BaseModel {

    @DaoField(name = "uid")
    private String customerId;

    @DaoField(name = "name")
    private String name;

    @DaoField(name = "email")
    private String email;

    @DaoField(name = "phone_number")
    private String phoneNumber;

    public Customer() {

    }

    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
