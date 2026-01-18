package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.math.BigDecimal;

public class User {
    private  Integer  id;
    private  String   userName;
    private  String   displayName;
    private  String   userPassword;
    private  String confirmPassword;
    private BigDecimal balance;
    private BigDecimal lockedBalance;
    private String securityCode;

    public User() {}

    public User(String userName, String displayName, String userPassword, String confirmPassword, String securityCode) {
        this.userName = userName;
        this.displayName = displayName;
        this.userPassword = userPassword;
        this.confirmPassword = confirmPassword;
        this.securityCode = securityCode;
    }

    public User(Integer id, String userName, String disPlayName, String userPassword, String securityCode) {
        this.id = id;
        this.userName = userName;
        this.displayName = disPlayName;
        this.userPassword = userPassword;
        this.securityCode = securityCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getLockedBalance() {
        return lockedBalance;
    }

    public void setLockedBalance(BigDecimal lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
