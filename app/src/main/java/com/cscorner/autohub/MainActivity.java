package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton goToWashingPage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page); // Linking main_page.xml

        // Get reference to the ImageButton for expenseManager
        ImageButton expenseManagerButton = findViewById(R.id.expenseManager);

        // Set an onClickListener to redirect to ExpenseActivity
        expenseManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to go to ExpenseActivity
                Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                startActivity(intent);
        goToWashingPage = findViewById(R.id.imageButton5);

        goToWashingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToWashingPage = new Intent(MainActivity.this , WashingPage.class);
                startActivity(goToWashingPage);

            }
        });
    }
}
