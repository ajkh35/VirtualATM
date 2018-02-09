/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_engineering.project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author ajay
 */
public class VirtualATM extends javax.swing.JFrame{

    private ActionListener OkListener;
    private User mUser;
    
    /**
     * Creates new form VirtualATM
     */
    public VirtualATM() {
        initComponents();
        
        // load up initial elements
        createStartScreen();
        
        // setup card swipe
        setupUserLogin();
        
        // setup admin login
        setupAdminLogin();
        
        // Cancel press listener
        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome.setVisible(true);
                jLabel3.setVisible(false);
                jTextField1.setVisible(false);
                jLabel1.setVisible(false);
                jLabel2.setVisible(false);
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                jTextField1.setText("");
                jButton1.removeAll();
                jButton2.removeAll();
                jButton4.removeAll();
                jButton5.removeAll();
                jButton6.removeAll();
                jButton7.removeAll();
                ok.removeAll();
            }
        });
        
    }

    // setup the users card login
    private void setupUserLogin(){
        
        // card insert listener
        insertCard.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome.setVisible(false);
                jLabel3.setVisible(true);
                jTextField1.setVisible(true);
                jLabel3.setText("Enter PIN");
                jTextField1.setText("");
            }
        });
        
        // initialize keypad
        setKeypadListeners();
        
        // Ok press listener
        ok.removeActionListener(OkListener);
        OkListener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                
                if(!str.equals("")){
                    if(verifyPin(str)){
                    System.out.println("great");
                    loadMainMenu();
                    }else{
                        System.out.println("not so great");
                        JOptionPane.showMessageDialog(null, "Invalid PIN");
                        cancel.doClick();
                    }
                }else
                    JOptionPane.showMessageDialog(null,"Field can't be blank");
            }
        };
        ok.addActionListener(OkListener);
    }
    
    private void setupAdminLogin(){
        
        Admin.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel1.setVisible(false);
                jLabel2.setVisible(false);
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                welcome.setVisible(false);
                jLabel3.setVisible(true);
                jTextField1.setVisible(true);
                jLabel3.setText("Used Id: ");
                jTextField1.setText("");
                
                ok.removeActionListener(OkListener);
                OkListener = new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String str = jTextField1.getText();
                        if(!str.equals("")){
                            try {
                                if(UserDao.validateAdminID(str)){
                                    jTextField1.setText("");
                                    jLabel3.setText("Password: ");
                                    
                                    ok.removeActionListener(OkListener);
                                    OkListener = new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String str = jTextField1.getText();
                                            if(!str.equals("")){
                                                try {
                                                    if(UserDao.validateAdminPassword(str)){
                                                        loadAdminMenu();
                                                    }else
                                                        JOptionPane.showMessageDialog(null
                                                                ,"Wrong password");
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(VirtualATM.class.getName())
                                                            .log(Level.SEVERE, null, ex);
                                                }
                                            }else
                                                JOptionPane.showMessageDialog(null
                                                        ,"Field can't be blank");
                                        }
                                    };
                                    ok.addActionListener(OkListener);
                                }else
                                    JOptionPane.showMessageDialog(null,"Wrong user id");
                            } catch (SQLException ex) {
                                Logger.getLogger(VirtualATM.class.getName())
                                        .log(Level.SEVERE, null, ex);
                            }
                        }else
                            JOptionPane.showMessageDialog(null,"Field can't be blank");
                    }
                };
                ok.addActionListener(OkListener);
            }
        });
    }
    
    // Verification of ATM PIN
    private boolean verifyPin(String str){
        boolean result = false;
        
        Connection con = null;
        try {  
            con = DriverManager.getConnection(  
                    "jdbc:mysql://localhost:3306/virtualatm","root","password");
            Statement stmt = con.createStatement();  
            ResultSet rs = stmt
                    .executeQuery("select * from UserBankAccount where user_pin = "+str);  
            if(rs.next()){
                result = rs.getMetaData().getColumnCount() > 0;
                mUser = UserDao.getUser(Integer.parseInt(str));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(VirtualATM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    // create default screen
    public void createStartScreen(){
        jPanel1.setBackground(Color.white);
        jPanel2.setBackground(Color.gray);
        jPanel3.setBackground(Color.gray);
        jLabel1.setText("");
        jLabel2.setText("");
        jLabel4.setText("");
        jLabel5.setText("");
        jLabel6.setText("");
        jLabel7.setText("");
        jButton1.setText("");
        jButton1.setActionCommand("withdraw");
        jButton2.setText("");
        jButton2.setActionCommand("deposit");
        jButton4.setText("");
        jButton4.setActionCommand("paybill");
        jButton5.setText("");
        jButton5.setActionCommand("accountbalance");
        jButton6.setText("");
        jButton6.setActionCommand("changepin");
        jButton7.setText("");
        jButton7.setActionCommand("fastcash");
        cancel.setText("Cancel");
        cancel.setForeground(Color.RED);
        cancel.setBackground(Color.WHITE);
        ok.setText("Ok");
        ok.setForeground(Color.GREEN);
        ok.setBackground(Color.WHITE);
        insertCard.setText("Insert Card");
        insertCard.setBackground(Color.WHITE);
        Admin.setText("Admin");
        Admin.setBackground(Color.WHITE);
        
        // keypad
        one.setText("1");
        one.setBackground(Color.white);
        two.setText("2");
        two.setBackground(Color.white);
        three.setText("3");
        three.setBackground(Color.white);
        four.setText("4");
        four.setBackground(Color.white);
        five.setText("5");
        five.setBackground(Color.white);
        six.setText("6");
        six.setBackground(Color.white);
        seven.setText("7");
        seven.setBackground(Color.white);
        eight.setText("8");
        eight.setBackground(Color.white);
        nine.setText("9");
        nine.setBackground(Color.white);
        zero.setText("0");
        zero.setBackground(Color.white);
        welcome.setText("Welcome to VirtualATM");
        jLabel3.setVisible(false);
        jTextField1.setVisible(false);
    }
    
    // setup the keypad
    private void setKeypadListeners(){
        one.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "1";
                    jTextField1.setText(str);
                } else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        two.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "2";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        three.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "3";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        four.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "4";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        five.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "5";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        six.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "6";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        seven.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "7";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        eight.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "8";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        nine.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "9";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
        zero.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jTextField1.getText();
                if(str.length()<4){
                    str += "0";
                    jTextField1.setText(str);
                }else
                    JOptionPane.showMessageDialog(null, "Only four digits allowed.");
            }
        });
    }
    
    // load up the main menu
    private void loadMainMenu(){
        
        jTextField1.setVisible(false);
        jLabel3.setVisible(false);
        
        jLabel1.setText("Withdraw");
        jLabel2.setText("Deposit");
        jLabel4.setText("Bill Payment");
        jLabel5.setText("Account Balance");
        jLabel6.setText("Change PIN");
        jLabel7.setText("FastCash");
        
        // User withdraws money
        setupWithdraw();
        
        // User deposits money
        setupDeposit();
        
        // User pays the bill
        setupBillPayment();
        
        //User checks balance
        setupBalanceCheck();
        
        //User changes PIN
        setupChangePin();
        
        // User selects FastCash
        setupFastcash();
    }
    
    // load up the admin menu
    private void loadAdminMenu(){
        
        jLabel3.setVisible(false);
        jTextField1.setVisible(false);
        jLabel1.setVisible(true);
        jLabel2.setVisible(true);
        jLabel1.setText("Recash");
        jLabel2.setText("Change capacity");
    }
    
    // user withdraws money
    private void setupWithdraw(){
        
        jButton1.removeAll();
        jButton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                jLabel4.setText("Current");
                jLabel5.setText("Savings");
                jLabel1.setVisible(false);
                jLabel2.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                
                jButton4.removeAll();
                jButton4.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jLabel4.setVisible(false);
                        jLabel5.setVisible(false);
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Enter the amount:");
                        ok.removeActionListener(OkListener);
                        
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    long amt = Long.parseLong(jTextField1.getText());
                                    Date date = new Date(new java.util.Date().getTime());
                                    int type = 1;
                                    Transaction trans = new Transaction(date,amt,type);
//                                    trans.setID(saveTransaction(trans));
                                    JOptionPane.showMessageDialog(null,
                                            "Amount: "+trans.getAmount()
                                            ,String.valueOf(trans.getDate())
                                            ,JOptionPane.INFORMATION_MESSAGE);
                                    try {
                                        UserDao.updateBalanceWithdrawal(mUser.getAccountNumber()
                                                ,amt,"Current");
                                    } catch (SQLException ex) {
                                        Logger.getLogger(VirtualATM.class.getName())
                                                .log(Level.SEVERE, null, ex);
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"Please enter an amount");
                                }
                                cancel.doClick();
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });
                
                jButton5.removeAll();
                jButton5.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jLabel4.setVisible(false);
                        jLabel5.setVisible(false);
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Enter the amount:");
                        ok.removeActionListener(OkListener);
                        
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    if(mUser.getMonthlyTransactions() >= 6){
                                        JOptionPane.showMessageDialog(null,
                                                "Monthly Transactions depleted");
                                    }else{
                                        long amt = Long.parseLong(jTextField1.getText());
                                        Date date = new Date(new java.util.Date().getTime());
                                        int type = 1;
                                        Transaction trans = new Transaction(date,amt,type);
    //                                    trans.setID(saveTransaction(trans));
                                        JOptionPane.showMessageDialog(null,
                                                "Amount: "+trans.getAmount()
                                                ,String.valueOf(trans.getDate())
                                                ,JOptionPane.INFORMATION_MESSAGE);
                                        try {
                                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber()
                                                    ,amt,"Savings");
                                        } catch (SQLException ex) {
                                            Logger.getLogger(VirtualATM.class.getName())
                                                    .log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"Please enter an amount");
                                }
                                cancel.doClick();
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });
            }
        });
    }
    
    // user deposits money
    private void setupDeposit(){
        
        jButton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel4.setText("Current");
                jLabel5.setText("Savings");
                jLabel1.setVisible(false);
                jLabel2.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                
                jButton4.removeAll();
                jButton4.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jLabel4.setVisible(false);
                        jLabel5.setVisible(false);
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Enter the amount:");
                        ok.removeActionListener(OkListener);
                        
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    long amt = Long.parseLong(jTextField1.getText());
                                    Date date = new Date(new java.util.Date().getTime());
                                    int type = 1;
                                    Transaction trans = new Transaction(date,amt,type);
//                                    trans.setID(saveTransaction(trans));
                                    JOptionPane.showMessageDialog(null,
                                            "Amount: "+trans.getAmount()
                                            ,String.valueOf(trans.getDate())
                                            ,JOptionPane.INFORMATION_MESSAGE);
                                    try {
                                        UserDao.updateBalanceDeposit(mUser.getAccountNumber()
                                                ,amt,"Current");
                                    } catch (SQLException ex) {
                                        Logger.getLogger(VirtualATM.class.getName())
                                                .log(Level.SEVERE, null, ex);
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"Please enter an amount");
                                }
                                
                                welcome.setVisible(true);
                                
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });
                
                jButton5.removeAll();
                jButton5.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jLabel4.setVisible(false);
                        jLabel5.setVisible(false);
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Enter the amount:");
                        ok.removeActionListener(OkListener);
                        
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    if(mUser.getMonthlyTransactions() >= 6){
                                        JOptionPane.showMessageDialog(null,
                                                "Monthly Transactions depleted");
                                    }else{
                                        long amt = Long.parseLong(jTextField1.getText());
                                        Date date = new Date(new java.util.Date().getTime());
                                        int type = 1;
                                        Transaction trans = new Transaction(date,amt,type);
    //                                    trans.setID(saveTransaction(trans));
                                        JOptionPane.showMessageDialog(null,
                                                "Amount: "+trans.getAmount()
                                                ,String.valueOf(trans.getDate())
                                                ,JOptionPane.INFORMATION_MESSAGE);
                                        try {
                                            UserDao.updateBalanceDeposit(mUser.getAccountNumber()
                                                    ,amt,"Savings");
                                        } catch (SQLException ex) {
                                            Logger.getLogger(VirtualATM.class.getName())
                                                    .log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"Please enter an amount");
                                }
                                cancel.doClick();
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });
            }
        });
        
