package com.example.utaste.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;
import com.example.utaste.util.Validators;

public class AdminActivity extends AppCompatActivity {

    private Button btnCreateWaiter, btnResetDB, btnManageProfiles, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize buttons
        btnCreateWaiter = findViewById(R.id.btnCreateWaiter);
        btnResetDB = findViewById(R.id.btnResetDB);
        btnManageProfiles = findViewById(R.id.btnManageProfiles);
        btnLogout = findViewById(R.id.btnLogout);

        // Only Create Waiter and Logout are enabled for now
        btnResetDB.setEnabled(false);
        btnManageProfiles.setEnabled(false);

        // Create Waiter button
        btnCreateWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateWaiterDialog();
            }
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(i);
            finish(); // close AdminActivity
        });
    }

    // Step 6: Admin creates a new Waiter
    private void showCreateWaiterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Waiter");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setHint("First Name (optional)");
        layout.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setHint("Last Name (optional)");
        layout.addView(lastNameInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);

        builder.setPositiveButton("Create", null); // we override later
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validate email and password
            String validationError = Validators.validateNewUser(email, password);
            if (validationError != null) {
                Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show();
                return;
            }

            // Default password if empty
            if (password.isEmpty()) password = "waiter-pwd";

            // Create new Waiter user
            User newWaiter = new User(email, password, User.Role.WAITER);
            newWaiter.setFirstName(firstName);
            newWaiter.setLastName(lastName);

            boolean added = UserRepository.getInstance().addUser(newWaiter);
            if (!added) {
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Waiter created successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
