package com.example.comp4200_group20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    ArrayList<DataSet> dataList;
    Context context;
    DBHelper dbHelper;
    private int expandedPosition = -1;
    public RecAdapter(ArrayList<DataSet> data, Context context){
        this.dataList = data;
        this.context = context;
        this.dbHelper = new DBHelper(context, "test_database", null, 1);
    }

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

        boolean isExpanded = (position == expandedPosition);
        holder.expandedSection.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (isExpanded) {
            Cursor cursor = dbHelper.getDataFromTitle(data.getTitle());
            if (cursor != null && cursor.moveToFirst()) {
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                holder.ingredientsText.setText(ingredients != null ? ingredients : "No ingredients listed.");
                holder.instructionsText.setText(instructions != null ? instructions : "No instructions listed.");
                cursor.close();
            }
        }

        // Regular click: toggle expand/collapse
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAbsoluteAdapterPosition();

                if (expandedPosition == currentPosition) {
                    // Collapse the currently expanded card
                    expandedPosition = -1;
                    notifyItemChanged(currentPosition);
                } else {
                    // Collapse the previously expanded card, expand the new one
                    int previousExpanded = expandedPosition;
                    expandedPosition = currentPosition;
                    if (previousExpanded != -1) {
                        notifyItemChanged(previousExpanded);
                    }
                    notifyItemChanged(currentPosition);
                }
            }
        });

        // Long click: navigate to edit screen
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, AddEditActivity.class);
                i.putExtra("title", data.getTitle());
                context.startActivity(i);
                return false;
            }
        });

        // Delete button click
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAbsoluteAdapterPosition();
                boolean deleted = dbHelper.deleteData(data.getTitle());
                if (deleted) {
                    // Reset expanded state if the deleted card was expanded
                    if (expandedPosition == currentPosition) {
                        expandedPosition = -1;
                    } else if (expandedPosition > currentPosition) {
                        // Shift expanded index down since an item above it was removed
                        expandedPosition--;
                    }
                    dataList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, dataList.size());
                } else {
                    Toast.makeText(context, "Failed to delete recipe.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Export button click
        holder.exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getDataFromTitle(data.getTitle());
                if (cursor == null || !cursor.moveToFirst()) {
                    Toast.makeText(context, "Could not load recipe data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title       = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                cursor.close();

                String content =
                        "Recipe:\n" + title + "\n\n" +
                                "Description:\n" + description + "\n\n" +
                                "Ingredients:\n" + ingredients + "\n\n" +
                                "Instructions:\n" + instructions + "\n";

                // Save directly to the public Downloads folder
                String safeTitle = title.replaceAll("[^a-zA-Z0-9_\\-]", "_");
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File exportFile = new File(downloadsDir, safeTitle + ".txt");

                try (FileWriter writer = new FileWriter(exportFile)) {
                    writer.write(content);
                    Toast.makeText(context, "Saved to Downloads: " + exportFile.getName(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(context, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
        TextView ingredientsText;
        TextView instructionsText;
        CardView cardView;
        View expandedSection;
        Button deleteButton;
        Button exportButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descText = itemView.findViewById(R.id.descText);
            cardView = itemView.findViewById(R.id.cardView);
            expandedSection = itemView.findViewById(R.id.expandedSection);
            ingredientsText = itemView.findViewById(R.id.ingredientsText);
            instructionsText = itemView.findViewById(R.id.instructionsText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            exportButton = itemView.findViewById(R.id.exportButton);
        }
    }
}
