package com.example.utaste.data;

import com.example.utaste.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static UserRepository instance;
    private Map<String, User> usersByEmail;

    private UserRepository() {
        usersByEmail = new HashMap<>();
        // Create default accounts
        User admin = new User("admin@local", "admin-pwd", User.Role.ADMIN);
        admin.setFirstName("System");
        admin.setLastName("Admin");
        usersByEmail.put(admin.getEmail(), admin);

        User chef = new User("chef@local", "chef-pwd", User.Role.CHEF);
        chef.setFirstName("Head");
        chef.setLastName("Chef");
        usersByEmail.put(chef.getEmail(), chef);
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean addUser(User user) {
        if (usersByEmail.containsKey(user.getEmail())) {
            return false;
        }
        usersByEmail.put(user.getEmail(), user);
        return true;
    }

    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    public boolean authenticate(String email, String password) {
        User u = findByEmail(email);
        if (u == null) return false;
        return u.getPassword().equals(password);
    }
}
