package com.splitwise.expensetracker.entity;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class User {

    private String userId;
    private String userName;
    private double consumedAmount;
    private Map<String, Double> balanceMapWithOtherUsers;


    public User(NewUserDto newUser) {
        this.userId = newUser.getUserId();
        this.userName = newUser.getUserName();
        this.balanceMapWithOtherUsers = new HashMap<>();

    }
}
