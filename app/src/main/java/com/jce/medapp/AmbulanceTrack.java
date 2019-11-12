package com.jce.medapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jce.medapp.Utility.DatabaseHelper;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class AmbulanceTrack extends AppCompatActivity implements View.OnClickListener {
    Bundle bundle;
    Button b1;
  //  Boolean flag = false;
    String username, cases, Hstatus,Astatus;
    ProgressBar progressBar;
    SharedPreferences pref;
    DatabaseHelper myDb;
    LocationManager locationManager;
    String[] nums;
    Boolean ambulance=false,hospital=false;
    String ambid,hosid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_track);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDb = new DatabaseHelper(this);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        bundle = getIntent().getExtras();
        username = pref.getString("username","");
        cases = bundle.getString("case");
        b1 = findViewById(R.id.track);
        progressBar = findViewById(R.id.progress);

        b1.setOnClickListener(this);
        b1.setEnabled(true);
        Log.e("Message", username + cases);
        doAllotment();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.track) {
            //doAllotment();
             checkForAllotmentAmbulance();
             checkForAllotmentHospital();
             Log.e("TAG","AL: "+Astatus);
             if (hospital==true && ambulance==true)
             {
                 Toast.makeText(getApplicationContext(),"Allotted",Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(AmbulanceTrack.this,MapsActivityAmbulance.class)
                         .putExtra("patient",username).putExtra("driver",Astatus));
             }
             else if (ambulance==true)
             {
                 Toast.makeText(getApplicationContext(),"Ambulance Allotted",Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(AmbulanceTrack.this,MapsActivityAmbulance.class)
                       .putExtra("patient",username).putExtra("driver",Astatus));
             }
             else
             {
                 Toast.makeText(getApplicationContext(),"Not Yet Allotted",Toast.LENGTH_SHORT).show();
             }

        }


    }

    private void doAllotment() {
        final ParseObject myNewObject = new ParseObject("AmbuNoti");
        if (bundle.getString("name")=="" || bundle.getString("name")==null)
        {
            myNewObject.put("PatientName", pref.getString("pname", ""));
            myNewObject.put("Case", cases);
            myNewObject.put("Status", "");
            myNewObject.put("PatientID", pref.getString("username", ""));
        }
        else
        {
            myNewObject.put("PatientName", bundle.getString("name"));
            myNewObject.put("Case", cases);
            myNewObject.put("Status", "");
            myNewObject.put("PatientID", bundle.getString("username"));
        }




        myNewObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                {
                    Log.e("ERROR", e.getMessage());

                }

                else
                {
                    Log.e("STATUS", "H:"+myNewObject.getObjectId());
                    ambid=myNewObject.getObjectId();
            }}
        });
         final ParseObject myNewObject1 = new ParseObject("HospitalNotifications");
        if (bundle.getString("name")=="" || bundle.getString("name")==null)
        {
            myNewObject1.put("PatientName", pref.getString("pname", ""));
            myNewObject1.put("Case", cases);
            myNewObject1.put("Status", "");
            myNewObject1.put("PatientID", pref.getString("username", ""));
        }
        else
        {
            myNewObject1.put("PatientName", bundle.getString("name"));
            myNewObject1.put("Case", cases);
            myNewObject1.put("Status", "");
            myNewObject1.put("PatientID", bundle.getString("username"));
        }

        myNewObject1.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.e("ERROR", e.getMessage());
                else{
                    Log.e("STATUS", "H: "+myNewObject1.getObjectId());
                    hosid=myNewObject1.getObjectId();
                }

            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,locationListener);

            }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(locationListener);
            final ParseGeoPoint currentLoc = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLocation");
            if (bundle.getString("username")=="" || bundle.getString("username")==null)
                query.whereEqualTo("Username", username);
            else
                query.whereEqualTo("Username", bundle.getString("username"));

            try {
                List<ParseObject> value = query.find();
                ParseObject val = value.get(0);
                String id = val.getObjectId();
                query.getInBackground(id, new GetCallback<ParseObject>() {
                    public void done(ParseObject entity, ParseException e) {
                        if (e == null) {
                            // Update the fields we want to
                            entity.put("Location", currentLoc);

                            // All other fields will remain the same
                            entity.saveInBackground();

                        }
                    }
                });
            }
            catch (ParseException e) {
                e.printStackTrace();
            }


            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("EmergencyContacts");
            query1.whereEqualTo("userEmail",bundle.getString("username") );

            try {
                List<ParseObject> value = query1.find();
                nums = new String[value.size()];

                if (value.size() < 1)
                    Toast.makeText(getApplicationContext(), "No Friends Added", Toast.LENGTH_SHORT).show();
                else {
                    for (int i=0;i<value.size();i++)
                        nums[i]=value.get(i).getString("phone");
                }
            }
            catch (ParseException e)
            {

            }

                sendMessages(nums);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private void checkForAllotmentAmbulance()
    {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("AmbuNoti");
                    try {
                        Log.e("TAG",""+ambid);
                            query.getInBackground(ambid, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        // Update the fields we want to

                                        if (object.getString("Status")!="")
                                        {
                                          //  flag=true;
                                            Astatus= object.getString("Status");
                                            ambulance=true;
//                                            startActivity(new Intent(AmbulanceTrack.this,MapsActivityAmbulance.class)
//                                                    .putExtra("patient",username).putExtra("driver",status));


                                        }

                                        // All other fields will remain the same
                                        object.saveInBackground();

                                    }
                                }
                            });





                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }

                }
    public void checkForAllotmentHospital()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("HospitalNotifications");
        query.whereEqualTo("objectId",hosid);
        try {
                query.getInBackground(hosid, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // Update the fields we want to
                           Hstatus= object.getString("Status");
                            if (Hstatus!="")
                            {
                              //  flag=true;
                                hospital=true;
//                                startActivity(new Intent(AmbulanceTrack.this,MapsActivityAmbulance.class)
//                                        .putExtra("patient",username).putExtra("driver",status));


                            }

                            // All other fields will remain the same
                            object.saveInBackground();

                        }
                    }
                });





        }
        catch (Exception e)
        {
            e.getMessage();
        }

    }



   public void sendMessages(String[] num)
   {
       SmsManager smsManager = SmsManager.getDefault();
       if (bundle.getString("name")=="" ||bundle.getString("name")==null)
       {
           for (String n : num)
           {
               smsManager.sendTextMessage(n,null,"This is emergency call that your contact "+pref.getString("pname","")+" needs your help\n  \n Reason:"+cases,null,null);
           }
           smsManager.sendTextMessage(getResources().getString(R.string.police),null,"This is to inform that one of the citizen "+pref.getString("pname","")+" has got emergency because of "+cases,null,null);
           smsManager.sendTextMessage(getResources().getString(R.string.forest),null,"This is to inform that one of the citizen "+pref.getString("pname","")+" has got emergency because of "+cases,null,null);
           smsManager.sendTextMessage(getResources().getString(R.string.electricity),null,"This is to inform that one of the citizen "+pref.getString("pname","")+" has got emergency because of "+cases,null,null);
       }
       else
       {
           for (String n : num)
           {
               smsManager.sendTextMessage(n,null,"This is emergency call that your contact "+bundle.getString("name")+" needs your help\n  \n Reason:"+cases,null,null);
           }
           smsManager.sendTextMessage(getResources().getString(R.string.police),null,"This is to inform that one of the citizen "+bundle.getString("name")+" has got emergency because of "+cases,null,null);
           smsManager.sendTextMessage(getResources().getString(R.string.forest),null,"This is to inform that one of the citizen "+bundle.getString("name")+" has got emergency because of "+cases,null,null);
           smsManager.sendTextMessage(getResources().getString(R.string.electricity),null,"This is to inform that one of the citizen "+bundle.getString("name")+" has got emergency because of "+cases,null,null);
       }

   }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
