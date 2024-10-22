package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page); // Linking to the signup_page.xml layout

        // Find the signup button and input fields by their IDs
        Button signupButton = findViewById(R.id.signUp_button);
        EditText nameEditText = findViewById(R.id.nameSignUp_EditText);
        EditText mobileNoEditText = findViewById(R.id.mobileNoSignUp_EditText);
        EditText emailEditText = findViewById(R.id.emailSignUp_EditText);
        EditText usernameEditText = findViewById(R.id.usernameSignUp_EditText);
        EditText passwordEditText = findViewById(R.id.passwordSignUp_EditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordSignUp_EditText);

        // Set an OnClickListener on the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String name = nameEditText.getText().toString().trim();
                String mobileNo = mobileNoEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Simple validation
                if (name.isEmpty() || mobileNo.isEmpty() || email.isEmpty() || username.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // If validation passes, navigate to MainActivity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close SignupActivity to prevent returning to it on back press
                }
            }
        });
    }
}
