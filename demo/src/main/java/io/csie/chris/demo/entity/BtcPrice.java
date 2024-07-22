package io.csie.chris.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "btc_prices")
public class BtcPrice {
    @Id
    private String id = "btcPrice";
    private double price;
    private long timestamp;
}
