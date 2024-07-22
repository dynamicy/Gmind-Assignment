package io.csie.chris.demo.service;

import io.csie.chris.demo.dto.UserModel;
import io.csie.chris.demo.entity.User;
import io.csie.chris.demo.exception.UserNotFoundException;
import io.csie.chris.demo.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void addUser() {
        User user = new User();
        user.setId(new ObjectId());
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserModel result = userService.addUser("Test User", "test@example.com");

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        ObjectId userId = new ObjectId();
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId.toHexString());

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_NotFound() {
        ObjectId userId = new ObjectId();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId.toHexString()));
    }

    @Test
    void getUserById() {
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel result = userService.getUserById(userId.toHexString());

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserById_NotFound() {
        ObjectId userId = new ObjectId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId.toHexString()));
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        user1.setId(new ObjectId());
        user1.setName("User1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(new ObjectId());
        user2.setName("User2");
        user2.setEmail("user2@example.com");

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Arrays.asList(user1, user2), pageable, 2);

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserModel> result = userService.getAllUsers(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("User1", result.getContent().get(0).getName());
        assertEquals("user1@example.com", result.getContent().get(0).getEmail());
        assertEquals("User2", result.getContent().get(1).getName());
        assertEquals("user2@example.com", result.getContent().get(1).getEmail());
    }
}