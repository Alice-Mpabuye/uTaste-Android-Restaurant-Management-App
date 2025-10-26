package com.example.utaste.ui;

import android.annotation.SuppressLint;
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

public class ChefActivity extends AppCompatActivity {

    private Button btnLogout, btnChangePassword, btnCreateRecipe, btnMyRecipes;
    private String currentChefEmail = "chef@local";
    private UserRepository userRepository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        userRepository = UserRepository.getInstance();
        userRepository.init(getApplicationContext());

        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnCreateRecipe = findViewById(R.id.btnCreateRecipe);
        btnMyRecipes = findViewById(R.id.btnMyRecipes);

        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            currentChefEmail = getIntent().getStringExtra("userEmail");
        }

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(ChefActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(currentChefEmail));

        btnCreateRecipe.setOnClickListener(v -> {
            Intent n = new Intent(ChefActivity.this, CreateRecipeActivity.class);
            startActivity(n);
        });

        btnMyRecipes.setOnClickListener(v -> {
            Intent t = new Intent(ChefActivity.this, RecipeListActivity.class);
            startActivity(t);
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

            user.setPassword(np1);
            userRepository.updateUser(user.getEmail(), user);

            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());
        builder.show();
    }
}
