package com.example.comp4200_group20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    ArrayList<DataSet> dataList;
    public RecAdapter(ArrayList<DataSet> data){this.dataList = data;}

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
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleText;
        TextView descText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descText = itemView.findViewById(R.id.descText);
        }
    }
}
