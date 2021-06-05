package com.example.animalcare.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.animalsActivities.AnimalDetailsActivity;
import com.example.animalcare.models.Animal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.ADMIN;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class AnimalsListActivity extends AppCompatActivity {
    private RecyclerView animalsRV;
    private AnimalsAdapter animalsAdapter;
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

        // Retrieve animals data from Firestore
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");
        animalsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Animal> animals = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Animal currentAnimal = document.toObject(Animal.class);
                        animals.add(currentAnimal);
                    }
                    animalsAdapter = new AnimalsAdapter(animals);

                    animalsRV.setLayoutManager(animalsLayoutManager);
                    animalsRV.setAdapter(animalsAdapter);

                    // click on animal
                    animalsAdapter.setOnItemClickListener(position -> {
                        Toast.makeText(AnimalsListActivity.this, animals.get(position).getSpecies(), Toast.LENGTH_SHORT).show();
                        //  String usernameToRemove = animals.get(position).getAnimalID();
                        Intent intent = new Intent(AnimalsListActivity.this, AnimalDetailsActivity.class);
                        intent.putExtra("Animal", animals.get(position));
                        startActivity(intent);
                    });

                    Log.d(TAG, animals.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        addBtn = findViewById(R.id.activity_animals_list_img_add);
        // Retrieve userRole from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String userRole = sharedpreferences.getString(UserRole, "");

        // Show addBtn if userRole == ADMIN or VOLUNTEER
        if (userRole.equals(ADMIN) || userRole.equals(VOLUNTEER)) {
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setOnClickListener(r -> {
                // Redirect to add an animal
                startActivity(new Intent(AnimalsListActivity.this, AddAnimalActivity.class));
            });
        }
        else {
            CardView cv = findViewById(R.id.activity_animals_list_cv_add);
            cv.setVisibility(View.INVISIBLE);
            addBtn.setVisibility(View.INVISIBLE);
        }

    }
}