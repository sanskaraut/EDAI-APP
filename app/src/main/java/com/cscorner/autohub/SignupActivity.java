package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page); // Linking signup_page.xml

        Button signupButton = findViewById(R.id.signUp_button); // Assuming button id is signup_button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // After successful signup, navigate to MainActivity
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close signup activity
            }
        });
    }
}
