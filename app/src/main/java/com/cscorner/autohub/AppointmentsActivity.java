package com.cscorner.autohub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AppointmentsActivity extends AppCompatActivity {

    TextView CarWashingAtDoorstep , PickUp ,CarWashingAppointment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);
        TextView CarWashingAtDoorstep , PickUp ,CarWashingAppointment ;

        CarWashingAtDoorstep  = findViewById(R.id.textView53);
        PickUp = findViewById(R.id.textView54);
        CarWashingAppointment = findViewById(R.id.textView55);

        CarWashingAtDoorstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toWashingCentre ;
                toWashingCentre = new Intent(AppointmentsActivity.this , AvailableWashingCentersActivity.class) ;
                startActivity(toWashingCentre) ;
            }
        });

    }
}
