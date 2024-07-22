package io.csie.chris.demo.repository;

import io.csie.chris.demo.entity.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {
    Page<Transaction> findAllByUserId(ObjectId userId, Pageable pageable);
}