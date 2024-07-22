package io.csie.chris.demo.controller;

import io.csie.chris.demo.dto.BtcPriceModel;
import io.csie.chris.demo.response.ApiResponse;
import io.csie.chris.demo.service.BtcPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BtcPriceController {

    @Autowired
    private BtcPriceService btcPriceService;

    @GetMapping("/btcPrice")
    public ResponseEntity<ApiResponse<BtcPriceModel>> getCurrentBtcPrice() {
        BtcPriceModel btcPrice = btcPriceService.getCurrentBtcPrice();
        if (btcPrice == null) {
            ApiResponse<BtcPriceModel> response = new ApiResponse<>(false, "BTC price not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        ApiResponse<BtcPriceModel> response = new ApiResponse<>(true, "BTC price retrieved successfully", btcPrice);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}