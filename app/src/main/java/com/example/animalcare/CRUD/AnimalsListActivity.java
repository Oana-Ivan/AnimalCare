package com.example.animalcare.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.example.animalcare.models.Volunteer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AnimalsListActivity extends AppCompatActivity {
    private RecyclerView animalsRV;
    private VolunteersAdapter animalsAdapter;
    private RecyclerView.LayoutManager animalsLayoutManager;

    private ImageView addBtn;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);

        animalsRV = findViewById(R.id.activity_animals_list_rv);
        animalsRV.setHasFixedSize(true);
        animalsLayoutManager = new LinearLayoutManager(this);

        // TODO Retrieve volunteers data from Firestore
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");

        ArrayList<Animal> animals = new ArrayList<>();
        Animal currentAnimal1 = new Animal("asdc", 2.0, "FEMALE", "DOG", "SDF", "NEAH", false, 1, 1, "sdc", "swdsc");
        Animal currentAnimal2 = new Animal("asdc", 2.0, "FEMALE", "DOG", "SDF", "NEAH", false, 1, 1, "sdc", "swdsc");
        Animal currentAnimal3 = new Animal("asdc", 2.0, "FEMALE", "DOG", "SDF", "NEAH", false, 1, 1, "sdc", "swdsc");
        animals.add(currentAnimal1);
        animals.add(currentAnimal2);
        animals.add(currentAnimal3);

        AnimalsAdapter animalsAdapter = new AnimalsAdapter(animals);

        animalsRV.setLayoutManager(animalsLayoutManager);
        animalsRV.setAdapter(animalsAdapter);

        // click on animal
        // TODO Redirect to animal page
        animalsAdapter.setOnItemClickListener(position -> {
            Toast.makeText(AnimalsListActivity.this, animals.get(position).getSpecies(), Toast.LENGTH_SHORT).show();
//            String usernameToRemove = animals.get(position).getAnimalID();

        });

        addBtn = findViewById(R.id.activity_animals_list_img_add);
        addBtn.setOnClickListener(r -> {
            // Redirect to add volunteer
            startActivity(new Intent(AnimalsListActivity.this, AddAnimalActivity.class));
        });

    }
}