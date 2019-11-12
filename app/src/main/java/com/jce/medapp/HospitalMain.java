package com.jce.medapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jce.medapp.adapters.ListAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class HospitalMain extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String[] id,name,pcase;
    ListView listView;
    ListAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    TextView textView;
    Animation animation;
    SharedPreferences pref;
    Bundle bundle;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_main);
        bundle = getIntent().getExtras();
        username=  bundle.getString("Name","");
        Log.e("EE",username);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        username=pref.getString("username","");
        pullToRefresh = findViewById(R.id.pullToRefresh);
        textView = findViewById(R.id.fade_in);
        animation= AnimationUtils.loadAnimation(this,R.anim.fade);
        textView.startAnimation(animation);
        getEntries();
        pullToRefresh.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // Toast.makeText(getApplicationContext(),"refreshing...",Toast.LENGTH_LONG).show();
                getEntries();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);

                    }
                },5000);

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
            i = new Intent(HospitalMain.this,UserNotification.class).putExtra("name",username);
            startActivity(i);
        }
        if (id==R.id.logout)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(HospitalMain.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getEntries()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("HospitalNotifications");
        query.whereEqualTo("Status",username);
        ParseObject val;
        try
        {
            List<ParseObject> value = query.find();
            id=new String[value.size()];
            name=new String[value.size()];
            pcase=new String[value.size()];
            for (int i=0;i<value.size();i++) {
                val= value.get(i);
                id[i]=val.getString("PatientID");
                name[i]=val.getString("PatientName");
                pcase[i]=val.getString("Case");
            }
            listView = findViewById(R.id.lview);
            adapter = new ListAdapter(this,id,name,pcase);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            }
        catch (ParseException e)
        {
            e.getMessage();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.myDialog));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MedicalHistory");
        query.whereEqualTo("userEmail",this.id[position]);
        final String pid = this.id[position];
        ParseObject val;
        StringBuffer buffer=new StringBuffer();

        try
        {
            List<ParseObject> value = query.find();
            for (int i=0;i<value.size();i++) {
                val= value.get(i);
                buffer.append(val.getString("date")+":\t"+val.getString("disease")+"\n");
            }
          builder.setMessage(buffer).setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
              //  startActivity(new Intent(HospitalMain.this,MapsActivityPatient.class).putExtra("driver",username).putExtra("patient",pid));
              }
          }) ;
            builder.create();
            builder.show();
        }
        catch (ParseException e)
        {
            e.getMessage();
        }


    }
}
