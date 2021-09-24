package com.splitwise.expensetracker.service;

import com.splitwise.expensetracker.entity.NewExpenseDto;
import com.splitwise.expensetracker.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISplitWiseService {

    ResponseEntity<Object> addUser(User user);

    ResponseEntity<List<User>> getAllUsers();

    ResponseEntity<Object> addExpense(NewExpenseDto newExpense);

    ResponseEntity<List<String>> getBalanceByUserId(String userId);

    ResponseEntity<List<String>> getBalancesOfAllUser();

}
