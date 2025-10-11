package com.example.utaste.util;

import android.util.Patterns;

public class Validators {

    public static String validateLoginFields(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        // Accept local emails like admin@local, chef@local, waiter@local
        if (!email.endsWith(".local") && !email.endsWith("@local") && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email format is invalid.";
        }

        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        if (password.length() < 5) {
            return "Password must be at least 5 characters.";
        }

        return null; // OK
    }

    public static String validateNewUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        // Relaxed validator: accept .local or @local
        if (!email.endsWith(".local") && !email.endsWith("@local") && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email format is invalid.";
        }

        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        if (password.length() < 5) {
            return "Password must be at least 5 characters.";
        }

        return null;
    }
}
