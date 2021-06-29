package com.example.animalcare.adminAndVolunteerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.CRUD.VolunteersListActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.example.animalcare.models.Visit;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.models.Visit.STATUS_ON;

public class ShelterManagementActivity extends AppCompatActivity {
    private TextView volunteersTV, animalsTV, adoptersTV, visitsTV;
    private AppCompatButton addTicketBtn, viewAllTicketsBtn;

    // cloud database
    private FirebaseFirestore db;
    private CollectionReference volunteersCollection;
    private CollectionReference animalsCollection;
    private CollectionReference adoptersCollection;
    private CollectionReference visitsCollection;

    private int volunteersSize = 0;
    private int animalsSize = 0;
    private int adoptersSize = 0;
    private int visitsSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_management);

        initViews();
        firestoreInit();

        // get the number of volunteers in collection
        volunteersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    volunteersSize++;
                }
                String text = "Number of volunteers in the shelter: \n" + volunteersSize;
                volunteersTV.setText(text);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        // get the number of animals in collection
        animalsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Animal animal = (Animal) document.toObject(Animal.class);
                    if (!animal.getWasAdopted()) {
                        animalsSize++;
                    }
                }
                String text = "Number of animals at the shelter: \n" + animalsSize;
                animalsTV.setText(text);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        // get the number of adopters in collection
        adoptersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    adoptersSize++;
                }
                String text = "Number of registered adopters: \n" + adoptersSize;
                adoptersTV.setText(text);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        // get the number of visits in collection
        visitsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Visit visit = (Visit) document.toObject(Visit.class);
                    if (visit.getStatus().equals(STATUS_ON)) {
                        visitsSize++;
                    }
                }
                String text = "Number of schedule visits: \n" + visitsSize;
                visitsTV.setText(text);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


        setOnClickEvents();
    }

    private void initViews() {
        volunteersTV = findViewById(R.id.activity_shelter_management_tv_number_of_volunteers);
        animalsTV = findViewById(R.id.activity_shelter_management_tv_number_of_animals);
        adoptersTV = findViewById(R.id.activity_shelter_management_tv_number_of_adopters);
        visitsTV = findViewById(R.id.activity_shelter_management_tv_number_of_visits);
        addTicketBtn = findViewById(R.id.activity_shelter_management_btn_add_ticket);
        viewAllTicketsBtn = findViewById(R.id.activity_shelter_management_btn_view_tickets);
    }
    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        volunteersCollection = db.collection("Volunteers");
        animalsCollection = db.collection("Animals");
        adoptersCollection = db.collection("Users");
        visitsCollection = db.collection("Visits");
    }

    private void setOnClickEvents() {
        volunteersTV.setOnClickListener(c -> {
            startActivity(new Intent(ShelterManagementActivity.this, VolunteersListActivity.class));
        });
        animalsTV.setOnClickListener(c -> {
            startActivity(new Intent(ShelterManagementActivity.this, AnimalsListActivity.class));
        });
        adoptersTV.setOnClickListener(c -> {
//            startActivity(new Intent(ShelterManagementActivity.this, A.class));
        });
        visitsTV.setOnClickListener(c -> {
            startActivity(new Intent(ShelterManagementActivity.this, VisitsListActivity.class));
        });
        addTicketBtn.setOnClickListener(c -> {
            startActivity(new Intent(ShelterManagementActivity.this, AddTicketActivity.class));
        });
        viewAllTicketsBtn.setOnClickListener(c -> {
            startActivity(new Intent(ShelterManagementActivity.this, TicketsListActivity.class));
        });
    }
}
