package com.jce.medapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {
    Button addambu,remambu,addhos,remhosp;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addambu=findViewById(R.id.addambu);
        remambu=findViewById(R.id.remambu);
        addhos=findViewById(R.id.addhos);
        remhosp=findViewById(R.id.remhos);
        pref=getSharedPreferences("user_details",MODE_PRIVATE);

        addambu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Admin.this,AddAmbulance.class));

            }
        });

        remambu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, DeleteAmbulance.class));
            }
        });

        addhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this,AddHospital.class));

            }
        });

        remhosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, DeleteHospital.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id== R.id.logu)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(Admin.this, Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
