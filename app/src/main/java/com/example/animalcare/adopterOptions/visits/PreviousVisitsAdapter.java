package com.example.animalcare.adopterOptions.visits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animalcare.R;
import com.example.animalcare.adminAndVolunteerOptions.VisitsAdapter;
import com.example.animalcare.models.Visit;

import java.util.ArrayList;

import static com.example.animalcare.models.Visit.STATUS_OFF;

public class PreviousVisitsAdapter extends RecyclerView.Adapter<PreviousVisitsAdapter.PreviousVisitsViewHolder>{
    private ArrayList<Visit> visits;
    private PreviousVisitsAdapter.OnItemClickListener onItemClickListener;

    public PreviousVisitsAdapter(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    @NonNull
    @Override
    public PreviousVisitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_item_min, parent, false);
        PreviousVisitsAdapter.PreviousVisitsViewHolder visitsViewHolder = new PreviousVisitsAdapter.PreviousVisitsViewHolder(view, onItemClickListener);
        return visitsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousVisitsViewHolder holder, int position) {
        Visit currentVisit = visits.get(position);

        String date = "Date: " + ((currentVisit.getDay() < 10) ? "0" + currentVisit.getDay() : currentVisit.getDay()) + "."
                + ((currentVisit.getMonth() < 10) ? "0" + currentVisit.getMonth() : currentVisit.getMonth()) + "."
                + ((currentVisit.getYear() < 10) ? "0" + currentVisit.getYear() : currentVisit.getYear()) + ". Time: "
                + currentVisit.getHour() + ":" + ((currentVisit.getMinutes() == 0) ? currentVisit.getMinutes() + "0" : currentVisit.getMinutes());
        holder.dateTV.setText(date);

        if (currentVisit.getStatus().equals(STATUS_OFF)) {
            Glide.with(holder.img.getContext()).load(R.drawable.cancel).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PreviousVisitsAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class PreviousVisitsViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTV;
        public ImageView img;

        public PreviousVisitsViewHolder(@NonNull View itemView, PreviousVisitsAdapter.OnItemClickListener listener) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.visit_item_min_date);
            img = itemView.findViewById(R.id.visit_item_min_img);

            img.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Glide.with(img.getContext()).load(R.drawable.cancel).into(img);
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
