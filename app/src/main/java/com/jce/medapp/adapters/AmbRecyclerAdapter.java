package com.jce.medapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jce.medapp.R;

import java.util.Collections;
import java.util.List;

public class AmbRecyclerAdapter extends RecyclerView.Adapter<AmbViewHolder> {
    List<notData> list = Collections.emptyList();
    Context context;
    int i=0;
    String username;
    public AmbRecyclerAdapter(List<notData> list, Context context, String username)
    {
        this.list=list;
        this.context=context;
        this.username=username;
    }
    @NonNull
    @Override
    public AmbViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.notification_card,viewGroup,false);
        AmbViewHolder viewHolder = new AmbViewHolder(photoView,username,this.context);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AmbViewHolder ambViewHolder, int i) {
        ambViewHolder.pname.setText(list.get(i).pname);
        ambViewHolder.pcase.setText(list.get(i).pcase);
        ambViewHolder.id.setText(list.get(i).pid);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
