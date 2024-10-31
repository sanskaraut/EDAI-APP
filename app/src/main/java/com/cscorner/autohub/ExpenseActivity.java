package com.cscorner.autohub;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpenseActivity extends AppCompatActivity {

    private ImageView addImage;
    private TextView fuelExpenseView, tollFineExpenseView, maintenanceExpenseView, miscellaneousExpenseView, totalExpenseView, currentMonthExpenseView;
    private ImageButton refetchButton; // Button for refetching data
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_page);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        addImage = findViewById(R.id.imageButton2);
        fuelExpenseView = findViewById(R.id.fuelAmount);
        tollFineExpenseView = findViewById(R.id.tollFineAmount);
        maintenanceExpenseView = findViewById(R.id.maintainanceAmount);
        miscellaneousExpenseView = findViewById(R.id.miscellaneousAmount);
        totalExpenseView = findViewById(R.id.totalExpenseAmount);
        currentMonthExpenseView = findViewById(R.id.currentMonthExpenseAmount);
        refetchButton = findViewById(R.id.refetchDataFromDatabase); // Initialize refetch button

        // Check if user is logged in and fetch the email
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            fetchUserDocument(email);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        addImage.setOnClickListener(v -> openAddExpenseDialog());

        // Set the click listener for the refetch button
        refetchButton.setOnClickListener(v -> fetchExpenseData());
    }

    private void fetchUserDocument(String email) {
        db.collection("users").whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        userRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                        fetchExpenseData();
                    } else {
                        Toast.makeText(this, "No document found for this user", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ExpenseActivity", "Error fetching user document", e);
                    Toast.makeText(this, "Failed to find user document", Toast.LENGTH_LONG).show();
                });
    }

    private void fetchExpenseData() {
        if (userRef != null) {
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    int fuelExpense = documentSnapshot.contains("fuelExpense") ? documentSnapshot.getLong("fuelExpense").intValue() : 0;
                    int tollFineExpense = documentSnapshot.contains("tollFineExpense") ? documentSnapshot.getLong("tollFineExpense").intValue() : 0;
                    int maintenanceExpense = documentSnapshot.contains("maintenanceExpense") ? documentSnapshot.getLong("maintenanceExpense").intValue() : 0;
                    int miscExpense = documentSnapshot.contains("miscellaneousExpense") ? documentSnapshot.getLong("miscellaneousExpense").intValue() : 0;

                    // Calculate total expense
                    int totalExpense = fuelExpense + tollFineExpense + maintenanceExpense + miscExpense;

                    // Get the total expense from Firestore
                    int totalExpenseFromFirestore = documentSnapshot.contains("totalExpense") ? documentSnapshot.getLong("totalExpense").intValue() : 0;

                    // Set values to TextViews
                    fuelExpenseView.setText(String.valueOf(fuelExpense));
                    tollFineExpenseView.setText(String.valueOf(tollFineExpense));
                    maintenanceExpenseView.setText(String.valueOf(maintenanceExpense));
                    miscellaneousExpenseView.setText(String.valueOf(miscExpense));
                    totalExpenseView.setText(String.valueOf(totalExpenseFromFirestore)); // Display total expense from Firestore
                    currentMonthExpenseView.setText(String.valueOf(totalExpense)); // Display total of individual expenses
                } else {
                    Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("ExpenseActivity", "Error fetching data", e);
                Toast.makeText(this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            Toast.makeText(this, "User document not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAddExpenseDialog() {
        Dialog dialog = new Dialog(ExpenseActivity.this);
        dialog.setContentView(R.layout.expense_dialogbox);

        EditText edtAmount = dialog.findViewById(R.id.edtAmount);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        Button btnAction = dialog.findViewById(R.id.btnAction);

        String[] categories = {"Fuel", "Maintenance", "Toll / Fine", "Miscellaneous"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);

        btnAction.setOnClickListener(v1 -> {
            String amountText = edtAmount.getText().toString();
            String category = spinner.getSelectedItem().toString();

            if (!amountText.isEmpty()) {
                int amount = Integer.parseInt(amountText);
                updateExpenseInFirebase(category, amount);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void updateExpenseInFirebase(String category, int amount) {
        if (userRef == null) {
            Toast.makeText(this, "User document not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        String fieldToUpdate = "";
        switch (category) {
            case "Fuel":
                fieldToUpdate = "fuelExpense";
                break;
            case "Maintenance":
                fieldToUpdate = "maintenanceExpense";
                break;
            case "Toll / Fine":
                fieldToUpdate = "tollFineExpense";
                break;
            case "Miscellaneous":
                fieldToUpdate = "miscellaneousExpense";
                break;
        }

        if (!fieldToUpdate.isEmpty()) {
            String finalFieldToUpdate = fieldToUpdate;
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                int currentExpense = documentSnapshot.contains(finalFieldToUpdate) ? documentSnapshot.getLong(finalFieldToUpdate).intValue() : 0;
                int updatedExpense = currentExpense + amount;

                userRef.update(finalFieldToUpdate, updatedExpense)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                            fetchExpenseData(); // Refresh data after update
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ExpenseActivity", "Failed to update expense", e);
                            Toast.makeText(this, "Failed to update expense: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            });
        }
    }
}
