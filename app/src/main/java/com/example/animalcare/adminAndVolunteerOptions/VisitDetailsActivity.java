package com.example.animalcare.adminAndVolunteerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalcare.CRUD.AnimalsAdapter;
import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.R;
import com.example.animalcare.animalsActivities.AnimalDetailsActivity;
import com.example.animalcare.models.Animal;
import com.example.animalcare.models.BasicUser;
import com.example.animalcare.models.Visit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.adminAndVolunteerOptions.VisitsListActivity.VISIT;
import static com.example.animalcare.models.Visit.STATUS_OFF;

public class VisitDetailsActivity extends AppCompatActivity {
    private Visit currentVisit;
    private TextView visitInfoTV, adopterInfoTV, savedAnimalsTV, closedTV;
    private RecyclerView animalsRV;
    private VisitAnimalAdapter animalAdapter;
    private GridLayoutManager gridLayoutManager;

    private String username;
    private int numberOfColumns = 3;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);

        initViews();

        // Set visit info
        String visitInfo = "Date: " + ((currentVisit.getDay() < 10) ? "0" + currentVisit.getDay() : currentVisit.getDay()) + "."
                + ((currentVisit.getMonth() < 10) ? "0" + currentVisit.getMonth() : currentVisit.getMonth()) + "."
                + ((currentVisit.getYear() < 10) ? "0" + currentVisit.getYear() : currentVisit.getYear())
                + "\nTime: " + ((currentVisit.getHour() < 10) ? "0" + currentVisit.getHour() : currentVisit.getHour())
                + ":" + ((currentVisit.getMinutes() == 0) ? currentVisit.getMinutes() + "0" : currentVisit.getMinutes());
        visitInfoTV.setText(visitInfo);

        // Set adopter info
        username = currentVisit.getAdopterUsername();
        // Get adopter information from FireStore
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                BasicUser user = document.toObject(BasicUser.class);
                if (user != null) {
                    String adopterInfo = "Username: " + username + "\nFull name: " + user.getFirstName()
                            + " " + user.getLastName().toUpperCase() + "\nEmail: " + user.getEmail();
                    adopterInfoTV.setText(adopterInfo);
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });

        // Add saved animals list
//        animalsRV.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        // Retrieve animals data from Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("Animals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> savedAnimalsIDList = currentVisit.getSavedAnimalsID();
                List<Animal> savedAnimalsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Animal currentAnimal = document.toObject(Animal.class);
                    if (savedAnimalsIDList.contains(currentAnimal.getAnimalID())) {
                        savedAnimalsList.add(currentAnimal);
                    }
                }
                animalAdapter = new VisitAnimalAdapter((ArrayList<Animal>) savedAnimalsList);

                animalsRV.setLayoutManager(gridLayoutManager);
                animalsRV.setAdapter(animalAdapter);

//                // search
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        filter(newText);
//                        return false;
//                    }
//                });


                // click on animal
                animalAdapter.setOnItemClickListener(position -> {
                    Toast.makeText(VisitDetailsActivity.this, savedAnimalsList.get(position).getSpecies(), Toast.LENGTH_SHORT).show();
                    //  String usernameToRemove = animals.get(position).getAnimalID();
                    Intent intent = new Intent(VisitDetailsActivity.this, AnimalDetailsActivity.class);
                    intent.putExtra("Animal", savedAnimalsList.get(position));
                    startActivity(intent);
                });

                Log.d(TAG, savedAnimalsList.toString());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        // Set status of the visit as OFF
        closedTV.setOnClickListener(c -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Remove visit")
                    .setMessage("Are you sure you want to mark this visit as closed?")
                    .setPositiveButton("Yes", (dialog1, which) -> {
                        currentVisit.setStatus(STATUS_OFF);

                        // Update status of visit in collection
                        db.collection("Visits").document(currentVisit.getVisitID())
                                .set(currentVisit)
                                .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Updated visit"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

                        // Redirect to all visits
                        finish();
                        startActivity(new Intent(VisitDetailsActivity.this, VisitsListActivity.class));
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void initViews() {
        currentVisit = (Visit) getIntent().getSerializableExtra(VISIT);
        visitInfoTV = findViewById(R.id.activity_visit_details_tv_info_2);
        adopterInfoTV = findViewById(R.id.activity_visit_details_tv_adopter_2);
        savedAnimalsTV = findViewById(R.id.activity_visit_details_tv_animals);
        animalsRV = findViewById(R.id.activity_visit_details_rv_animals);
        closedTV = findViewById(R.id.activity_visit_details_tv_closed);
    }
}