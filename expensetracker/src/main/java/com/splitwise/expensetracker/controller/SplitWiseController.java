package com.splitwise.expensetracker.controller;


import com.splitwise.expensetracker.entity.NewExpenseDto;
import com.splitwise.expensetracker.entity.NewUserDto;
import com.splitwise.expensetracker.entity.User;
import com.splitwise.expensetracker.service.ISplitWiseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
public class SplitWiseController {


    @Autowired
    private ISplitWiseService splitWiseService;

   @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
       String hello = "hello1233";
       return new ResponseEntity<String>(hello, HttpStatus.OK);
   }

    @PostMapping("/initilaizeUserRepository")
    public ResponseEntity<Object> initialiseUserRepository(){
        NewUserDto newUserDto = new NewUserDto("u1", "AJ");
        NewUserDto newUserDto1 = new NewUserDto("u2", "PS");
         addUser(newUserDto);
         addUser(newUserDto1);
        return new ResponseEntity<Object>(getUsers(), HttpStatus.CREATED);
    }


    @PostMapping("/user")
    @Operation(summary = "Add a user, if userId not exist")
    public ResponseEntity<Object> addUser(@RequestBody NewUserDto newUser){
       log.info("new user posted with details {}", newUser);
       User user = new User(newUser);
       log.info("User is going to created {}", user);

        return splitWiseService.addUser(user);
    }

    @GetMapping("/user")
    @Operation(summary = "Get all users list")
    public ResponseEntity<List<User>> getUsers(){
        return splitWiseService.getAllUsers();
    }

    @PostMapping("/user/expense")
    @Operation(summary = "Add expense spent by a user")
    public ResponseEntity<Object> addExpense(@RequestBody NewExpenseDto newExpense) {

       return splitWiseService.addExpense(newExpense);
    }

    @GetMapping("/user/{userId}/balance")
    @Operation(summary = "Get balance of user")
    public ResponseEntity<List<String>> getBalanceByUserId(@PathVariable String userId){
        return splitWiseService.getBalanceByUserId(userId);
    }

    @GetMapping("/user/balance")
    @Operation(summary = "Get balances of all user")
    public ResponseEntity<List<String>> getBalanceOfAllUser(){
        return splitWiseService.getBalancesOfAllUser();
    }


}
