package com.jce.medapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jce.medapp.adapters.CustomRecyclerAdapter;
import com.jce.medapp.adapters.notData;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserNotification extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Bundle bundle;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        bundle = getIntent().getExtras();
        username=  bundle.getString("name","");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.notification_recycler);
        List<notData> list = new ArrayList<>();
        list=getData();
        adapter=new CustomRecyclerAdapter(list,getApplication(),username);



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNotification.this));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh,menu);
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
            adapter=new CustomRecyclerAdapter(list,getApplication(),username);



            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(UserNotification.this));
            Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private List<notData> getData()
    {
        final List<notData> list = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("HospitalNotifications");
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
