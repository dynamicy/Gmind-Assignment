package io.csie.chris.demo.repository;

import io.csie.chris.demo.entity.BtcPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtcPriceRepository extends MongoRepository<BtcPrice, String> {
}