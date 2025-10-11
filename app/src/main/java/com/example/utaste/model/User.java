package com.example.utaste.model;

public class User {

    public enum Role {
        ADMIN,
        CHEF,
        WAITER
    }

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    private long createdAt;   // timestamp de création
    private long modifiedAt;  // timestamp de dernière modification

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }

    // --- Getters et Setters ---
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        touchModified();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
        touchModified();
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        touchModified();
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        touchModified();
    }

    public Role getRole() { return role; }
    public void setRole(Role role) {
        this.role = role;
        touchModified();
    }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(long modifiedAt) { this.modifiedAt = modifiedAt; }

    public void touchModified() {
        this.modifiedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}
