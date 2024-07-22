package io.csie.chris.demo.dto;

import io.csie.chris.demo.entity.BtcPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BtcPriceModel {
    private String id;
    private double price;
    private long timestamp;

    public static BtcPriceModel fromEntity(BtcPrice btcPrice) {
        return new BtcPriceModel(
                btcPrice.getId(),
                btcPrice.getPrice(),
                btcPrice.getTimestamp()
        );
    }
}