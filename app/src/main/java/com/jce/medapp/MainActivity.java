package com.jce.medapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {
    Button b1,b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createObject();
        b1=findViewById(R.id.track_ambulance);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("","Hello");
                Intent i=new Intent(MainActivity.this,HospitalMain.class);
                startActivity(i);
            }
        });
        b2 = findViewById(R.id.track_patient);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this,HospitalMain.class);
                startActivity(j);
            }
        });
    }
    public void createObject() {
        String user = "nitinsabale59@gmail.com";
        String pass = "nitin";

        ParseObject myNewObject = new ParseObject("DemoTB");
        myNewObject.put("username", user);
        myNewObject.put("password", pass);

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        myNewObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Here you can handle errors, if thrown. Otherwise, "e" should be null
                if (e!=null)
                    Log.e("ERROR",e.getMessage());
                else
                    Log.e("STATUS","SUCCESS");
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        if (id==R.id.notification)
        {
            i = new Intent(MainActivity.this,UserNotification.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}

