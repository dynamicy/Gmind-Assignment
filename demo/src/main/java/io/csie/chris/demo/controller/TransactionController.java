package io.csie.chris.demo.controller;

import io.csie.chris.demo.common.Constants;
import io.csie.chris.demo.dto.TransactionModel;
import io.csie.chris.demo.response.ApiResponse;
import io.csie.chris.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse<TransactionModel>> buyBtc(@RequestParam String userId, @RequestParam double btcQuantity) {
        try {
            TransactionModel transaction = transactionService.buyBtc(userId, btcQuantity);
            ApiResponse<TransactionModel> response = new ApiResponse<>(true, "BTC purchase successful", transaction);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<TransactionModel> response = new ApiResponse<>(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<ApiResponse<TransactionModel>> sellBtc(@RequestParam String userId, @RequestParam double btcQuantity) {
        try {
            TransactionModel transaction = transactionService.sellBtc(userId, btcQuantity);
            ApiResponse<TransactionModel> response = new ApiResponse<>(true, "BTC sale successful", transaction);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<TransactionModel> response = new ApiResponse<>(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user transactions", description = "Get transactions for a user by ID")
    public ResponseEntity<ApiResponse<Page<TransactionModel>>> getUserTransactions(@PathVariable String userId,
                                                                                   @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                                                   @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Page<TransactionModel> transactions = transactionService.getUserTransactions(userId, page, size);
        ApiResponse<Page<TransactionModel>> response = new ApiResponse<>(true, "Transactions retrieved successfully", transactions);
        return ResponseEntity.ok(response);
    }
}