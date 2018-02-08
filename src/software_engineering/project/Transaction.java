/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_engineering.project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ajay
 */
public class Transaction {
    
    private int mID;
    private Date mDate;
    private long mAmount;
    private int mType;
    
    public Transaction(){}
    
    public Transaction(Date date,long amt,int type){
        this.mDate = date;
        this.mAmount = amt;
        this.mType = type;
    }
    
    public Transaction getReceipt(){
        return this;
    }
    
    public Date getDate(){
        return this.mDate;
    }
    
    public long getAmount(){
        return this.mAmount;
    }
    
    public int getType(){
        return this.mType;
    }
    
    public int getID(){
        return mID;
    }
    
    public void setID(int id){
        this.mID = id;
    }
    
    public Transaction getTransaction(long id){
        
        Transaction trans = new Transaction();
        Connection con = null;
        
        try {  
            con = DriverManager.getConnection(  
                    "jdbc:mysql://localhost:3306/virtualatm","root","password");
            Statement stmt = con.createStatement();  
            ResultSet rs = stmt.executeQuery("select * from Transaction where ID = "+id);
            if(rs.next()){
                trans.mDate = rs.getDate(1);
                trans.mAmount = rs.getLong(2);
                trans.mType = rs.getInt(3);
            }else{
                trans = null;
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(VirtualATM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return trans;
    }
}
