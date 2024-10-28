package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupTextView;

    // Firebase Authentication instance
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Find the fields by their IDs
        emailEditText = findViewById(R.id.enterEmailLogin);
        passwordEditText = findViewById(R.id.enterPasswordLogin);
        loginButton = findViewById(R.id.login_Button);
        signupTextView = findViewById(R.id.signUp_Button_Textview);

        // Set onClickListener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Set onClickListener for the Signup TextView
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // Function to handle user login
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Simple validation
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Please enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Please enter password");
            return;
        }

        // Firebase Authentication for login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Navigate to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If login fails
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(LoginActivity.this, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
