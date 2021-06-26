package com.example.animalcare.adminAndVolunteerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.animalcare.R;
import com.example.animalcare.models.Animal;

import java.util.ArrayList;

public class VisitAnimalAdapter extends RecyclerView.Adapter<VisitAnimalAdapter.VisitAnimalViewHolder> {

    private ArrayList<Animal> animalsList;
    private VisitAnimalAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(VisitAnimalAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public VisitAnimalAdapter(ArrayList<Animal> animalsList) {
        this.animalsList = animalsList;
    }

    @NonNull
    @Override
    public VisitAnimalAdapter.VisitAnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_animal_item, parent, false);
        VisitAnimalAdapter.VisitAnimalViewHolder animalsViewHolder = new VisitAnimalViewHolder(view, onItemClickListener);
        return animalsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitAnimalViewHolder holder, int position) {
        Animal animal = animalsList.get(position);

        String info = animal.getSpecies() + ", " + (animal.getGender().equals(Animal.FEMALE) ? "F" : "M") + ", " + animal.getAge() + " years";
        holder.detailsTV.setText(info);

        if (animal.getImage() != null) {
            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);
            Glide.with(holder.animalImg.getContext())
                    .asBitmap()
                    .apply(myOptions)
                    .load(animal.getImage())
                    .into(holder.animalImg);
        } else {
            Glide.with(holder.animalImg.getContext()).load(R.drawable.paw).into(holder.animalImg);
        }
    }

    @Override
    public int getItemCount() {
        return animalsList.size();
    }

    public class VisitAnimalViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTV;
        public ImageView animalImg;

        public VisitAnimalViewHolder(@NonNull View itemView, VisitAnimalAdapter.OnItemClickListener listener) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.simple_animal_item_tv);
            animalImg = itemView.findViewById(R.id.simple_animal_item_img);

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
