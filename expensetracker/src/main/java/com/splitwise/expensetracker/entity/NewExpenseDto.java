package com.splitwise.expensetracker.entity;

import lombok.Data;

import java.util.List;

@Data
public class NewExpenseDto {

    private String spenderId;
    private Double expenseAmount;
    private SplitType splitType;
    private List<UserId> consumersConsumedEqually;
    private List<ExpenseDetailsPercentage> consumersConsumedByPercentage;
    private List<ExpenseDetailsExact> consumersConsumedExactAmt;

}

