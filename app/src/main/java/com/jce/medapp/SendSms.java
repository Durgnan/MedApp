package com.jce.medapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

public class SendSms extends AppCompatActivity implements View.OnClickListener {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        button=findViewById(R.id.send);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String num = "+918310336673";
        String message="Hello Bhosri Wale";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(num,null,message,null,null);
    }
}
