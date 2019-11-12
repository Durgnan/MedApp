package com.jce.medapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jce.medapp.Utility.DatabaseHelper;
import com.jce.medapp.model.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    EditText etECEmail,etECName,etECPhone,etFEmail,etFName,etFPhone,etMHText,etMHDate;
    Button btnAddEC,btnAddF,btnAddMH,btnViewEC,btnViewF,btnViewMH,btnDate;
    SharedPreferences pref;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        final String USEREMAIL = pref.getString("username","");




        etECEmail = findViewById(R.id.etECEmail);
        etECName = findViewById(R.id.etECName);
        etECPhone = findViewById(R.id.etECPhone);
        btnAddEC = findViewById(R.id.btnAddEC);

        etFEmail = findViewById(R.id.etFEmail);
        etFName = findViewById(R.id.etFName);
        etFPhone = findViewById(R.id.etFPhone);
        btnAddF = findViewById(R.id.btnAddF);

        etMHText = findViewById(R.id.etMHtext);
        btnAddMH = findViewById(R.id.btnAddMH);
        etMHDate = findViewById(R.id.etDate);

        btnViewEC = findViewById(R.id.btnViewEC);
        btnViewF = findViewById(R.id.btnViewF);
        btnViewMH = findViewById(R.id.btnViewMH);


        btnDate = findViewById(R.id.btnDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year,month,day );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();


            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                etMHDate.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };




        btnAddEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etECEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    etECName.setError("Enter Email");
                    etECName.requestFocus();
                    return;
                }
                String name = etECName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    etECName.setError("Enter Name");
                    etECName.requestFocus();
                    return;
                }
                String phone = etECPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    etECPhone.setError("Enter Phone Number");
                    etECPhone.requestFocus();
                    return;
                }

                if(isAPEmergencyContacts(USEREMAIL,name))
                {
                    Toast.makeText(getApplicationContext(),"Emergency Contact Already Present",Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseObject addECObject = new ParseObject("EmergencyContacts");
                    addECObject.put("userEmail", USEREMAIL);
                    addECObject.put("email", email);
                    addECObject.put("name", name);
                    addECObject.put("phone", phone);


                    addECObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // Here you can handle errors, if thrown. Otherwise, "e" should be null
                            if (e != null) {
                                Log.e("ERROR", e.getMessage());
                                Toast.makeText(getApplication(), "Data Not Inserted", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("STATUS", "SUCCESS");
                                Toast.makeText(getApplication(), "Data Inserted", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


        btnViewEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuffer buffer = new StringBuffer();

                SpannableString s ;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("EmergencyContacts");
                query.whereEqualTo("userEmail",USEREMAIL);
                ParseObject val;
                try
                {
                    List<ParseObject> value = query.find();

                    for (int i=0;i<value.size();i++) {
                        val = value.get(i);
                        buffer.append("\tName : "+val.getString("name")+"\n");
                        buffer.append("\tEmail : "+val.getString("email")+"\n");
                        buffer.append("\tPhone : "+val.getString("phone")+"\n\n");
                    }

                }
                catch (ParseException e)
                {
                    e.getMessage();
                }
                s = new SpannableString(buffer);
                Linkify.addLinks(s,Linkify.PHONE_NUMBERS);

                showMessage("Emergency Contacts",s);
            }
        });



        btnAddF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etFEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    etECName.setError("Enter Email");
                    etECName.requestFocus();
                    return;
                }
                String name = etFName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    etFName.setError("Enter Name");
                    etFName.requestFocus();
                    return;
                }
                String phone = etFPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    etFPhone.setError("Enter Phone Number");
                    etFPhone.requestFocus();
                    return;
                }

                if(isAPFriends(USEREMAIL,name))
                {
                    Toast.makeText(getApplicationContext(),"Friend Already Present",Toast.LENGTH_SHORT).show();
                }
                else {

                    ParseObject addFObject = new ParseObject("Friends");
                    addFObject.put("userEmail", USEREMAIL);
                    addFObject.put("email", email);
                    addFObject.put("name", name);
                    addFObject.put("phone", phone);


                    addFObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // Here you can handle errors, if thrown. Otherwise, "e" should be null
                            if (e != null) {
                                Log.e("ERROR", e.getMessage());
                                Toast.makeText(getApplication(), "Data Not Inserted", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("STATUS", "SUCCESS");
                                Toast.makeText(getApplication(), "Data Inserted", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }



            }
        });

        btnViewF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer buffer = new StringBuffer();

                SpannableString s ;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                query.whereEqualTo("userEmail",USEREMAIL);
                ParseObject val;
                try
                {
                    List<ParseObject> value = query.find();

                    for (int i=0;i<value.size();i++) {
                        val = value.get(i);
                        buffer.append("\tName : "+val.getString("name")+"\n");
                        buffer.append("\tEmail : "+val.getString("email")+"\n");
                        buffer.append("\tPhone : "+val.getString("phone")+"\n\n");
                    }

                }
                catch (ParseException e)
                {
                    e.getMessage();
                }
                s = new SpannableString(buffer);
                Linkify.addLinks(s,Linkify.PHONE_NUMBERS);

                showMessage("Friends",s);
            }
        });


        btnAddMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = etMHDate.getText().toString().trim();
                String text = etMHText.getText().toString().trim();
                if(TextUtils.isEmpty(text)){
                    etMHText.setError("Enter Disease");
                    etMHText.requestFocus();
                    return;
                }


                //Insertion in Parse Database
                ParseObject addMHObject = new ParseObject("MedicalHistory");
                addMHObject.put("userEmail", USEREMAIL);
                addMHObject.put("date", date);
                addMHObject.put("disease",text);

                addMHObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Here you can handle errors, if thrown. Otherwise, "e" should be null
                        if (e != null ){
                            Log.e("ERROR", e.getMessage());
                            Toast.makeText(getApplication(),"Data Not Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.e("STATUS", "SUCCESS");
                            Toast.makeText(getApplication(),"Data Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        btnViewMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer buffer = new StringBuffer();

                SpannableString s ;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MedicalHistory");
                query.whereEqualTo("userEmail",USEREMAIL);
                ParseObject val;
                try
                {
                    List<ParseObject> value = query.find();

                    for (int i=0;i<value.size();i++) {
                        val = value.get(i);
                        buffer.append("\tDate : "+val.getString("date")+"\n");
                        buffer.append("\tDisease : "+val.getString("disease")+"\n\n");
                    }

                }
                catch (ParseException e)
                {
                    e.getMessage();
                }
                s = new SpannableString(buffer);
                Linkify.addLinks(s,Linkify.PHONE_NUMBERS);

                showMessage("Medical History",s);
            }
        });

    }
    public void showMessage(String title,SpannableString Message){
        TextView message = new TextView(getApplicationContext());
        message.setText(Message);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setView(message);
        builder.create();

        builder.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    boolean isAPEmergencyContacts(String email,String name){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("EmergencyContacts");
        query.whereEqualTo("userEmail", email);
        query.whereEqualTo("name", name);

        try {
            List<ParseObject> value = query.find();
            if (value.size() == 1)
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    boolean isAPFriends(String email,String name){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
        query.whereEqualTo("userEmail", email);
        query.whereEqualTo("name", name);

        try {
            List<ParseObject> value = query.find();
            if (value.size() == 1)
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

}