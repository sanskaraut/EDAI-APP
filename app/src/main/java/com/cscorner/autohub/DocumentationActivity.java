package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DocumentationActivity extends AppCompatActivity {

    private EditText welcomeText;
    private ImageView drivingLicenseCheckmark, aadharCardCheckmark, panCardCheckmark, rcDetailsCheckmark, profilePhotoCheckmark;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documentation); // Link to the documentation layout

        welcomeText = findViewById(R.id.welcomeText);

        // Checkmark ImageViews
        drivingLicenseCheckmark = findViewById(R.id.imageView12); // Driving License checkmark
        aadharCardCheckmark = findViewById(R.id.imageView15);     // Aadhaar Card checkmark
        panCardCheckmark = findViewById(R.id.imageView18);        // PAN Card checkmark
        rcDetailsCheckmark = findViewById(R.id.imageView19);      // RC Details checkmark
        profilePhotoCheckmark = findViewById(R.id.imageView20);   // Profile Photo checkmark

        db = FirebaseFirestore.getInstance();

        // Fetch user data and document statuses
        fetchUserName();
        checkDocumentStatus();

        // Set click listeners for each document TextView
        setClickListeners();
    }

    private void fetchUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Assuming you have a Realtime Database with user information
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

    // Check the Firestore for all document statuses and show the corresponding checkmarks
    private void checkDocumentStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Fetch from Firestore
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {

                                // Check Driving License Status
                                String drivingLicenseImage = document.getString("drivingLicenseImage");
                                if (drivingLicenseImage != null && !drivingLicenseImage.isEmpty()) {
                                    drivingLicenseCheckmark.setVisibility(View.VISIBLE);
                                } else {
                                    drivingLicenseCheckmark.setVisibility(View.GONE);
                                }

                                // Check Aadhaar Card Status
                                String aadhaarCardImage = document.getString("aadhaarImageUrl");
                                if (aadhaarCardImage != null && !aadhaarCardImage.isEmpty()) {
                                    aadharCardCheckmark.setVisibility(View.VISIBLE);
                                } else {
                                    aadharCardCheckmark.setVisibility(View.GONE);
                                }

                                // Check PAN Card Status
                                String panCardImage = document.getString("panCardImage");
                                if (panCardImage != null && !panCardImage.isEmpty()) {
                                    panCardCheckmark.setVisibility(View.VISIBLE);
                                } else {
                                    panCardCheckmark.setVisibility(View.GONE);
                                }

                                // Check RC Details Status
                                String rcDetailsImage = document.getString("rcDetailsImage");
                                if (rcDetailsImage != null && !rcDetailsImage.isEmpty()) {
                                    rcDetailsCheckmark.setVisibility(View.VISIBLE);
                                } else {
                                    rcDetailsCheckmark.setVisibility(View.GONE);
                                }

                                // Check Profile Photo Status
                                String profilePhotoImage = document.getString("profilePhoto");
                                if (profilePhotoImage != null && !profilePhotoImage.isEmpty()) {
                                    profilePhotoCheckmark.setVisibility(View.VISIBLE);
                                } else {
                                    profilePhotoCheckmark.setVisibility(View.GONE);
                                }

                            } else {
                                // Hide all checkmarks if document doesn't exist
                                drivingLicenseCheckmark.setVisibility(View.GONE);
                                aadharCardCheckmark.setVisibility(View.GONE);
                                panCardCheckmark.setVisibility(View.GONE);
                                rcDetailsCheckmark.setVisibility(View.GONE);
                                profilePhotoCheckmark.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(DocumentationActivity.this, "Failed to check document status", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        findViewById(R.id.doneButton).setOnClickListener(v -> {
            Intent intent = new Intent(DocumentationActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}