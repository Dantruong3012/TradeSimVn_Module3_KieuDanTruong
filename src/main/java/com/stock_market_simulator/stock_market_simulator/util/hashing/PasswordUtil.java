package com.stock_market_simulator.stock_market_simulator.util.hashing;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public  static  String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    // ham dung de kiem tra mat khau

    public  static  boolean checkPassword(String rawPassword, String hashedPassword){
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
