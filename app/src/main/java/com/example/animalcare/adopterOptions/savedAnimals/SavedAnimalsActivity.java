package com.example.animalcare.adopterOptions.savedAnimals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.animalcare.CRUD.AnimalsAdapter;
import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.AdopterOptionsDatabase;
import com.example.animalcare.adopterOptions.roomDatabase.dao.SavedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.animalsActivities.AnimalDetailsActivity;
import com.example.animalcare.models.Animal;

import java.util.ArrayList;
import java.util.List;

import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class SavedAnimalsActivity extends AppCompatActivity {
    private RecyclerView animalsRV;
    private SavedAnimalsAdapter animalsAdapter;
    private RecyclerView.LayoutManager animalsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_animals);

        animalsRV = findViewById(R.id.activity_saved_animals_list_rv);
        animalsRV.setHasFixedSize(true);
        animalsLayoutManager = new LinearLayoutManager(this);

        // Retrieve username from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Username, "");

        // Retrieve all saved animals from Room Database
        AdopterOptionsDatabase db = Room.databaseBuilder(this, AdopterOptionsDatabase.class, "db-app").allowMainThreadQueries().build();
        SavedAnimalDAO savedAnimalDAO = db.savedAnimalDAO();
        List<SavedAnimal> savedAnimals = savedAnimalDAO.findByAdopterID(username);

        // Initialize recycle view
        animalsAdapter = new SavedAnimalsAdapter((ArrayList<SavedAnimal>) savedAnimals);
        animalsRV.setLayoutManager(animalsLayoutManager);
        animalsRV.setAdapter(animalsAdapter);

        // click on animal
        animalsAdapter.setOnItemClickListener(position -> {
            Toast.makeText(SavedAnimalsActivity.this, savedAnimals.get(position).getSpecies(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SavedAnimalsActivity.this, AnimalDetailsActivity.class);
            Animal animal = new Animal(savedAnimals.get(position).getArrivingDate(), savedAnimals.get(position).getAge(), savedAnimals.get(position).getGender(), savedAnimals.get(position).getSpecies(), savedAnimals.get(position).getColor(), savedAnimals.get(position).getDescription(), savedAnimals.get(position).isDisease(), savedAnimals.get(position).getPersonalityType(), savedAnimals.get(position).getSize(), savedAnimals.get(position).getAnimalId(), savedAnimals.get(position).getImage());
            intent.putExtra("Animal", animal);
            startActivity(intent);
        });

    }
}