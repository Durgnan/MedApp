package com.jce.medapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.jce.medapp.Utility.Utility.checkEmail;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister,btnLogin;
    EditText etEmail,etPassword;
    String email,password;
    String getType,getEmail,getPassword,getName,getPhone;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail =findViewById(R.id.etEmail);
        etPassword =findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        if (Build.VERSION.SDK_INT>=23)
        {
            if (checkPermission())
            {}
            else
            {
                requestPermission();
            }
        }

        autologin();
        btnLogin.setOnClickListener(this);
        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);

            }
        });
    }
    public void autologin(){

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("username") && pref.contains("password")){
            String type = pref.getString("type","");

            if (type.equals(new String("admin"))) {


                startActivity(new Intent(Login.this, Admin.class).putExtra("Name",getEmail));
                finish();
            } else if (type.equals(new String("user"))) {

                startActivity(new Intent(Login.this, UserMain.class).putExtra("Name",getEmail));
                finish();
            }
            else if (type.equals(new String("ambulance"))) {

                startActivity(new Intent(Login.this, AmbulanceMain.class)
                        .putExtra("email",getEmail));
                finish();
            }
            else if (type.equals(new String("hospital"))) {

                startActivity(new Intent(Login.this, HospitalMain.class)
                        .putExtra("email",getEmail));
                finish();
            }




        }

    }
    public void doLogin()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("email", email);
        query.whereEqualTo("password",password);
        try {
            List<ParseObject> value = query.find();
            if (value.size() == 1) {
                ParseObject val = value.get(0);
                String objectId = val.getObjectId();
                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject result, ParseException e) {
                        if (e == null) {

                            // Details
                            getType = result.get("type").toString().trim();
                            getName = result.get("username").toString().trim();
                            getPassword = result.get("password").toString().trim();
                            getEmail = result.get("email").toString().trim();
                            getPhone = result.get("phone").toString().trim();


                                   // Autologin Data
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("username",getEmail);
                                    editor.putString("password",getPassword);
                                    editor.putString("type",getType);
                                    editor.putString("pname",getName);
                                    editor.commit();



                            if (getType.equals(new String("hospital"))) {


                                startActivity(new Intent(Login.this, HospitalMain.class).putExtra("Name",getEmail));
                                finish();
                            }
                            else if (getType.equals(new String("user"))) {

                                startActivity(new Intent(Login.this, UserMain.class)
                                        .putExtra("Name",getEmail)
                                );
                                finish();
                            }
                            else if (getType.equals(new String("ambulance"))) {

                                startActivity(new Intent(Login.this, AmbulanceMain.class)
                                        .putExtra("email",getEmail)
                                );
                                finish();
                            }
                            else if (getType.equals(new String("admin"))) {

                                startActivity(new Intent(Login.this, Admin.class)
                                        .putExtra("email",getEmail)
                                );
                                finish();
                            }
                        }

                        else {

                        }
                    }
                });
            }
            else
                Toast.makeText(getApplicationContext(),"Login Unsuccessful \n Try Again",Toast.LENGTH_LONG).show();


        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter the Email");
            etEmail.requestFocus();
            return;
        }
        if (!checkEmail(email)) {
            etEmail.setError("Invalid Email Format");
            etEmail.requestFocus();
            return;
        }
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter the password");
            etPassword.requestFocus();
            return;
        }
        doLogin();

    }
    public boolean checkPermission()
    {
        int Coarse= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int fine= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int sms = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
        return  Coarse== PackageManager.PERMISSION_GRANTED &&
                fine==PackageManager.PERMISSION_GRANTED && sms==PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(Login.this,new String[]
                {
                  Manifest.permission.ACCESS_COARSE_LOCATION,
                  Manifest.permission.ACCESS_FINE_LOCATION,
                  Manifest.permission.SEND_SMS
                },1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    boolean fine=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean coarse=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean sms=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if (fine && coarse && sms)
                        ;
                    else
                        Log.e("TAG","Please give permissions");
                }
                break;
        }
    }
}

