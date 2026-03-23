package com.example.comp4200_group20;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    ArrayList<DataSet> dataList;
    Context context;
    public RecAdapter(ArrayList<DataSet> data, Context context){this.dataList = data; this.context = context;}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataSet data = dataList.get(position);
        holder.titleText.setText(data.getTitle());
        holder.descText.setText(data.getDescription());
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, AddEditActivity.class);
                i.putExtra("title", data.getTitle());
                context.startActivity(i);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleText;
        TextView descText;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descText = itemView.findViewById(R.id.descText);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
