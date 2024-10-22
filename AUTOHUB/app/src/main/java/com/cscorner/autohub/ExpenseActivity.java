package com.cscorner.autohub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_page); // Linking expense_page.xml
    }
}
