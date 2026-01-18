package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.CashFlowDao;
import com.stock_market_simulator.stock_market_simulator.dao.NotificationDao;
import com.stock_market_simulator.stock_market_simulator.dao.UserDao;
import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.CashFlow;
import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;
import com.stock_market_simulator.stock_market_simulator.model.entity.User;
import com.stock_market_simulator.stock_market_simulator.util.hashing.PasswordUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    private UserDao userDao = new UserDao();
    private CashFlowDao cashFlowDao = new CashFlowDao();
    private NotificationDao notificationDao = new NotificationDao();

    public boolean isUserExists(String userName) {
        return userDao.isUserNameExist(userName);
    }

    public  boolean register(User user) {
        Connection connection = null;
        try {

            BigDecimal bonus = new BigDecimal("1000000000");

            connection = JdbcConnection.getConnection(); // lay connection tu config
            connection.setAutoCommit(false); // tat autocommit de bat dau transaction

            // HASH PASSWORD
            String rawConfirmPassword = user.getConfirmPassword();
            String rawPassword = user.getUserPassword();
            String hashedPassword = PasswordUtil.hashPassword(rawPassword);
            String hashedConfirm = PasswordUtil.hashPassword(rawConfirmPassword);
            user.setUserPassword(hashedPassword);
            user.setConfirmPassword(hashedConfirm);

            // goi ham userDao de dang ky user moi va tra ve id de cong cho 1 toi.
            int newUserID = userDao.registerNewUser(connection, user);
            if (newUserID == 0){
                connection.rollback();
                return false;
            }
            CashFlow initiallation = new CashFlow(newUserID, bonus, bonus, "DEPOSIT", "Quà khởi nghiệp"); // tao cashflow cho nguoi dung moi voi id moi
            cashFlowDao.insertInCashFlow(connection, initiallation);
//           userDao.updateUserBalance(connection, newUserID, bonus, BigDecimal.ZERO); truong hop khong set cung db
            // sau nay thi khong nen set cung db trong truong hop muon sua db thi co the gay anh huong den du lieu

            // SET THONG BAO SAU KHI DANG KY THANH CONG
            Notification notifi = new Notification();
            notifi.setUserId(newUserID);
            notifi.setMessage("Chào mừng! Bạn đã nhận được 1 tỷ tiền trải nghiệm.");
            notifi.setType("SUCCESS");
            notificationDao.insertIntoNotification(connection, notifi);
            connection.commit();
            return true;
        }catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ex){ex.printStackTrace();}
            return  false;
        } finally {
            try {
                if (connection != null){connection.close();}
            }catch (SQLException e){e.printStackTrace();}
        }
    }

    public UserDto login(String userName, String userPassword) throws  SQLException {
       Connection connection = null;
       try {
           connection = JdbcConnection.getConnection();

           User userEntity = userDao.findUserByUserName(connection, userName);
           if (userEntity == null){
               return  null;
           }

           if (PasswordUtil.checkPassword(userPassword, userEntity.getUserPassword())){
               UserDto userDto = new UserDto();
               userDto.setId(userEntity.getId());
               userDto.setUserName(userEntity.getUserName());
               userDto.setUserDisplayName(userEntity.getDisplayName());
               userDto.setBalance(userEntity.getBalance());
               userDto.setLockedBalance(userEntity.getLockedBalance());
               return  userDto;
           }

       }finally {
          try {
              if (connection != null){connection.close();}
          } catch (SQLException e) {e.printStackTrace();}
       }
       return  null;
    }

    public boolean resetPassword(String userName, String securityCode, String newPassword){
       Connection connection = null;
       try {
           connection = JdbcConnection.getConnection();
           connection.setAutoCommit(false);

           User user    = userDao.findUserByUserName(connection, userName);
           if (user == null){
               return false;
           }

           if (user.getSecurityCode() == null || !securityCode.equals(user.getSecurityCode())){
               return  false;
           }
           int userId = user.getId();
           String newHashedPassword = PasswordUtil.hashPassword(newPassword);
           userDao.updateUserPassword(connection, newHashedPassword, userId);
           userDao.updateUserConfirmPassword(connection, newHashedPassword, userId);
           connection.commit(); // commit luu thay doi
           return  true;

       } catch (SQLException e) {
           try {
               if (connection != null){
                   connection.rollback();
               }
           }catch (SQLException ex){
               ex.printStackTrace();
           }
           e.printStackTrace();
       }finally {
           if (connection != null){
               try {
                   connection.close();
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
           }
       }
    return  false;
    }


    public UserDto findUserById(int userId) {
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();

            User user = userDao.findUserByID(connection, userId);

            if (user == null) {
                return null;
            }

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setUserDisplayName(user.getDisplayName());
            userDto.setBalance(user.getBalance());
            userDto.setLockedBalance(user.getLockedBalance());

            return userDto;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (connection != null) { connection.close(); }
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

}
