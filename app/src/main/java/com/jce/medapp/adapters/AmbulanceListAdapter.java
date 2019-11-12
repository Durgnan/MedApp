package com.jce.medapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jce.medapp.R;

public class AmbulanceListAdapter extends BaseAdapter {
    public String[] id1;
    public Activity context;
    public LayoutInflater inflater;
    public AmbulanceListAdapter(Activity context,String id1[]) {
        super();
        this.context = context;
        this.id1 = id1;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

        @Override
    public int getCount() {
        return id1.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder {
        TextView id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v==null)
        {
            holder=new ViewHolder();
            v = inflater.inflate(R.layout.ambulance_list,null);
            holder.id = v.findViewById(R.id.did);
            v.setTag(holder);

        }
        else
            holder=(ViewHolder) v.getTag();
        holder.id.setText(id1[position]);
        return v;
    }


    }
