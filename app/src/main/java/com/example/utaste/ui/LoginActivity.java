package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;
import com.example.utaste.util.Validators;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passField;
    private TextView errorText;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find views (IDs must match your activity_login.xml)
        emailField = findViewById(R.id.email);
        passField = findViewById(R.id.password);
        errorText = findViewById(R.id.errorText);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            // Ensure any uncommitted text is read
            emailField.clearFocus();
            passField.clearFocus();

            String email = emailField.getText().toString().trim();
            String pass = passField.getText().toString();

            // Validate fields
            String validation = Validators.validateLoginFields(email, pass);
            if (validation != null) {
                showError(validation);
                return;
            }

            // Run authentication off UI thread
            executor.execute(() -> {
                try {
                    UserRepository repo = UserRepository.getInstance();
                    boolean ok = repo.authenticate(email, pass);
                    if (!ok) {
                        runOnUiThread(() -> showError("Invalid credentials. Check email/password."));
                        return;
                    }

                    User u = repo.findByEmail(email);
                    if (u == null) {
                        runOnUiThread(() -> showError("User record not found after authentication."));
                        return;
                    }

                    // Navigate on UI thread
                    runOnUiThread(() -> navigateToRoleHome(u));
                } catch (Exception e) {
                    // Unexpected error — show message on UI
                    runOnUiThread(() -> showError("An error occurred: " + e.getMessage()));
                }
            });
        });
    }

    private void showError(String msg) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }

    private void navigateToRoleHome(User u) {
        Intent i;
        if (u.getRole() == User.Role.ADMIN) {
            i = new Intent(this, AdminActivity.class);
        } else if (u.getRole() == User.Role.CHEF) {
            i = new Intent(this, ChefActivity.class);
        } else {
            i = new Intent(this, WaiterActivity.class);
        }
        i.putExtra("userEmail", u.getEmail());
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
