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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivityOwner extends AppCompatActivity {

    private EditText enterEmail, enterPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_login); // use the same layout as loginActivity, just different logic

        // Initialize FirebaseAuth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Find the fields by their IDs
        enterEmail = findViewById(R.id.enterEmailLogin);
        enterPassword = findViewById(R.id.enterPasswordLogin);
        Button loginBtn = findViewById(R.id.login_Button);
        TextView signupBtn = findViewById(R.id.signUp_Button_Textview);
        TextView areYouAUser = findViewById(R.id.areYouAUser);

        // Set onClickListener for the Login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOwner();
            }
        });

        // Set onClickListener for the Signup TextView
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity for Owners
                Intent intent = new Intent(LoginActivityOwner.this, SignupActivityOwner.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the "Are you a User?" TextView
        areYouAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to User Login
                Intent intent = new Intent(LoginActivityOwner.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Function to handle owner login
    private void loginOwner() {
        String email = enterEmail.getText().toString().trim();
        String password = enterPassword.getText().toString().trim();

        // Simple validation
        if (TextUtils.isEmpty(email)) {
            enterEmail.setError("Please enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            enterPassword.setError("Please enter password");
            return;
        }

        // Firebase Authentication for login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Check if the user is an owner
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        firestore.collection("WashingCenterOwners").document(userId)
                                .get()
                                .addOnCompleteListener(roleTask -> {
                                    if (roleTask.isSuccessful()) {
                                        DocumentSnapshot document = roleTask.getResult();
                                        if (document.exists()) {
                                            String role = document.getString("role");
                                            if ("owner".equals(role)) {
                                                // Role matches, navigate to Owner Dashboard
                                                Toast.makeText(LoginActivityOwner.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivityOwner.this, MainActivityOwner.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Role mismatch
                                                firebaseAuth.signOut();
                                                Toast.makeText(LoginActivityOwner.this, "Access denied: You are not an owner", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            firebaseAuth.signOut();
                                            Toast.makeText(LoginActivityOwner.this, "User data not found", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivityOwner.this, "Error checking user role", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivityOwner.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
