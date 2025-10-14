package com.example.utaste.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;
import com.example.utaste.util.Validators;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    private Button btnCreateWaiter, btnResetDB, btnManageProfiles, btnLogout, btnChangePassword;
    private ListView waiterListView;
    private ArrayAdapter<String> adapter;
    private String currentAdminEmail = "admin@local"; // hardcoded for demo

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnCreateWaiter = findViewById(R.id.btnCreateWaiter);
        btnResetDB = findViewById(R.id.btnResetDB);
        btnManageProfiles = findViewById(R.id.btnManageProfiles);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        waiterListView = findViewById(R.id.waiterListView);

        // Setup list adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        waiterListView.setAdapter(adapter);
        refreshWaiterList();

        btnCreateWaiter.setOnClickListener(v -> showCreateWaiterDialog());
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(currentAdminEmail));

        waiterListView.setOnItemClickListener((parent, view, position, id) -> {
            List<User> waiters = UserRepository.getInstance().listWaiters();
            if (position < 0 || position >= waiters.size()) return;
            User selected = waiters.get(position);
            showWaiterOptionsDialog(selected);
        });
    }

    // 🔹 Updated: show creation + modification dates in list
    private void refreshWaiterList() {
        adapter.clear();
        List<User> waiters = UserRepository.getInstance().listWaiters();
        for (User w : waiters) {
            String info = "Email: " + w.getEmail() +
                    (w.getFirstName() != null && !w.getFirstName().isEmpty() ? " — " + w.getFirstName() : "") + "\n" +
                    "Created: " + dateFormat.format(w.getCreatedAt()) + "\n" +
                    "Modified: " + dateFormat.format(w.getModifiedAt());
            adapter.add(info);
        }
        adapter.notifyDataSetChanged();
    }

    private void showCreateWaiterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Waiter");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email");
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setHint("First Name (optional)");
        layout.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setHint("Last Name (optional)");
        layout.addView(lastNameInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);
        builder.setPositiveButton("Create", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (password.isEmpty()) password = "waiter-pwd";

            String validationError = Validators.validateNewUser(email, password);
            if (validationError != null) {
                Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show();
                return;
            }

            User newWaiter = new User(email, password, User.Role.WAITER);
            newWaiter.setFirstName(firstName);
            newWaiter.setLastName(lastName);

            boolean added = UserRepository.getInstance().addUser(newWaiter);
            if (!added) {
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Waiter created successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refreshWaiterList();
            }
        });
    }

    private void showWaiterOptionsDialog(User waiter) {
        CharSequence[] options = {"Edit", "Delete", "Cancel"};
        new AlertDialog.Builder(this)
                .setTitle(waiter.getEmail())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showEditWaiterDialog(waiter);
                    else if (which == 1) confirmAndDeleteWaiter(waiter);
                    else dialog.dismiss();
                })
                .show();
    }

    private void confirmAndDeleteWaiter(User waiter) {
        new AlertDialog.Builder(this)
                .setTitle("Delete waiter")
                .setMessage("Are you sure you want to delete " + waiter.getEmail() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    boolean ok = UserRepository.getInstance().deleteUser(waiter.getEmail());
                    if (ok) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        refreshWaiterList();
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditWaiterDialog(User waiter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Waiter");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setText(waiter.getEmail());
        layout.addView(emailInput);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setText(waiter.getFirstName() == null ? "" : waiter.getFirstName());
        layout.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setText(waiter.getLastName() == null ? "" : waiter.getLastName());
        layout.addView(lastNameInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("New Password (leave blank to keep)");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newEmail = emailInput.getText().toString().trim();
            String newFirst = firstNameInput.getText().toString().trim();
            String newLast = lastNameInput.getText().toString().trim();
            String newPass = passwordInput.getText().toString().trim();

            String passToValidate = newPass.isEmpty() ? waiter.getPassword() : newPass;
            String validation = Validators.validateNewUser(newEmail, passToValidate);
            if (validation != null) {
                Toast.makeText(this, validation, Toast.LENGTH_SHORT).show();
                return;
            }

            User updated = new User(newEmail, passToValidate, waiter.getRole());
            updated.setFirstName(newFirst);
            updated.setLastName(newLast);
            updated.setCreatedAt(waiter.getCreatedAt());
            updated.touchModified();

            boolean ok = UserRepository.getInstance().updateUser(waiter.getEmail(), updated);
            if (!ok) {
                Toast.makeText(this, "Email already exists or update failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refreshWaiterList();
            }
        });
    }

    private void showChangePasswordDialog(String userEmail) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,20,50,10);

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

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Change password")
                .setView(layout)
                .setPositiveButton("Change", null)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String oldp = oldPwd.getText().toString();
            String np1 = newPwd.getText().toString();
            String np2 = newPwd2.getText().toString();

            User user = UserRepository.getInstance().findByEmail(userEmail);
            if (user == null) { Toast.makeText(this,"User not found",Toast.LENGTH_SHORT).show(); return; }
            if (!user.getPassword().equals(oldp)) { Toast.makeText(this,"Current password incorrect",Toast.LENGTH_SHORT).show(); return; }
            if (!np1.equals(np2)) { Toast.makeText(this,"New passwords do not match",Toast.LENGTH_SHORT).show(); return; }
            if (np1.length() < 5) { Toast.makeText(this,"Password must be at least 5 chars",Toast.LENGTH_SHORT).show(); return; }

            user.setPassword(np1); // touchModified inside setter
            Toast.makeText(this,"Password changed",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
}
