package com.example.animalcare.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
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
    private SearchView searchView;
    private RecyclerView animalsRV;
    private AnimalsAdapter animalsAdapter;
    private RecyclerView.LayoutManager animalsLayoutManager;

    private ImageView addBtn;
    private ArrayList<Animal> animals, animalsAll;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);

        searchView = findViewById(R.id.activity_animals_list_sv);
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
                    animals = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Animal currentAnimal = document.toObject(Animal.class);
                        if (!currentAnimal.getWasAdopted()) {
                            animals.add(currentAnimal);
                        }
                    }
                    animalsAll = animals;

                    animalsAdapter = new AnimalsAdapter(animals);

                    animalsRV.setLayoutManager(animalsLayoutManager);
                    animalsRV.setAdapter(animalsAdapter);

                    // search
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            filter(newText);
                            return false;
                        }
                    });


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

    @Override
    protected void onRestart() {
        super.onRestart();

        // update animals list
        animalsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                animals = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Animal currentAnimal = document.toObject(Animal.class);
                    if (!currentAnimal.getWasAdopted()) {
                        animals.add(currentAnimal);
                    }
                }
                animalsAll = animals;
                animalsAdapter.filterList(animals);

                Log.d(TAG, animals.toString());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        animalsAdapter.notifyDataSetChanged();
    }

    private void filter(String text) {
        ArrayList<Animal> filteredList = new ArrayList<>();

        for (Animal item : animalsAll) {
            if (item.getSpecies().toLowerCase().contains(text.toLowerCase())
                    || item.getGender().toLowerCase().contains(text.toLowerCase())
                    || item.getColor().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || (item.getBreed() != null && item.getBreed().toLowerCase().contains(text.toLowerCase()))
                    || String.valueOf(item.getAge()).contains(text)
                    || (item.getSize() == 1 && "small".contains(text))
                    || (item.getSize() == 2 && "medium".contains(text))
                    || (item.getSize() == 3 && "big".contains(text))) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
//        else {
        animals = filteredList;
//        }

        animalsAdapter.filterList(animals);

        if (text.equals("")) {
            animalsAdapter.filterList(animalsAll);
        }
        animalsAdapter.notifyDataSetChanged();
    }

}