package io.csie.chris.demo.controller;

import io.csie.chris.demo.common.Constants;
import io.csie.chris.demo.dto.UserModel;
import io.csie.chris.demo.response.ApiResponse;
import io.csie.chris.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Add a new user", description = "Add a new user with name and email")
    public ResponseEntity<ApiResponse<UserModel>> addUser(@RequestParam String name, @RequestParam String email) {
        UserModel user = userService.addUser(name, email);
        ApiResponse<UserModel> response = new ApiResponse<>(true, "User added successfully", user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "User deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user", description = "Get a user by ID")
    public ResponseEntity<ApiResponse<UserModel>> getUserById(@PathVariable String id) {
        UserModel user = userService.getUserById(id);
        ApiResponse<UserModel> response = new ApiResponse<>(true, "User retrieved successfully", user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users with pagination")
    public ResponseEntity<ApiResponse<Page<UserModel>>> getAllUsers(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Page<UserModel> users = userService.getAllUsers(page, size);
        ApiResponse<Page<UserModel>> response = new ApiResponse<>(true, "Users retrieved successfully", users);
        return ResponseEntity.ok(response);
    }
}