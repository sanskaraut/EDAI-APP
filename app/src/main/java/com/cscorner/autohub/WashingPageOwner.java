package com.cscorner.autohub;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class WashingPageOwner extends AppCompatActivity{

    TextView cancelAppointments , viewAppointments , viewRequest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_washing_page); // Linking washing_center_detail.xml

        cancelAppointments = findViewById(R.id.textView40);
        viewAppointments =  findViewById(R.id.textView52);
        viewRequest  = findViewById(R.id.textView51);
    }


}