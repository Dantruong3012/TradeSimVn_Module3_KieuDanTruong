package com.stock_market_simulator.stock_market_simulator.dto;

import java.math.BigDecimal;

public class UserDto {
    private  Integer id;
    private  String userName;
    private  String userDisplayName;
    private  BigDecimal balance;
    private  BigDecimal lockedBalance;

    public UserDto() {}

    public UserDto(String userName, String userDisplayName, BigDecimal balance, BigDecimal lockedBalance) {
        this.userName = userName;
        this.userDisplayName = userDisplayName;
        this.balance = balance;
        this.lockedBalance = lockedBalance;
    }

    public UserDto(Integer id, String userName, String userDisplayName, BigDecimal balance, BigDecimal lockedBalance) {
        this.id = id;
        this.userName = userName;
        this.userDisplayName = userDisplayName;
        this.balance = balance;
        this.lockedBalance = lockedBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
