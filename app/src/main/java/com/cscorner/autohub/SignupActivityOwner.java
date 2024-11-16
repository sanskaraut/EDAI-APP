package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivityOwner extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance
    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_signup); // Linking to the owner_signup.xml layout

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                // Retrieve owner input
                String name = nameEditText.getText().toString().trim();
                String mobileNo = mobileNoEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Simple validation
                if (name.isEmpty() || mobileNo.isEmpty() || email.isEmpty() || username.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivityOwner.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivityOwner.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Check for existing user info
                    checkExistingOwner(email, username, mobileNo, password, name);
                }
            }
        });
    }

    // Function to check for existing owner info (email, username, mobileNo)
    private void checkExistingOwner(String email, String username, String mobileNo, String password, String name) {
        // Check if email exists
        db.collection("owners")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Toast.makeText(SignupActivityOwner.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            // If email doesn't exist, check username
                            db.collection("owners")
                                    .whereEqualTo("username", username)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                Toast.makeText(SignupActivityOwner.this, "Username already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // If username doesn't exist, check mobile number
                                                db.collection("WashingCenterOwner")
                                                        .whereEqualTo("mobileNo", mobileNo)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                    Toast.makeText(SignupActivityOwner.this, "Mobile number already in use", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // All checks passed, proceed with registration
                                                                    registerOwner(email, password, username, mobileNo, name);
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    // Function to register an owner in Firebase
    private void registerOwner(String email, String password, String username, String mobileNo, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Owner registration success, save to Firestore
                            String ownerId = mAuth.getCurrentUser().getUid(); // Get owner ID
                            saveOwnerToFirestore(ownerId, name, username, mobileNo, email);
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(SignupActivityOwner.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Function to save owner data to Firestore
    private void saveOwnerToFirestore(String ownerId, String name, String username, String mobileNo, String email) {
        Map<String, Object> owner = new HashMap<>();
        owner.put("name", name);
        owner.put("username", username);
        owner.put("mobileNo", mobileNo);
        owner.put("email", email);
        owner.put("role", "owner");  // Set role to "owner" for owners

        // Save the owner details to the "owners" collection in Firestore
        db.collection("WashingCenterOwners").document(ownerId)
                .set(owner)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Redirect to the owner dashboard or another activity after successful registration
                            Intent intent = new Intent(SignupActivityOwner.this, MainActivityOwner.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivityOwner.this, "Failed to save owner details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
