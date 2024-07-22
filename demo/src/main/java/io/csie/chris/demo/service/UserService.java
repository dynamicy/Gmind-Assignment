package io.csie.chris.demo.service;

import io.csie.chris.demo.dto.UserModel;
import io.csie.chris.demo.exception.UserNotFoundException;
import io.csie.chris.demo.entity.User;
import io.csie.chris.demo.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel addUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user = userRepository.save(user);
        return UserModel.fromEntity(user);
    }

    public void deleteUser(String id) {
        ObjectId userId = new ObjectId(id);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public UserModel getUserById(String id) {
        ObjectId userId = new ObjectId(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserModel.fromEntity(user);
    }

    public Page<UserModel> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserModel::fromEntity);
    }
}