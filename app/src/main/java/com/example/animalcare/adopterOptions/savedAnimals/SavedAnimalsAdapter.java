package com.example.animalcare.adopterOptions.savedAnimals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animalcare.CRUD.AnimalsAdapter;
import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.models.Animal;

import java.util.ArrayList;

public class SavedAnimalsAdapter extends RecyclerView.Adapter<SavedAnimalsAdapter.SavedAnimalsViewHolder>{
    private ArrayList<SavedAnimal> animalsList;
    private AnimalsAdapter.OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AnimalsAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public SavedAnimalsAdapter(ArrayList<SavedAnimal> animalsList) {
        this.animalsList = animalsList;
    }

    @NonNull
    @Override
    public SavedAnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item, parent, false);
        SavedAnimalsViewHolder animalsViewHolder = new SavedAnimalsViewHolder(view, onItemClickListener);
        return animalsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedAnimalsViewHolder holder, int position) {
        SavedAnimal animal = animalsList.get(position);

        String details1 = animal.getSpecies() + ", " + (animal.getGender().equals(Animal.FEMALE) ? "F" : "M") + ", " + animal.getAge() + " years, size: " + ((animal.getSize() == 1) ? "small" : ((animal.getSize() == 2) ? "medium" : "big"));
        String details2 = "Personality: " + ((animal.getPersonalityType() == 1) ? "inactive" : ((animal.getPersonalityType() == 2) ? "medium" : "active"));
        String details3 = "Caring level required: " + ((animal.getAttentionLevelRequired() == 1) ? "small" : ((animal.getAttentionLevelRequired() == 2) ? "medium" : "high"));

        holder.detailsTV.setText(details1);
        holder.detailsTV2.setText(details2);
        holder.detailsTV3.setText(details3);
        if (animal.getImage() != null) {
            Glide.with(holder.animalImg.getContext()).load(animal.getImage()).into(holder.animalImg);
        }
    }

    @Override
    public int getItemCount() {
        return animalsList.size();
    }


    public class SavedAnimalsViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTV, detailsTV2, detailsTV3;
        public ImageView animalImg;

        public SavedAnimalsViewHolder(@NonNull View itemView, AnimalsAdapter.OnItemClickListener listener) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.animal_item_details);
            detailsTV2 = itemView.findViewById(R.id.animal_item_details_2);
            detailsTV3 = itemView.findViewById(R.id.animal_item_details_3);
            animalImg = itemView.findViewById(R.id.animal_item_img);

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
