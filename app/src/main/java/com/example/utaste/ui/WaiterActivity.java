package com.example.utaste.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;

public class WaiterActivity extends AppCompatActivity {

    private Button btnLogout, btnChangePassword, btnViewRecipe;
    private String currentWaiterEmail = null;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        // Initialize repository with context
        userRepository = UserRepository.getInstance();
        userRepository.init(getApplicationContext());

        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnViewRecipe = findViewById(R.id.btnViewRecipes);

        // Check if user email is available in intent

        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            currentWaiterEmail = getIntent().getStringExtra("userEmail");
        }

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(WaiterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        btnChangePassword.setOnClickListener(v -> {
            if (currentWaiterEmail == null) {
                Toast.makeText(this, "No user email available", Toast.LENGTH_SHORT).show();
            } else {
                showChangePasswordDialog(currentWaiterEmail);
            }
        });

        btnViewRecipe.setOnClickListener(v -> {
            Intent i = new Intent(WaiterActivity.this, RecipeListActivity.class);
            i.putExtra("IS_READ_ONLY", true);
            startActivity(i);
        });
    }

    private void showChangePasswordDialog(String userEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        final EditText oldPwd = new EditText(this);
        oldPwd.setHint("Current password");
        oldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(oldPwd);

        final EditText newPwd = new EditText(this);
        newPwd.setHint("New password");
        newPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPwd);

        final EditText newPwd2 = new EditText(this);
        newPwd2.setHint("Confirm new password");
        newPwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPwd2);

        builder.setTitle("Change password");
        builder.setView(layout);
        builder.setPositiveButton("Change", (d, w) -> {
            String oldp = oldPwd.getText().toString();
            String np1 = newPwd.getText().toString();
            String np2 = newPwd2.getText().toString();

            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!user.getPassword().equals(oldp)) {
                Toast.makeText(this, "Current password incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!np1.equals(np2)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (np1.length() < 5) {
                Toast.makeText(this, "Password must be at least 5 chars", Toast.LENGTH_SHORT).show();
                return;
            }

            // Persist password change to DB
            user.setPassword(np1);
            userRepository.updateUser(user.getEmail(), user);

            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());
        builder.show();
    }
}
