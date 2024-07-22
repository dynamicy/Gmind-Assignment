package io.csie.chris.demo.dto;

import io.csie.chris.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String id;
    private String name;
    private String email;

    public static UserModel fromEntity(User user) {
        return new UserModel(user.getId().toHexString(), user.getName(), user.getEmail());
    }
}