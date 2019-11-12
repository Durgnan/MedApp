package com.jce.medapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jce.medapp.adapters.AmbRecyclerAdapter;
import com.jce.medapp.adapters.CustomRecyclerAdapter;
import com.jce.medapp.adapters.notData;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceMain extends AppCompatActivity {
    RecyclerView recyclerView;
    AmbRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Bundle bundle;
    String username;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref=getSharedPreferences("user_details",MODE_PRIVATE);
        username = pref.getString("username","");
        recyclerView = findViewById(R.id.ambulance_recycler);
        List<notData> list = new ArrayList<>();
        list=getData();
        adapter=new AmbRecyclerAdapter(list,getApplication(),username);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AmbulanceMain.this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.refresh)
        {
            Toast.makeText(this,"Refreshing...",Toast.LENGTH_LONG).show();
            List<notData> list = new ArrayList<>();
            list=getData();
            adapter=new AmbRecyclerAdapter(list,getApplication(),username);



            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(AmbulanceMain.this));
            Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();
        }
        else if (id==R.id.log)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(AmbulanceMain.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private List<notData> getData()
    {
        final List<notData> list = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("AmbuNoti");
        query.whereEqualTo("Status","");
        ParseObject val;

        try {
            List<ParseObject> value = query.find();
            Log.e("",""+value.size());
            for (int i=0;i<value.size();i++)
            {

                val= value.get(i);
                list.add(new notData(val.getString("PatientName"),val.getString("Case"),val.getString("PatientID")));

            }


        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        return list;
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
