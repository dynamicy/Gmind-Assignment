package io.csie.chris.demo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.csie.chris.demo.serializer.ObjectIdSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;
    private String email;
    private double usdBalance = 1000.0;
    private double btcBalance = 0.0;
}
