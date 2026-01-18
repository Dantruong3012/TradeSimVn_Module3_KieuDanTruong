package com.stock_market_simulator.stock_market_simulator.util.Mapper;

import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.User;

public class UserMapper {
    public  static UserDto userToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setUserDisplayName(user.getDisplayName());
        userDto.setBalance(user.getBalance());
        userDto.setLockedBalance(user.getLockedBalance());
        return  userDto;
    }
}
