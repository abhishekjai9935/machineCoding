package com.splitwise.expensetracker.entity;

import com.splitwise.expensetracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

@Slf4j
public enum SplitType {

    EXACT{
        @Override
        public ResponseEntity<Object> addExpense(NewExpenseDto newExpense) {

            User spender = UserRepository.findUserByUserId(newExpense.getSpenderId());

            for(ExpenseDetailsExact expenseDetailsExact: newExpense.getConsumersConsumedExactAmt()){

                User consumer = UserRepository.findUserByUserId(expenseDetailsExact.getUserId());

                Double consumedAmount = expenseDetailsExact.getExactAmount();
                consumer.setConsumedAmount(consumer.getConsumedAmount()+consumedAmount);

                if(!newExpense.getSpenderId().equals(expenseDetailsExact.getUserId())){
                    updateBalanceMap(spender,consumer,consumedAmount);
                }
            }
            return new ResponseEntity<>("Expense added successfully", HttpStatus.CREATED);
        }
    },
    PERCENTAGE {
        @Override
        public ResponseEntity<Object> addExpense(NewExpenseDto newExpense) {

            User spender = UserRepository.findUserByUserId(newExpense.getSpenderId());

            for(ExpenseDetailsPercentage expenseDetailsPercentage: newExpense.getConsumersConsumedByPercentage()){

                User consumer = UserRepository.findUserByUserId(expenseDetailsPercentage.getUserId());

                Double consumedAmount = newExpense.getExpenseAmount()*expenseDetailsPercentage.getPercentage()/100;
                consumer.setConsumedAmount(consumer.getConsumedAmount()+consumedAmount);

                if(!newExpense.getSpenderId().equals(expenseDetailsPercentage.getUserId())){
                    updateBalanceMap(spender,consumer,consumedAmount);
                }
            }
            return new ResponseEntity<>("Expense added successfully", HttpStatus.CREATED);
        }
    },
    EQUAL {
        @Override
        public ResponseEntity<Object> addExpense(NewExpenseDto newExpense) {
            User spenderUser = UserRepository.findUserByUserId(newExpense.getSpenderId());

            Integer numberOfUsersInvolvedInExpense = newExpense.getConsumersConsumedEqually().size();

            Double expenseSpentOnEachUser = newExpense.getExpenseAmount()/numberOfUsersInvolvedInExpense;

            for(UserId userId: newExpense.getConsumersConsumedEqually()){

                User userInvolvedInExpense = UserRepository.findUserByUserId(userId.getUserId());
                userInvolvedInExpense.setConsumedAmount(userInvolvedInExpense.getConsumedAmount()+expenseSpentOnEachUser);

                if(!newExpense.getSpenderId().equals(userId.getUserId())){

                    updateBalanceMap(spenderUser,userInvolvedInExpense,expenseSpentOnEachUser);
                }
            }
            return new ResponseEntity<>("Expense added successfully", HttpStatus.CREATED);
        }
    };

    private static void updateBalanceMap(User spenderUser, User userInvolvedInExpense, Double expenseSpentOnUser){

        String spenderUserId = spenderUser.getUserId();
        String userIdSpentOn = userInvolvedInExpense.getUserId();

        //Update the balancemap of user on whom expense is spent
        Double currentBalanceWithSpender = userInvolvedInExpense.getBalanceMapWithOtherUsers().get(spenderUserId);
        if(Objects.isNull(currentBalanceWithSpender)){
            currentBalanceWithSpender = 0.0;
        }
        Map<String,Double> balanceMapOfUser = userInvolvedInExpense.getBalanceMapWithOtherUsers();
        balanceMapOfUser.put(spenderUserId,currentBalanceWithSpender - expenseSpentOnUser);

        //update the balancemap of spender
        Double currentBalanceWithUserOnWhomExpenseIsSpent = spenderUser.getBalanceMapWithOtherUsers().get(userIdSpentOn);
        if(Objects.isNull(currentBalanceWithUserOnWhomExpenseIsSpent)){
            currentBalanceWithUserOnWhomExpenseIsSpent = 0.0;
        }
        Map<String,Double> balanceMapOfSpender = spenderUser.getBalanceMapWithOtherUsers();
        balanceMapOfSpender.put(userIdSpentOn,currentBalanceWithUserOnWhomExpenseIsSpent + expenseSpentOnUser);
    };

    public abstract ResponseEntity<Object> addExpense(NewExpenseDto newExpense) ;
}
