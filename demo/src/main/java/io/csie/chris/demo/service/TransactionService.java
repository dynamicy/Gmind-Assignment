package io.csie.chris.demo.service;

import io.csie.chris.demo.common.TransactionType;
import io.csie.chris.demo.dto.TransactionModel;
import io.csie.chris.demo.entity.Transaction;
import io.csie.chris.demo.entity.User;
import io.csie.chris.demo.exception.InsufficientBalanceException;
import io.csie.chris.demo.exception.InsufficientBtcBalanceException;
import io.csie.chris.demo.exception.UserNotFoundException;
import io.csie.chris.demo.repository.TransactionRepository;
import io.csie.chris.demo.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BtcPriceService btcPriceService;

    @Transactional
    public TransactionModel buyBtc(String userId, double btcQuantity) {
        ObjectId userObjectId = new ObjectId(userId);

        User user = userRepository.findById(userObjectId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        double btcPrice = btcPriceService.getCurrentBtcPrice().getPrice();
        double cost = btcQuantity * btcPrice;
        if (user.getUsdBalance() < cost) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        user.setUsdBalance(user.getUsdBalance() - cost);
        user.setBtcBalance(user.getBtcBalance() + btcQuantity);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUserId(userObjectId);
        transaction.setBtcAmount(btcQuantity);
        transaction.setUsdAmount(cost);
        transaction.setTransactionType(TransactionType.BUY);
        transaction = transactionRepository.save(transaction);

        return TransactionModel.fromEntity(transaction);
    }

    @Transactional
    public TransactionModel sellBtc(String userId, double btcQuantity) {
        ObjectId userObjectId = new ObjectId(userId);

        User user = userRepository.findById(userObjectId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getBtcBalance() < btcQuantity) {
            throw new InsufficientBtcBalanceException("Insufficient BTC balance");
        }

        double btcPrice = btcPriceService.getCurrentBtcPrice().getPrice();
        double revenue = btcQuantity * btcPrice;
        user.setBtcBalance(user.getBtcBalance() - btcQuantity);
        user.setUsdBalance(user.getUsdBalance() + revenue);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUserId(userObjectId);
        transaction.setBtcAmount(btcQuantity);
        transaction.setUsdAmount(revenue);
        transaction.setTransactionType(TransactionType.SELL);
        transaction = transactionRepository.save(transaction);

        return TransactionModel.fromEntity(transaction);
    }

    public Page<TransactionModel> getUserTransactions(String userId, int page, int size) {
        ObjectId userObjectId = new ObjectId(userId);
        userRepository.findById(userObjectId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findAllByUserId(userObjectId, pageable);
        return transactions.map(TransactionModel::fromEntity);
    }
}