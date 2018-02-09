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
public class UtilityDao {
    
    /**
     * Verify the user's utility account
     * @param utilityNumber
     * @return
     * @throws SQLException 
     */
    public static boolean verifyUser(long utilityNumber) throws SQLException{
        boolean result = false;
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select * from Utility where utility_number = "+utilityNumber);
        if(rs.next())
            result = rs.getMetaData().getColumnCount() > 0;
        
        return result;
    }
    
    /**
     * Pay the bill
     * @param utilityNumber
     * @param due
     * @throws SQLException 
     */
    public static int payBill(long utilityNumber,int due) throws SQLException{
        int result = 0;
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/virtualatm","root","password");
        Statement stmt = conn.createStatement();
        result = stmt.executeUpdate(
                "update Utility set amount_due = amount_due - "
                        +due+" where utility_number = "+utilityNumber);
        return result;
    }
}
