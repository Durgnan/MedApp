package com.jce.medapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jce.medapp.MapsActivityPatient;
import com.jce.medapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AmbViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView pname,pcase,id;
    ImageButton accept,reject;
    String username;
    Context context;
    Boolean status=false;
    public AmbViewHolder(View itemView, String username, Context context)  {
        super(itemView);
        pname = (TextView) itemView.findViewById(R.id.patient_name);
        pcase = (TextView) itemView.findViewById(R.id.patient_case);
        id = (TextView)itemView.findViewById(R.id.id);
        accept = (ImageButton) itemView.findViewById(R.id.baccept);
        reject = (ImageButton)itemView.findViewById(R.id.breject);
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);
        SharedPreferences preferences = context.getSharedPreferences("user_details", MODE_PRIVATE);

        this.username=preferences.getString("username","");
        this.context=context;


    }
    @Override
    public void onClick(View v) {
        if (v.getId()==accept.getId())
        {
            updateAllocationStatus((String) id.getText());
            Log.e("TTy",username+" "+id.getText());
            reject.setEnabled(false);
            accept.setEnabled(false);
            reject.setVisibility(View.INVISIBLE);
            accept.setVisibility(View.INVISIBLE);

            Intent i= new Intent(v.getContext(),MapsActivityPatient.class).putExtra("driver",username).putExtra("patient",id.getText());
            v.getContext().startActivity(i);

        }
        else if (v.getId()==reject.getId())
        {
            accept.setBackgroundColor(Color.WHITE);


            accept.setEnabled(false);
            reject.setEnabled(false);
            reject.setVisibility(View.INVISIBLE);
            accept.setVisibility(View.INVISIBLE);
        }

    }
    public void updateAllocationStatus(String id1)
    {
        Log.e("abc", username+id1);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AmbuNoti");
        query.whereEqualTo("PatientID",id1);
        query.whereEqualTo("Status","");
        try
        {
            List<ParseObject> value = query.find();
            ParseObject val = value.get(0);
            String id = val.getObjectId();
            Log.e("TAG",id);
            query.getInBackground(id, new GetCallback<ParseObject>() {
                public void done(ParseObject entity, ParseException e) {
                    if (e == null) {
                        // Update the fields we want to
                        Log.e("TAG",username+"");
                        entity.put("Status",username );

                        // All other fields will remain the same
                        entity.saveInBackground();

                    }
                    else ;
                }
            });
        }
        catch (Exception e)
        {
            Log.e("",e.getMessage());
        }
        Intent intent = new Intent(context, MapsActivityPatient.class);
       // Start start = new Start(intent,username,id1);

    }
}
class Start extends Activity
{
    public Start(Intent intent,String username,String pid)
    {
     startActivity(intent.putExtra("driver",username).putExtra("patient",pid));
    }
}