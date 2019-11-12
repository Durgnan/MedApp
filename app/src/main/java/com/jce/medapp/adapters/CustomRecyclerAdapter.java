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

public class CustomRecyclerAdapter extends RecyclerView.Adapter<NotViewHolder> {
    List<notData> list = Collections.emptyList();
    Context context;
    int i=0;
    String username;
    public CustomRecyclerAdapter(List<notData> list, Context context,String username)
    {
        this.list=list;
        this.context=context;
        this.username=username;
         }
    @Override
    public NotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.notification_card,viewGroup,false);
        NotViewHolder viewHolder = new NotViewHolder(photoView,username);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotViewHolder notViewHolder, int i) {
        notViewHolder.pname.setText(list.get(i).pname);
        notViewHolder.pcase.setText(list.get(i).pcase);
        notViewHolder.id.setText(list.get(i).pid);

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
