package com.example.animalcare.CRUD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.example.animalcare.models.Volunteer;

import java.util.ArrayList;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalsViewHolder>{
    private ArrayList<Animal> animalsList;
    private AnimalsAdapter.OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AnimalsAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public AnimalsAdapter(ArrayList<Animal> animalsList) {
        this.animalsList = animalsList;
    }

    @NonNull
    @Override
    public AnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item, parent, false);
        AnimalsViewHolder  animalsViewHolder = new AnimalsViewHolder(view, onItemClickListener);
        return animalsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsViewHolder holder, int position) {
        Animal animal = animalsList.get(position);

        String details1 = animal.getSpecies() + ", " + animal.getAge() + " years, " + ((animal.getSize() == 1) ? "small" : ((animal.getSize() == 2) ? "medium" : "big"));
        String details2 = "Personality: " + ((animal.getPersonalityType() == 1) ? "inactive" : ((animal.getPersonalityType() == 2) ? "medium" : "active"));
        String details3 = "Caring level required: " + ((animal.getAttentionLevelRequired() == 1) ? "small" : ((animal.getAttentionLevelRequired() == 2) ? "medium" : "high"));

        holder.detailsTV.setText(details1);
        holder.detailsTV2.setText(details2);
        holder.detailsTV3.setText(details3);
    }

    @Override
    public int getItemCount() {
        return animalsList.size();
    }


    public class AnimalsViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTV, detailsTV2, detailsTV3;

        public AnimalsViewHolder(@NonNull View itemView, AnimalsAdapter.OnItemClickListener listener) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.animal_item_details);
            detailsTV2 = itemView.findViewById(R.id.animal_item_details_2);
            detailsTV3 = itemView.findViewById(R.id.animal_item_details_3);

            itemView.setOnClickListener(v -> {
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
