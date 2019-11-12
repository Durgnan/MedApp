package com.jce.medapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmergencyChoice extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4,b5;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_choice);
        bundle=getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b1 = findViewById(R.id.animal);
        b2=findViewById(R.id.accident);
        b3=findViewById(R.id.heart);
        b4=findViewById(R.id.fire);
        b5=findViewById(R.id.electric);
        b1.setOnClickListener(this);b2.setOnClickListener(this);b3.setOnClickListener(this);b4.setOnClickListener(this);b5.setOnClickListener(this);

    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.animal:
                if (bundle.getString("name")=="")
                    startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                            .putExtra("case","Animal Bite")
                            .putExtra("username",bundle.getString("username")).putExtra("name",""));
                else

                startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                    .putExtra("case","Animal Bite").putExtra("name",bundle.getString("name"))
                    .putExtra("username",bundle.getString("username")));
                break;
            case R.id.accident:
                if (bundle.getString("name")=="")

                    startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                    .putExtra("case","Accident").putExtra("name","")
                    .putExtra("username",bundle.getString("username")));
                else
                    startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                            .putExtra("case","Accident").putExtra("name",bundle.getString("name"))
                            .putExtra("username",bundle.getString("username")));
                break;
            case R.id.heart:
                if (bundle.getString("name")=="")
                startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                    .putExtra("case","Heart/Cardiac Arrest").putExtra("name","")
                    .putExtra("username",bundle.getString("username")));
                else
                startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                        .putExtra("case","Heart/Cardiac Arrest").putExtra("name",bundle.getString("name"))
                        .putExtra("username",bundle.getString("username")));
                break;
            case R.id.fire:
                if (bundle.getString("name")=="")
                startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                    .putExtra("case","Fire Injury").putExtra("name","")
                    .putExtra("username",bundle.getString("username")));
                else
                    startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                            .putExtra("case","Fire Injury").putExtra("name",bundle.getString("name"))
                            .putExtra("username",bundle.getString("username")));
                break;
            case R.id.electric:
                if (bundle.getString("name")=="")
                startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                    .putExtra("case","Electric Incident").putExtra("name","")
                    .putExtra("username",bundle.getString("username")));
                else
                    startActivity(new Intent(EmergencyChoice.this,AmbulanceTrack.class)
                            .putExtra("case","Electric Incident").putExtra("name",bundle.getString("name"))
                            .putExtra("username",bundle.getString("username")));
                break;
        }

    }
}
