package com.example.animalcare.CRUD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.animalcare.R;
import com.example.animalcare.models.Animal;

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

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Animal> filterList) {
        animalsList = filterList;
        notifyDataSetChanged();
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

        String details1 = animal.getSpecies() + ", " + (animal.getGender().equals(Animal.FEMALE) ? "F" : "M") + ", " + animal.getAge() + " years, size: " + ((animal.getSize() == 1) ? "small" : ((animal.getSize() == 2) ? "medium" : "big"));
        String details2 = "Personality: " + ((animal.getPersonalityType() == 1) ? "inactive" : ((animal.getPersonalityType() == 2) ? "medium" : "active"));
        String details3 = "Caring level required: " + ((animal.getCaringLevelRequired() == 1) ? "small" : ((animal.getCaringLevelRequired() == 2) ? "medium" : "high"));

        holder.detailsTV.setText(details1);
        holder.detailsTV2.setText(details2);
        holder.detailsTV3.setText(details3);

        if (animal.getImage() != null) {
            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);
            Glide.with(holder.animalImg.getContext())
                    .asBitmap()
                    .apply(myOptions)
                    .load(animal.getImage())
                    .into(holder.animalImg);
        } else {
//            holder.animalImg.setBackgroundResource(R.drawable.paw);
        }

//        Glide.with(holder.animalImg.getContext()).load(animal.getImage()).into(holder.animalImg);
    }

    @Override
    public int getItemCount() {
        return animalsList.size();
    }


    public class AnimalsViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTV, detailsTV2, detailsTV3;
        public ImageView animalImg;
        public LinearLayout item;

        public AnimalsViewHolder(@NonNull View itemView, AnimalsAdapter.OnItemClickListener listener) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.animal_item_details);
            detailsTV2 = itemView.findViewById(R.id.animal_item_details_2);
            detailsTV3 = itemView.findViewById(R.id.animal_item_details_3);
            animalImg = itemView.findViewById(R.id.animal_item_img);
            item = itemView.findViewById(R.id.animal_item_ll);

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