//        depositSlot.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//               if(jTextField1.getText().equals(""))
//                   JOptionPane.showMessageDialog(null,"Please enter an amount");
//               else{
////                   updateBalanceWithdrawal(Long.parseLong(jTextField1.getText()));
//               }
//            }
//        });
    }
    
    // user pays the bill
    private void setupBillPayment(){
        
        jButton4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel1.setText("Provider1");
                jLabel2.setText("Provider2");
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                
                jButton1.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Utility number: ");
                        
                        ok.removeActionListener(OkListener);
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    long utilityNumber = Long.parseLong(jTextField1.getText());
                                    try {
                                        if(UtilityDao.verifyUser(utilityNumber)){
                                            jTextField1.setText("");
                                            jLabel3.setText("Amount: ");
                                            
                                            ok.removeActionListener(OkListener);
                                            OkListener = new ActionListener(){
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String str = jTextField1.getText();
                                                    if(!str.equals("")){
                                                        try {
                                                            UtilityDao.payBill(utilityNumber,
                                                                    Integer.parseInt(str));
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(VirtualATM.class.getName())
                                                                    .log(Level.SEVERE, null, ex);
                                                        }
                                                    }else{
                                                        try {
                                                            UtilityDao.payBill(utilityNumber, 0);
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(VirtualATM.class.getName())
                                                                    .log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                    JOptionPane.showMessageDialog(
                                                            null,
                                                            "Amount: "+str,
                                                            "Receipt",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                    cancel.doClick();
                                                }
                                            };
                                            ok.addActionListener(OkListener);
                                        }else
                                            JOptionPane.showMessageDialog(null,"Wrong number");
                                    } catch (SQLException ex) {
                                        Logger.getLogger(VirtualATM.class.getName())
                                                .log(Level.SEVERE, null, ex);
                                    }
                                }else
                                    JOptionPane.showMessageDialog(null, "Please specify amount");
                                
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });

                jButton2.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jTextField1.setVisible(true);
                        jTextField1.setText("");
                        jLabel3.setVisible(true);
                        jLabel3.setText("Utility number: ");
                        
                        ok.removeActionListener(OkListener);
                        OkListener = new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!jTextField1.getText().equals("")){
                                    long utilityNumber = Long.parseLong(jTextField1.getText());
                                    try {
                                        if(UtilityDao.verifyUser(utilityNumber)){
                                            jTextField1.setText("");
                                            jLabel3.setText("Amount: ");
                                            
                                            ok.removeActionListener(OkListener);
                                            OkListener = new ActionListener(){
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String str = jTextField1.getText();
                                                    if(!str.equals("")){
                                                        try {
                                                            UtilityDao.payBill(utilityNumber,
                                                                    Integer.parseInt(str));
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(VirtualATM.class.getName())
                                                                    .log(Level.SEVERE, null, ex);
                                                        }
                                                    }else{
                                                        try {
                                                            UtilityDao.payBill(utilityNumber, 0);
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(VirtualATM.class.getName())
                                                                    .log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                    JOptionPane.showMessageDialog(
                                                            null,
                                                            "Amount: "+str,
                                                            "Receipt",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                    cancel.doClick();
                                                }
                                            };
                                            ok.addActionListener(OkListener);
                                        }else
                                            JOptionPane.showMessageDialog(null,"Wrong number");
                                    } catch (SQLException ex) {
                                        Logger.getLogger(VirtualATM.class.getName())
                                                .log(Level.SEVERE, null, ex);
                                    }
                                }else
                                    JOptionPane.showMessageDialog(null, "Please specify amount");
                                
                            }
                        };
                        ok.addActionListener(OkListener);
                    }
                });
            }
        });
    }
    
    // user checks balance
    private void setupBalanceCheck(){
        
        jButton5.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Balance: "+mUser.getBalance(),
                        "Receipt",JOptionPane.INFORMATION_MESSAGE);
                cancel.doClick();
            }
        });
    }
    
    // user changes pin
    private void setupChangePin(){
        
        jButton6.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel1.setVisible(false);
                jLabel2.setVisible(false);
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                jLabel3.setVisible(true);
                jTextField1.setVisible(true);
                jTextField1.setText("");
                jLabel3.setText("Enter new PIN");
                
                ok.removeActionListener(OkListener);
                OkListener = new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String pin = jTextField1.getText();
                        if(!pin.equals("")){
                            jLabel3.setText("Enter PIN again");
                            jTextField1.setText("");
                            
                            ok.removeActionListener(OkListener);
                            OkListener = new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String newPin = jTextField1.getText();
                                    if(!newPin.equals("")){
                                        try {
                                            UserDao.updateUserPin(mUser.getAccountNumber()
                                                    ,Integer.parseInt(newPin));
                                        } catch (SQLException ex) {
                                            Logger.getLogger(VirtualATM.class.getName()).
                                                    log(Level.SEVERE, null, ex);
                                        }
                                    }else
                                        JOptionPane.showMessageDialog(null,"PIN can't be null");
                                    
                                    cancel.doClick();
                                }
                            };
                            ok.addActionListener(OkListener);
                        }else
                            JOptionPane.showMessageDialog(null,"Please enter an amount");
                    }
                };
                ok.addActionListener(OkListener);
            }
        });
    }
    
    // user selects fast cash
    private void setupFastcash(){
        
        jButton7.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel1.setText("20 euro");
                jLabel2.setText("50 euro");
                jLabel4.setText("100 euro");
                jLabel5.setText("200 euro");
                jLabel6.setText("250 euro");
                jLabel7.setText("300 euro");
                
                jButton1.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    20, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 20 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
                jButton2.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    50, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 50 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
                jButton4.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    100, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 100 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
                jButton5.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    200, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 200 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
                jButton6.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    250, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 250 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
                jButton7.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            UserDao.updateBalanceWithdrawal(mUser.getAccountNumber(),
                                    300, "Current");
                        } catch (SQLException ex) {
                            Logger.getLogger(VirtualATM.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null,"Amount: 300 euro"
                                ,"Receipt",JOptionPane.INFORMATION_MESSAGE);
                        cancel.doClick();
                    }
                });
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        welcome = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        one = new javax.swing.JButton();
        two = new javax.swing.JButton();
        three = new javax.swing.JButton();
        four = new javax.swing.JButton();
        five = new javax.swing.JButton();
        six = new javax.swing.JButton();
        seven = new javax.swing.JButton();
        eight = new javax.swing.JButton();
        nine = new javax.swing.JButton();
        zero = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cancel = new javax.swing.JButton();
        ok = new javax.swing.JButton();
        insertCard = new javax.swing.JButton();
        Admin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(640, 600));

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        welcome.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jLabel7.setText("jLabel7");

        jTextField1.setText("jTextField1");

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(124, 124, 124)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcome)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addGap(47, 47, 47))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel4)
                        .addGap(2, 2, 2)
                        .addComponent(welcome))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(2, 2, 2)
                        .addComponent(jLabel5)))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(71, 71, 71))
        );

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        jButton4.setText("jButton3");

        one.setText("jButton7");

        two.setText("jButton8");

        three.setText("jButton9");

        four.setText("jButton10");

        five.setText("jButton11");

        six.setText("jButton12");

        seven.setText("jButton13");

        eight.setText("jButton14");

        nine.setText("jButton15");

        zero.setText("jButton16");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(seven, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(four, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zero, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(two, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(three, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(six, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(eight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nine, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(two, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(four, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(six, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seven, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nine, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zero))
        );

        jButton5.setText("jButton4");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("jButton5");

        jButton7.setText("jButton6");

        cancel.setText("jButton17");

        ok.setText("jButton18");

        insertCard.setText("jButton19");

        Admin.setText("jButton20");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Admin, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertCard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Admin)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 95, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VirtualATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VirtualATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VirtualATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VirtualATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VirtualATM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Admin;
    private javax.swing.JButton cancel;
    private javax.swing.JButton eight;
    private javax.swing.JButton five;
    private javax.swing.JButton four;
    private javax.swing.JButton insertCard;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton nine;
    private javax.swing.JButton ok;
    private javax.swing.JButton one;
    private javax.swing.JButton seven;
    private javax.swing.JButton six;
    private javax.swing.JButton three;
    private javax.swing.JButton two;
    private javax.swing.JLabel welcome;
    private javax.swing.JButton zero;
    // End of variables declaration//GEN-END:variables
}
