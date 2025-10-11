package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;

public class WaiterActivity extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        // Initialize logout button
        btnLogout = new Button(this);
        btnLogout.setText("Logout");

        // Optionally, add it to the existing layout
        LinearLayout layout = findViewById(R.id.waiterLayout); // make sure your root layout has this id
        layout.addView(btnLogout);

        // Logout functionality
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WaiterActivity.this, LoginActivity.class);
                startActivity(i);
                finish(); // close WaiterActivity
            }
        });
    }
}
