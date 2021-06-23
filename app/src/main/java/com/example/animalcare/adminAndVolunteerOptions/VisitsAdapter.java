package com.example.animalcare.adminAndVolunteerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalcare.CRUD.VolunteersAdapter;
import com.example.animalcare.R;
import com.example.animalcare.models.Visit;

import java.util.ArrayList;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitsViewHolder>{
    private ArrayList<Visit> visits;
    private OnItemClickListener onItemClickListener;

    public VisitsAdapter(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    @NonNull
    @Override
    public VisitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_item, parent, false);
        VisitsViewHolder visitsViewHolder = new VisitsViewHolder(view, onItemClickListener);
        return visitsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsViewHolder holder, int position) {
        Visit currentVisit = visits.get(position);

        String adopterName = currentVisit.getAdopterUsername();
        String date = "Date: " + currentVisit.getDay() + "." + currentVisit.getMonth() + "." + currentVisit.getYear();
        String time = "Time: " + currentVisit.getHour() + ":" + currentVisit.getMinutes();

        holder.nameTV.setText(adopterName);
        holder.dateTV.setText(date);
        holder.timeTV.setText(time);
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(VisitsAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class VisitsViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV, dateTV, timeTV;
        public ImageView img;

        public VisitsViewHolder(@NonNull View itemView, VisitsAdapter.OnItemClickListener listener) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.visit_item_adopter_name);
            dateTV = itemView.findViewById(R.id.visit_item_date);
            timeTV = itemView.findViewById(R.id.visit_item_time);
            img = itemView.findViewById(R.id.visit_item_img);

            img.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
