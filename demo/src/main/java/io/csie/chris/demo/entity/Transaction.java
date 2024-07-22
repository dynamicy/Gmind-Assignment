package io.csie.chris.demo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.csie.chris.demo.common.TransactionType;
import io.csie.chris.demo.serializer.ObjectIdSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private ObjectId userId;
    private double btcAmount;
    private double usdAmount;
    private TransactionType transactionType;
    private long timestamp = System.currentTimeMillis();
}
