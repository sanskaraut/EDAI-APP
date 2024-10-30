package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DocumentationActivity extends AppCompatActivity {

    private EditText welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documentation); // Linking to documentation.xml

        welcomeText = findViewById(R.id.welcomeText);

        // Fetch user data from Firebase
        fetchUserName();

        // Set click listeners for document TextViews
        setClickListeners();
    }

    private void fetchUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("name").getValue(String.class);
                        if (userName != null) {
                            welcomeText.setText("Welcome " + userName);
                        } else {
                            Toast.makeText(DocumentationActivity.this, "User name not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DocumentationActivity.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DocumentationActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClickListeners() {
        findViewById(R.id.drivingLicense_TextView).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, DrivingLicenseActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.aadharCard_TextView).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, AADHARCardActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.panCard_TextView).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, PANCardActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.RC_Details_TextView).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, RCDetailsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.profilePhoto_TextView).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
