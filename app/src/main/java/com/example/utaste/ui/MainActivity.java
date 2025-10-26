package com.example.utaste.ui;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.data.UserRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize repository ONCE for the app
        UserRepository.init(getApplicationContext());

        // Redirect to login screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // so user can't go back here
    }
}
