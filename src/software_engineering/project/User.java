/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_engineering.project;

import java.sql.Date;

/**
 *
 * @author ajay
 */
public class User {
 
    private String mName;
    private long mAccountNumber;
    private int mCardPin;
    private long mBalance;
    private Date mDOB;
    private int mMonthlyTransactions;
    private long mCardNumber;
    
    public void setBalance(long balance){
        mBalance = balance;
    }
    
    public long getBalance(){
        return mBalance;
    }
    
    public void setDOB(Date dob){
        mDOB = dob;
    }
    
    public Date getDOB(){
        return mDOB;
    }
    
    public void setName(String name){
        mName = name;
    }
    
    public String getName(){
        return mName;
    }
    
    public void setCardPin(int pin){
        mCardPin = pin;
    }
    
    public int getCardPin(){
        return mCardPin;
    }
    
    public void setCardNumber(long number){
        mCardNumber = number;
    }
    
    public long getCardNumber(){
        return mCardNumber;
    }
    
    public void setMonthlyTransactions(int trans){
        mMonthlyTransactions = trans;
    }
    
    public int getMonthlyTransactions(){
        return mMonthlyTransactions;
    }
    
    public void setAccountNumber(long number){
        mAccountNumber = number;
    }
    
    public long getAccountNumber(){
        return mAccountNumber;
    }
}