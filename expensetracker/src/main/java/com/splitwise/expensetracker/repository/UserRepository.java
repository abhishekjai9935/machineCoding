package com.splitwise.expensetracker.repository;

import com.splitwise.expensetracker.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
@Slf4j
public class UserRepository {

    public static List<User> userList = new ArrayList<>();

    public static User findUserByUserId(String userId){
        for(User user: userList){
            if(user.getUserId().equals(userId)){
                return user;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not find by userId");
    }

    public static Boolean userExistByUserId(String userId){
        for(User user: userList){
            log.info("user in for loop {}", user);
            if(user.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }
}
