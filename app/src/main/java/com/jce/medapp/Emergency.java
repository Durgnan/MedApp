package com.jce.medapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Emergency extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3;
    Bundle bundle;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        bundle=getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        b1 = findViewById(R.id.self);
        b2  =findViewById(R.id.friends);
        b3 = findViewById(R.id.unknown);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.self)
        {
            startActivity(new Intent(Emergency.this,EmergencyChoice.class).putExtra("username",bundle.getString("username")));
        }
        if (v.getId() == R.id.friends)


        {
            selectFriend(bundle.getString("username"));

        }
        if (v.getId() == R.id.unknown)
        {
            startActivity(new Intent(Emergency.this,EmergencyChoice.class).putExtra("username",bundle.getString("username")));
        }
    }
    public void selectFriend(String uname)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
        query.whereEqualTo("userEmail", uname);

        try {
            List<ParseObject> value = query.find();
            final String[] friends=new String[value.size()];
            final String[] fmail=new String[value.size()];
            String[] fname = new String[value.size()];

            if (value.size()<1)
                Toast.makeText(getApplicationContext(),"No Friends Added",Toast.LENGTH_SHORT).show();
            else
            {
                for (int i=0;i<value.size();i++)
                {
                 friends[i]=value.get(i).getString("name");
                 fmail[i]=value.get(i).getString("email");
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(Emergency.this);
                builder.setTitle("Select Friend");
                builder.setItems(friends, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Fmail",fmail[which]);
                        startActivity(new Intent(Emergency.this,EmergencyChoice.class).putExtra("username",fmail[which]).putExtra("name",friends[which]));
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        catch (ParseException e)
        {

        }
    }
}
