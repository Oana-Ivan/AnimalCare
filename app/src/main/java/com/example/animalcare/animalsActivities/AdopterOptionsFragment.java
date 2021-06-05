package com.example.animalcare.animalsActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.AdopterOptionsDatabase;
import com.example.animalcare.adopterOptions.roomDatabase.dao.SavedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.models.Animal;

import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class AdopterOptionsFragment extends Fragment {

    public AdopterOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adopter_options, container, false);
        AppCompatButton saveAnimalBtn = view.findViewById(R.id.animal_fragment_options_save);

        // Save animal in Room database
        saveAnimalBtn.setOnClickListener(v -> {
            Animal currentAnimal = (Animal) getActivity().getIntent().getSerializableExtra("Animal");

            AdopterOptionsDatabase db = Room.databaseBuilder(getContext(), AdopterOptionsDatabase.class, "db-app").allowMainThreadQueries().build();
            SavedAnimalDAO savedAnimalDAO = db.savedAnimalDAO();

            // Retrieve username from sharedPreferences
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
            String username = sharedpreferences.getString(Username, "");

            // verify if the animal already exists
            if (savedAnimalDAO.findByAnimalID(currentAnimal.getAnimalID()) == null) {
                // insert new animal
                SavedAnimal newAnimal = new SavedAnimal(currentAnimal.getAnimalID(), username, currentAnimal.getAge(), currentAnimal.getGender(),
                        currentAnimal.getSpecies(), currentAnimal.getColor(), currentAnimal.getDescription(), currentAnimal.hasDisease(),
                        currentAnimal.getImage(), currentAnimal.getArrivingDate(), currentAnimal.getBreed(), currentAnimal.getSize(),
                        currentAnimal.getPersonalityType(), currentAnimal.getAttentionLevelRequired(), currentAnimal.getAttentionLevelRequired());

                savedAnimalDAO.insertAll(newAnimal);
                Toast.makeText(getContext(), "The animal has been added", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getContext(), "Animal already saved", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}