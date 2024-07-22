package io.csie.chris.demo.repository;

import io.csie.chris.demo.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Page<User> findAll(Pageable pageable);
}