package com.splitwise.expensetracker.service;

import com.splitwise.expensetracker.entity.NewExpenseDto;
import com.splitwise.expensetracker.entity.User;
import com.splitwise.expensetracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SplitWiseService implements ISplitWiseService {

    private static String balanceStringTemplate = "%s owes %s: %f";

    @Override
    public ResponseEntity<Object> addUser(User newUser) {
        List<User> userList = UserRepository.userList;
        boolean userIdExist = UserRepository.userExistByUserId(newUser.getUserId());

        if(userIdExist){
            log.error("User already exist with user id {}", newUser);
            return new ResponseEntity<>("User already exist with user id", HttpStatus.BAD_REQUEST);
        }else{
            /*Adding new user to list*/
             userList.add(newUser);
        }
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {

        return new ResponseEntity<>(UserRepository.userList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addExpense(NewExpenseDto newExpense) {
        newExpense.getSplitType().addExpense(newExpense);
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getBalanceByUserId(String userId) {

        List<String> balances = getBalance(userId);
        if(balances.isEmpty()){
            balances.add("No balances");
        }
        return new ResponseEntity<>(balances,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<List<String>> getBalancesOfAllUser() {
        List<String> balances = new ArrayList<>();
        for(User user: UserRepository.userList){
            List<String> balanceOfUser = getBalance(user.getUserId());
            balances.addAll(balanceOfUser);
        }
        if(balances.isEmpty()){
            balances.add("No balances");
        }
        return new ResponseEntity<>(balances,HttpStatus.OK);
    }

    private List<String> getBalance(String userId) {
        List<String> balances = new ArrayList<>();
        User user = UserRepository.findUserByUserId(userId);
        for (Map.Entry<String, Double> balance : user.getBalanceMapWithOtherUsers().entrySet()) {
            log.info(balance.getKey() + "/" + balance.getValue());
            if (balance.getValue() < 0) {
                String balanceDetail = String.format(balanceStringTemplate, userId, balance.getKey(), balance.getValue());
                balances.add(balanceDetail);
            }
        }
        return balances;
    }
}
