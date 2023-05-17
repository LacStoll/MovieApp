package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Get a reference to the welcome button
        Button welcomeButton = findViewById(R.id.welcomeButton);

        // Set a listener for the welcome button
        welcomeButton.setOnClickListener(view -> {
            // When the welcome button is clicked, start the main activity
            Intent intent = new Intent(Welcome.this, MainActivity.class);
            startActivity(intent);

            // Finish the welcome activity to prevent the user from going back to it
            finish();
        });
    }
}