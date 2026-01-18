package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Notification {
    private Integer id;
    private  Integer userId;
    private String message;
    private String type;
    private boolean seen;
    private Timestamp createdAt;

    public Notification() {
    }

    public Notification(Integer userId, String message, String type) {
        this.userId = userId;
        this.message = message;
        this.type = type;
    }

    public Notification(Integer id, Integer userId, String message, String type, boolean seen, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.seen = seen;
        this.createdAt = createdAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
