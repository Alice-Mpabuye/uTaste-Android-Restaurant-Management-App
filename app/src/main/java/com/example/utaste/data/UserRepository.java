package com.example.utaste.data;

import com.example.utaste.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple in-memory repository used for Livrable 1.
 */
public class UserRepository {
    private static UserRepository instance;
    private final Map<String, User> usersByEmail;

    private UserRepository() {
        usersByEmail = new HashMap<>();

        // Default accounts
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
        if (instance == null) instance = new UserRepository();
        return instance;
    }

    public synchronized boolean addUser(User user) {
        if (user == null || user.getEmail() == null) return false;
        if (usersByEmail.containsKey(user.getEmail())) return false;

        // Enregistrer date de création et modification au moment de l’ajout
        user.setCreatedAt(System.currentTimeMillis());
        user.setModifiedAt(System.currentTimeMillis());

        usersByEmail.put(user.getEmail(), user);
        return true;
    }

    public synchronized User findByEmail(String email) {
        if (email == null) return null;
        return usersByEmail.get(email);
    }

    public synchronized boolean authenticate(String email, String password) {
        User u = findByEmail(email);
        if (u == null) return false;
        return u.getPassword().equals(password);
    }

    /**
     * Update an existing user.
     * If email changes, ensure uniqueness then move entry.
     * Returns true if update succeeded.
     */
    public synchronized boolean updateUser(String originalEmail, User updated) {
        if (originalEmail == null || updated == null || updated.getEmail() == null) return false;
        User existing = usersByEmail.get(originalEmail);
        if (existing == null) return false;

        // Si l’email change, vérifier unicité
        if (!originalEmail.equals(updated.getEmail()) && usersByEmail.containsKey(updated.getEmail())) {
            return false;
        }

        // Préserver la date de création si elle est vide
        if (updated.getCreatedAt() == 0L) {
            updated.setCreatedAt(existing.getCreatedAt());
        }

        // Mettre à jour la date de modification
        updated.setModifiedAt(System.currentTimeMillis());

        // Si l’email change, remplacer la clé
        if (!originalEmail.equals(updated.getEmail())) {
            usersByEmail.remove(originalEmail);
        }

        usersByEmail.put(updated.getEmail(), updated);
        return true;
    }

    public synchronized boolean deleteUser(String email) {
        if (email == null || !usersByEmail.containsKey(email)) return false;
        usersByEmail.remove(email);
        return true;
    }

    public synchronized List<User> listWaiters() {
        List<User> res = new ArrayList<>();
        for (User u : usersByEmail.values()) {
            if (u.getRole() == User.Role.WAITER) res.add(u);
        }
        return res;
    }

    public synchronized List<User> listAllUsers() {
        return new ArrayList<>(usersByEmail.values());
    }
}
