package com.splitwise.expensetracker.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BalanceWithOtherUsers {
    private Map<String, Double> balanceWithOtherUserMap = new HashMap<>();

}
