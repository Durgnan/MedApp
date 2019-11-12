package com.jce.medapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jce.medapp.adapters.AmbulanceListAdapter;
import com.jce.medapp.adapters.ListAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DeleteAmbulance extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String[] id;
    ListView listView;
    AmbulanceListAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    TextView textView;
    Animation animation;
    SharedPreferences pref;
    Bundle bundle;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_ambulance);
        bundle = getIntent().getExtras();
//        username=  bundle.getString("Name","");
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        username=pref.getString("username","");
        pullToRefresh = findViewById(R.id.pullToRefresh2);
        textView = findViewById(R.id.fading);
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
    public void getEntries()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("type","ambulance");
        ParseObject val;
        try
        {
            List<ParseObject> value = query.find();
            id=new String[value.size()];
            for (int i=0;i<value.size();i++) {
                val= value.get(i);
                id[i]=val.getString("email");
            }
            listView = findViewById(R.id.linview);
            adapter = new AmbulanceListAdapter(this,id);
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
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("email",this.id[position]);
        ParseObject val;
        StringBuffer buffer=new StringBuffer();
        buffer.append("Do You want to Delete the Ambulance ?");
        try
        {
            List<ParseObject> value = query.find();
            val= value.get(0);
            final String did = val.getObjectId();

            builder.setMessage(buffer).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    query.getInBackground(did, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null)
                                object.deleteInBackground();
                        }
                    });

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

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

