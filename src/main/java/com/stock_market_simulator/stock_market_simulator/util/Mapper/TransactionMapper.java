package com.stock_market_simulator.stock_market_simulator.util.Mapper;

import com.stock_market_simulator.stock_market_simulator.dto.TransactionDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Transaction;
public class TransactionMapper {
    public  static TransactionDto transactionToDto(Transaction transaction, String side){
       TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSymbol(transaction.getSymbol());
        transactionDto.setPrice(transaction.getPrice());
        transactionDto.setSide(side);
        return  transactionDto;
    }
}
