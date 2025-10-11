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

    // getters / setters (omitted for brevity) - implement normally
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; touchModified(); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; touchModified(); }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; touchModified(); }
    private void touchModified() { this.modifiedAt = System.currentTimeMillis(); }
    public long getCreatedAt() { return createdAt; }
    public long getModifiedAt() { return modifiedAt; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; touchModified(); }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; touchModified(); }
}
