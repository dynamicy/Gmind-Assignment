package io.csie.chris.demo.dto;

import io.csie.chris.demo.common.TransactionType;
import io.csie.chris.demo.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
    private String id;
    private String userId;
    private double btcAmount;
    private double usdAmount;
    private TransactionType transactionType;
    private long timestamp;

    public static TransactionModel fromEntity(Transaction transaction) {
        return new TransactionModel(
                transaction.getId().toHexString(),
                transaction.getUserId().toHexString(),
                transaction.getBtcAmount(),
                transaction.getUsdAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }
}