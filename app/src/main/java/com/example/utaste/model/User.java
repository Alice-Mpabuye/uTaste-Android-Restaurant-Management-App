package com.example.utaste.model;

public class User {
    public enum Role { ADMIN, CHEF, WAITER }

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private long createdAt;
    private long modifiedAt;

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = this.createdAt;
    }

    // Full getters / setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; touchModified(); }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; touchModified(); }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; touchModified(); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; touchModified(); }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; touchModified(); }

    public long getCreatedAt() { return createdAt; }
    // allow preserving createdAt when updating (used by repository)
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getModifiedAt() { return modifiedAt; }

    // Update modified timestamp
    public void touchModified() { this.modifiedAt = System.currentTimeMillis(); }
}
