package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class WashingPage extends AppCompatActivity{

    TextView Cardetails ,MakeAppointment;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.washing_page); // Linking washing_center_detail.xml

        Cardetails = findViewById(R.id.textView35);
        MakeAppointment = findViewById(R.id.textView51);

        MakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toWashingCentre ;
                toWashingCentre = new Intent(WashingPage.this , WashingCenterActivity.class) ;
                startActivity(toWashingCentre) ;

            }
        });

        }


    }


