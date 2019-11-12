package com.jce.medapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jce.medapp.Utility.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class AddHospital extends AppCompatActivity {
    EditText etName,etPhone,etEmail,etPassword;
    Button btnRegister;
    String username,password,phoneNumber,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etName.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    etName.setError("Enter the name");
                    etName.requestFocus();
                    return;
                }
                phoneNumber = etPhone.getText().toString().trim();
                if(!Utility.checkMobileNo(phoneNumber))
                {
                    etPhone.setError("Enter the PhoneNumber");
                    etPhone.requestFocus();
                    return;
                }
                email = etEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Enter the Email");
                    etEmail.requestFocus();
                    return;
                }
                if(!Utility.checkEmail(email)){
                    etEmail.setError("Invalid Email Format");
                    etEmail.requestFocus();
                    return;
                }
                password = etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Enter the password");
                    etPassword.requestFocus();
                    return;
                }
                if(!Utility.checkPassword(password))
                {
                    etPassword.setError("Should contain uppercase,lowercase and specialcase characters");
                    etPassword.requestFocus();
                    return;
                }




                if(isAlreadyPresent(email))
                {
                    Toast.makeText(getApplication(),"Email Already Registered", Toast.LENGTH_LONG).show();
                }
                else {


                    ParseObject registerObject = new ParseObject("Users");
                    registerObject.put("username", username);
                    registerObject.put("password", password);
                    registerObject.put("phone", phoneNumber);
                    registerObject.put("email", email);
                    registerObject.put("type", "hospital");


                    registerObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // Here you can handle errors, if thrown. Otherwise, "e" should be null
                            if (e != null)
                                Log.e("ERROR", e.getMessage());
                            else {
                                Log.e("STATUS", "SUCCESS");
                                Toast.makeText(getApplication(),"Registration Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddHospital.this, Admin.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }


            }
        });
    }
    boolean isAlreadyPresent(String email){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("email", email);

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

