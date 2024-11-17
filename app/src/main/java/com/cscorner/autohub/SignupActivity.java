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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance
    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page); // Linking to the signup_page.xml layout

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the signup button and input fields by their IDs
        Button signupButton = findViewById(R.id.signUp_button);
        EditText nameEditText = findViewById(R.id.nameSignUp_EditText);
        EditText mobileNoEditText = findViewById(R.id.mobileNoSignUp_EditText);
        EditText emailEditText = findViewById(R.id.emailSignUp_EditText);
        EditText usernameEditText = findViewById(R.id.addressSignUp_EditText);
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
                    // Check for existing user info
                    checkExistingUser(email, username, mobileNo, password, name);
                }
            }
        });
    }

    // Function to check for existing user info (email, username, mobileNo)
    private void checkExistingUser(String email, String username, String mobileNo, String password, String name) {
        // Check if email exists
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            // If email doesn't exist, check username
                            db.collection("users")
                                    .whereEqualTo("username", username)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                Toast.makeText(SignupActivity.this, "Username already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // If username doesn't exist, check mobile number
                                                db.collection("users")
                                                        .whereEqualTo("mobileNo", mobileNo)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                    Toast.makeText(SignupActivity.this, "Mobile number already in use", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // All checks passed, proceed with registration
                                                                    registerUser(email, password, username, mobileNo, name);
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

    // Function to register a user in Firebase
    private void registerUser(String email, String password, String username, String mobileNo, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration success, save to Firestore
                            String userId = mAuth.getCurrentUser().getUid(); // Get user ID
                            saveUserToFirestore(userId, name, username, mobileNo, email);
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Function to save user data to Firestore
    private void saveUserToFirestore(String userId, String name, String username, String mobileNo, String email) {
        // Create a User object with default role "user"
        String role = determineUserRole(username);  // Determine the role based on username or other criteria
        User user = new User(name, username, mobileNo, email, role);

        // Save the User object to Firestore under the user's unique ID
        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    // Perform additional setup for new users
                    initializeUserSettings(userId);

                    // Notify user of success and proceed to next activity
                    Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, DocumentVerificationActivity.class);
                    startActivity(intent);
                    finish(); // Close SignupActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Function to determine user role
    private String determineUserRole(String username) {
        // Example: Assign "owner" role if username contains 'admin', else "user"
        if (username.contains("admin")) {
            return "owner";
        } else {
            return "user";
        }
    }

    // Additional Function to Initialize User Settings
    private void initializeUserSettings(String userId) {
        // Example: Add an empty expense tracker for the user
        Map<String, Object> expenseData = new HashMap<>();
        expenseData.put("fuelExpense", 0);
        expenseData.put("tollFineExpense", 0);
        expenseData.put("maintenanceExpense", 0);
        expenseData.put("miscellaneousExpense", 0);
        expenseData.put("totalCurrentMonthExpense", 0);
        expenseData.put("totalExpense", 0);
    }
}
