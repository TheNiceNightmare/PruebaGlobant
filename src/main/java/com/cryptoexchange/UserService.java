package com.cryptoexchange;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();
    private int userIdCounter = 1;

    public User registerUser(String name, String email, String password) {
        User user = new User(name, email, password, userIdCounter++);
        users.add(user);
        return user;
    }

    public User loginUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Invalid credentials
    }
}
