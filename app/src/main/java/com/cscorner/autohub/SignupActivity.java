package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page); // Linking to the signup_page.xml layout

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                    // Proceed with Firebase Authentication registration
                    registerUser(email, password);
                }
            }
        });
    }

    // Function to register a user in Firebase
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration success, move to the MainActivity
                            Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close SignupActivity
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
