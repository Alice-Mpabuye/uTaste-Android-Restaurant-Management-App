package com.example.utaste.util;

import android.util.Patterns;

public class Validators {

    /**
     * Validator for login fields. Accepts local emails like admin@local, chef@local, waiter@local,
     * or any email that matches Android Patterns.EMAIL_ADDRESS.
     */
    public static String validateLoginFields(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        // allow local addresses that end with ".local" or "@local"
        String e = email.trim();
        if (!(e.endsWith(".local") || e.endsWith("@local") || Patterns.EMAIL_ADDRESS.matcher(e).matches())) {
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

    /**
     * Validator for creating/updating users. Same relaxed rule for local addresses.
     */
    public static String validateNewUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        String e = email.trim();
        if (!(e.endsWith(".local") || e.endsWith("@local") || Patterns.EMAIL_ADDRESS.matcher(e).matches())) {
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
