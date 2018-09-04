package com.jeong_woochang.findng_airpod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jeong-woochang on 2018. 8. 27..
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    ArrayList<Log> items;

    public LogAdapter(){
        items=new ArrayList<>();
    }
    public LogAdapter(ArrayList<Log> items){
        this.items=items;
    }

    @NonNull
    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view=(LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, final int position) {
        final LinearLayout layout=holder.itemview;
        final TextView name=layout.findViewById(R.id.name);
        final TextView state=layout.findViewById(R.id.state);
        final TextView time=layout.findViewById(R.id.time);
        final TextView lat=layout.findViewById(R.id.lat);
        final TextView lng=layout.findViewById(R.id.lng);
        final Log item = items.get(position);

        name.setText(item.getName());
        state.setText(item.getState());
        time.setText(item.getTime());
        lat.setText(item.getLat());
        lng.setText(item.getLng());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(), MapActivity.class);
                intent.putExtra("Lat", item.getLat());
                intent.putExtra("Lng", item.getLng());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemview;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            itemview=itemView;
        }
    }

    public void addItem(Log item){
        items.add(item);
    }

    public void addItem(String name, String state, String time, String lat, String lng){
        Log temp=new Log(name, state, time, lat, lng);
        items.add(temp);
    }
}
