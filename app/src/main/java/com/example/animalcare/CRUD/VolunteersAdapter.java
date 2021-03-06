package com.example.animalcare.CRUD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalcare.R;
import com.example.animalcare.models.Volunteer;

import java.util.ArrayList;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.VolunteersViewHolder> {
    private ArrayList<Volunteer> volunteersList;
    private OnItemClickListener onItemClickListener1;
    private OnItemClickListener onItemClickListener2;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener1, OnItemClickListener listener2) {
        onItemClickListener1 = listener1;
        onItemClickListener2 = listener2;
    }

    public VolunteersAdapter(ArrayList<Volunteer> volunteersList) {
        this.volunteersList = volunteersList;
    }

    @NonNull
    @Override
    public VolunteersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_item, parent, false);
        VolunteersViewHolder volunteersViewHolder = new VolunteersViewHolder(view, onItemClickListener1, onItemClickListener2);
        return volunteersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteersViewHolder holder, int position) {
        Volunteer currentVolunteer = volunteersList.get(position);

        String details = currentVolunteer.getUsername() + " (" + currentVolunteer.getFirstName() + " " + currentVolunteer.getLastName().toUpperCase() + ")";
        String email = "Email: " + currentVolunteer.getEmail();
        String program = "Program: " + currentVolunteer.getStartingHour() + ":00 - " + currentVolunteer.getEndingHour() + ":00 " + currentVolunteer.getWorkingDays();
        holder.detailsTV.setText(details);
        holder.emailTV.setText(email);
        holder.programTV.setText(program);
    }

    @Override
    public int getItemCount() {
        return volunteersList.size();
    }

    public class VolunteersViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTV, emailTV, programTV;
        public ImageView deleteImg;

        public VolunteersViewHolder(@NonNull View itemView, OnItemClickListener listener1, OnItemClickListener listener2) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.volunteer_item_details);
            emailTV = itemView.findViewById(R.id.volunteer_item_email);
            programTV = itemView.findViewById(R.id.volunteer_item_program);
            deleteImg = itemView.findViewById(R.id.volunteer_item_img_delete);

            deleteImg.setOnClickListener(v -> {
                if (listener1 != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
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
