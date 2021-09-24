package com.splitwise.expensetracker.entity;

import lombok.Data;

@Data
public class ExpenseDetailsExact {

    private String userId;
    private double exactAmount;
}
