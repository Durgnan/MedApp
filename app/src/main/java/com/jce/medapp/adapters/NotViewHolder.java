package com.jce.medapp.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jce.medapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class NotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView pname,pcase,id;
    ImageButton accept,reject;
    String username;
    Boolean status=false;
    public NotViewHolder( View itemView,String username) {
        super(itemView);
        pname = (TextView) itemView.findViewById(R.id.patient_name);
        pcase = (TextView) itemView.findViewById(R.id.patient_case);
        id = (TextView)itemView.findViewById(R.id.id);
        accept = (ImageButton) itemView.findViewById(R.id.baccept);
        reject = (ImageButton)itemView.findViewById(R.id.breject);
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
            this.username=username;


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==accept.getId())
        {
            updateAllocationStatus((String) id.getText());
            reject.setEnabled(false);
            accept.setEnabled(false);
            reject.setVisibility(View.INVISIBLE);
            accept.setVisibility(View.INVISIBLE);

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
        Log.e("abc", String.valueOf(id1));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("HospitalNotifications");
        query.whereEqualTo("PatientID",id1);
        query.whereEqualTo("Status","");
        try
        {
            List<ParseObject> value = query.find();
            ParseObject val = value.get(0);
            String id = val.getObjectId();
            query.getInBackground(id, new GetCallback<ParseObject>() {
                public void done(ParseObject entity, ParseException e) {
                    if (e == null) {
                        // Update the fields we want to
                        entity.put("Status",username );

                        // All other fields will remain the same
                        entity.saveInBackground();

                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e("",e.getMessage());
        }

    }
}
