package com.example.animalcare.adminAndVolunteerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalcare.R;
import com.example.animalcare.models.Visit;

import java.util.ArrayList;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitsViewHolder>{
    private ArrayList<Visit> visits;
    private OnItemClickListener onItemClickListener1;
    private OnItemClickListener onItemClickListener2;

    public VisitsAdapter(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    @NonNull
    @Override
    public VisitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_item, parent, false);
        VisitsViewHolder visitsViewHolder = new VisitsViewHolder(view, onItemClickListener1, onItemClickListener2);
        return visitsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsViewHolder holder, int position) {
        Visit currentVisit = visits.get(position);

        String adopterName = currentVisit.getAdopterUsername();
        String date = "Date: " + ((currentVisit.getDay() < 10) ? "0" + currentVisit.getDay() : currentVisit.getDay()) + "."
                + ((currentVisit.getMonth() < 10) ? "0" + currentVisit.getMonth() : currentVisit.getMonth()) + "."
                + ((currentVisit.getYear() < 10) ? "0" + currentVisit.getYear() : currentVisit.getYear());
        String time = "Time: " + ((currentVisit.getHour() < 10) ? "0" + currentVisit.getHour() : currentVisit.getHour())
                + ":" + ((currentVisit.getMinutes() == 0) ? currentVisit.getMinutes() + "0" : currentVisit.getMinutes());

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

    public void setOnItemClickListener(VisitsAdapter.OnItemClickListener listener1, VisitsAdapter.OnItemClickListener listener2) {
        onItemClickListener1 = listener1;
        onItemClickListener2 = listener2;
    }

    public class VisitsViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV, dateTV, timeTV;
        public ImageView img;

        public VisitsViewHolder(@NonNull View itemView, VisitsAdapter.OnItemClickListener listener1, VisitsAdapter.OnItemClickListener listener2) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.visit_item_adopter_name);
            dateTV = itemView.findViewById(R.id.visit_item_date);
            timeTV = itemView.findViewById(R.id.visit_item_time);
            img = itemView.findViewById(R.id.visit_item_img);

            img.setOnClickListener(v -> {
                if (listener1 != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
//                        Glide.with(img.getContext()).load(R.drawable.cancel).into(img);
                        listener1.onItemClick(position);
                    }
                }
            });
            itemView.setOnClickListener(v -> {
                if (listener2 != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener2.onItemClick(position);
                    }
                }
            });
        }
    }
}
