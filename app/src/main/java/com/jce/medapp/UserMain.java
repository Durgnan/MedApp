package com.jce.medapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jce.medapp.model.User;

public class UserMain extends AppCompatActivity {
    Button b1, b2;
    SharedPreferences pref;
    Bundle bundle;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        bundle = getIntent().getExtras();
        pref=getSharedPreferences("user_details", MODE_PRIVATE);
        b1 = findViewById(R.id.emergency);
        b2 = findViewById(R.id.nemergency);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserMain.this, Emergency.class);
                i.putExtra("username", bundle.getString("Name"));
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmm = Uri.parse("geo:0,0?q=hospitals+%26+clinics");
                Intent map = new Intent(Intent.ACTION_VIEW,gmm);
                map.setPackage("com.google.android.apps.maps");
                startActivity(map);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.settings)
            startActivity(new Intent(UserMain.this,UserActivity.class).putExtra("Name",bundle.getString("Name")));
        else if (item.getItemId()==R.id.logout) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(UserMain.this,Login.class));
            finish();


        }
        return super.onOptionsItemSelected(item);
    }
}
