/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_engineering.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ajay
 */
public class UserDao {
    
    /**
     * Get a user record for the database
     * @param cardPin
     * @return the User object
     * @throws SQLException 
     */
    public static User getUser(int cardPin) throws SQLException{
        User user = new User();
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "select * from UserBankAccount where user_pin = "
                        +cardPin);
        if(rs.next()){
            user.setAccountNumber(rs.getLong(1));
            user.setCardPin(rs.getInt(2));
            user.setBalance(rs.getLong(3));
            user.setCardNumber(rs.getLong(4));
            user.setName(rs.getString(5));
            user.setDOB(rs.getDate(6));
            user.setMonthlyTransactions(rs.getInt(7));
        }
            
        conn.close();
        return user;
    }
    
    /**
     * Withdraw money from database
     * @param accountNumber
     * @param amt
     * @param type
     * @throws SQLException 
     */
    public static void updateBalanceWithdrawal(long accountNumber,long amt,String type) 
            throws SQLException{
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement statement = conn.createStatement();
        String updateQuery = "";
        if(type.equals("Current")){
            updateQuery = "update UserBankAccount set "
                        + "balance = balance - "+amt
                        + "  where account_number = "+accountNumber;
        }else if(type.equals("Savings")){
            updateQuery = "update UserBankAccount set "
                        + "monthlyTransactions = monthlyTransactions + 1,"
                        + "balance = balance - "+amt
                        + "  where account_number = "+accountNumber;
        }
        statement.executeUpdate(updateQuery);
        conn.close();
    }
    
    /**
     * Deposit money to database
     * @param accountNumber
     * @param amt
     * @param type
     * @throws SQLException 
     */
    public static void updateBalanceDeposit(long accountNumber,long amt,String type) 
            throws SQLException{
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement statement = conn.createStatement();
        String updateQuery = "";
        if(type.equals("Current")){
            updateQuery = "update UserBankAccount set "
                        + "balance = balance + "+amt
                        + "  where account_number = "+accountNumber;
        }else if(type.equals("Savings")){
            updateQuery = "update UserBankAccount set "
                        + "monthlyTransactions = monthlyTransactions + 1,"
                        + "balance = balance + "+amt
                        + "  where account_number = "+accountNumber;
        }
        statement.executeUpdate(updateQuery);
        conn.close();
    }
    
    /**
     * Update the user PIN
     * @param accountNumber
     * @param newPin
     * @throws SQLException 
     */
    public static void updateUserPin(long accountNumber,int newPin) throws SQLException{
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("update UserBankAccount set"
                        +" user_pin = "+newPin
                        +" where account_number = "+accountNumber);
        conn.close();
    }
    
    /**
     * Validate the admin id
     * @param userId
     * @return
     * @throws SQLException 
     */
    public static boolean validateAdminID(String userId) throws SQLException{
        boolean result = false;
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from AdminBankAccount"
                        +" where user_id = "+userId);
        if(rs.next())
            result = rs.getMetaData().getColumnCount() > 0;
            
        conn.close();
        return result;
    }
    
    /**
     * Validate admin password
     * @param password
     * @return
     * @throws SQLException 
     */
    public static boolean validateAdminPassword(String password) throws SQLException {
        boolean result = false;
        
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from AdminBankAccount"
                        +" where password = "+password);
        if(rs.next())
            result = rs.getMetaData().getColumnCount() > 0;
            
        conn.close();
        
        return result;
    }
}