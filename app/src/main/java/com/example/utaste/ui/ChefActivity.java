package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;

public class ChefActivity extends AppCompatActivity {

    private Button btnLogout, btnChangePassword;

    // If you want the real user email, get it from intent extras
    private String currentChefEmail = "chef@local";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(ChefActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(currentChefEmail));
    }

    private void showChangePasswordDialog(String userEmail) {
        // reuse the implementation from AdminActivity but adapted
        com.example.utaste.ui.AdminActivity helper = null;
        // Rather than duplicating complex dialog code, implement simple inline:

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50,20,50,10);

        final android.widget.EditText oldPwd = new android.widget.EditText(this);
        oldPwd.setHint("Current password");
        oldPwd.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(oldPwd);

        final android.widget.EditText newPwd = new android.widget.EditText(this);
        newPwd.setHint("New password");
        newPwd.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPwd);

        final android.widget.EditText newPwd2 = new android.widget.EditText(this);
        newPwd2.setHint("Confirm new password");
        newPwd2.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPwd2);

        builder.setTitle("Change password");
        builder.setView(layout);
        builder.setPositiveButton("Change", (d, w) -> {
            String oldp = oldPwd.getText().toString();
            String np1 = newPwd.getText().toString();
            String np2 = newPwd2.getText().toString();

            User user = UserRepository.getInstance().findByEmail(userEmail);
            if (user == null) { android.widget.Toast.makeText(this,"User not found",android.widget.Toast.LENGTH_SHORT).show(); return; }
            if (!user.getPassword().equals(oldp)) { android.widget.Toast.makeText(this,"Current password incorrect",android.widget.Toast.LENGTH_SHORT).show(); return; }
            if (!np1.equals(np2)) { android.widget.Toast.makeText(this,"New passwords do not match",android.widget.Toast.LENGTH_SHORT).show(); return; }
            if (np1.length() < 5) { android.widget.Toast.makeText(this,"Password must be at least 5 chars",android.widget.Toast.LENGTH_SHORT).show(); return; }

            user.setPassword(np1);
            android.widget.Toast.makeText(this,"Password changed",android.widget.Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (d,w)-> d.dismiss());
        builder.show();
    }
}
