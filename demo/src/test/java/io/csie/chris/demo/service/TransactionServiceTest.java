package io.csie.chris.demo.service;

import io.csie.chris.demo.common.TransactionType;
import io.csie.chris.demo.dto.BtcPriceModel;
import io.csie.chris.demo.dto.TransactionModel;
import io.csie.chris.demo.entity.Transaction;
import io.csie.chris.demo.entity.User;
import io.csie.chris.demo.exception.InsufficientBalanceException;
import io.csie.chris.demo.exception.InsufficientBtcBalanceException;
import io.csie.chris.demo.exception.UserNotFoundException;
import io.csie.chris.demo.repository.TransactionRepository;
import io.csie.chris.demo.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BtcPriceService btcPriceService;

    @InjectMocks
    private TransactionService transactionService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void buyBtc() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setUsdBalance(1000.0);
        user.setBtcBalance(0.0);

        BtcPriceModel btcPriceModel = new BtcPriceModel();
        btcPriceModel.setPrice(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(btcPriceService.getCurrentBtcPrice()).thenReturn(btcPriceModel);

        Transaction transaction = new Transaction();
        transaction.setId(new ObjectId());
        transaction.setUserId(userId);
        transaction.setBtcAmount(2.0);
        transaction.setUsdAmount(200.0);
        transaction.setTransactionType(TransactionType.BUY);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionModel result = transactionService.buyBtc(userId.toHexString(), 2.0);

        assertNotNull(result);
        assertEquals(TransactionType.BUY, result.getTransactionType());
        assertEquals(2.0, result.getBtcAmount());
        assertEquals(200.0, result.getUsdAmount());
        assertEquals(userId.toHexString(), result.getUserId());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(btcPriceService, times(1)).getCurrentBtcPrice();
        verify(transactionRepository, times(1)).save(any(Transaction.class));

        assertEquals(800.0, user.getUsdBalance());
        assertEquals(2.0, user.getBtcBalance());
    }

    @Test
    void buyBtc_InsufficientBalance() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setUsdBalance(100.0);
        user.setBtcBalance(0.0);

        BtcPriceModel btcPriceModel = new BtcPriceModel();
        btcPriceModel.setPrice(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(btcPriceService.getCurrentBtcPrice()).thenReturn(btcPriceModel);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.buyBtc(userId.toHexString(), 2.0));
    }

    @Test
    void sellBtc() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setUsdBalance(1000.0);
        user.setBtcBalance(2.0);

        BtcPriceModel btcPriceModel = new BtcPriceModel();
        btcPriceModel.setPrice(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(btcPriceService.getCurrentBtcPrice()).thenReturn(btcPriceModel);

        Transaction transaction = new Transaction();
        transaction.setId(new ObjectId());
        transaction.setUserId(userId);
        transaction.setBtcAmount(2.0);
        transaction.setUsdAmount(200.0);
        transaction.setTransactionType(TransactionType.SELL);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionModel result = transactionService.sellBtc(userId.toHexString(), 2.0);

        assertNotNull(result);
        assertEquals(TransactionType.SELL, result.getTransactionType());
        assertEquals(2.0, result.getBtcAmount());
        assertEquals(200.0, result.getUsdAmount());
        assertEquals(userId.toHexString(), result.getUserId());  // 使用 toHexString() 進行比較

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(btcPriceService, times(1)).getCurrentBtcPrice();
        verify(transactionRepository, times(1)).save(any(Transaction.class));

        assertEquals(1200.0, user.getUsdBalance());
        assertEquals(0.0, user.getBtcBalance());
    }

    @Test
    void sellBtc_InsufficientBtcBalance() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setUsdBalance(1000.0);
        user.setBtcBalance(1.0);

        BtcPriceModel btcPriceModel = new BtcPriceModel();
        btcPriceModel.setPrice(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(btcPriceService.getCurrentBtcPrice()).thenReturn(btcPriceModel);

        assertThrows(InsufficientBtcBalanceException.class, () -> transactionService.sellBtc(userId.toHexString(), 2.0));
    }

    @Test
    void getUserTransactions() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        Transaction transaction1 = new Transaction();
        transaction1.setId(new ObjectId());
        transaction1.setUserId(userId);
        transaction1.setBtcAmount(0.5);
        transaction1.setUsdAmount(10000);
        transaction1.setTransactionType(TransactionType.BUY);

        Transaction transaction2 = new Transaction();
        transaction2.setId(new ObjectId());
        transaction2.setUserId(userId);
        transaction2.setBtcAmount(1.0);
        transaction2.setUsdAmount(20000);
        transaction2.setTransactionType(TransactionType.SELL);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(Arrays.asList(transaction1, transaction2), pageable, 2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId, pageable)).thenReturn(page);

        Page<TransactionModel> result = transactionService.getUserTransactions(userId.toHexString(), 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(0.5, result.getContent().get(0).getBtcAmount());
        assertEquals(10000, result.getContent().get(0).getUsdAmount());
        assertEquals(TransactionType.BUY, result.getContent().get(0).getTransactionType());
        assertEquals(1.0, result.getContent().get(1).getBtcAmount());
        assertEquals(20000, result.getContent().get(1).getUsdAmount());
        assertEquals(TransactionType.SELL, result.getContent().get(1).getTransactionType());
    }

    @Test
    void getUserTransactions_UserNotFound() {
        ObjectId userId = new ObjectId();
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.getUserTransactions(userId.toHexString(), 0, 10));
    }
}