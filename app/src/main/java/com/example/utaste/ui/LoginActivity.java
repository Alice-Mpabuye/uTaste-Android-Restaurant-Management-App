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

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passField;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.email);
        passField = findViewById(R.id.password);
        errorText = findViewById(R.id.errorText);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure any uncommitted text is read
                emailField.clearFocus();
                passField.clearFocus();

                String email = emailField.getText().toString().trim();
                String pass = passField.getText().toString();

                System.out.println("Email input: '" + email + "'");

                // Validate fields using relaxed rules for local users
                String validation = Validators.validateLoginFields(email, pass);
                if (validation != null) {
                    showError(validation);
                    return;
                }

                UserRepository repo = UserRepository.getInstance();
                boolean ok = repo.authenticate(email, pass);
                if (!ok) {
                    showError("Invalid credentials. Check email/password.");
                    return;
                }

                User u = repo.findByEmail(email);
                navigateToRoleHome(u);
            }
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
}
