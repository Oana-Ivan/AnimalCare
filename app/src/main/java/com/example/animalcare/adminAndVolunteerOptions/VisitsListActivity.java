package com.example.animalcare.adminAndVolunteerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animalcare.CRUD.AddVolunteerActivity;
import com.example.animalcare.CRUD.VolunteersAdapter;
import com.example.animalcare.CRUD.VolunteersListActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Visit;
import com.example.animalcare.models.Volunteer;
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

public class VisitsListActivity extends AppCompatActivity {
    private RecyclerView visitsRV;
    private VisitsAdapter visitsAdapter;
    private RecyclerView.LayoutManager visitsLayoutManager;

    private ImageView setStatusOnBtn;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference visitsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_list);

        visitsRV = findViewById(R.id.activity_visits_list_rv);
        visitsRV.setHasFixedSize(true);
        visitsLayoutManager = new LinearLayoutManager(this);

        // Retrieve volunteers data from Firestore
        db = FirebaseFirestore.getInstance();
        visitsCollection = db.collection("Visits");
        visitsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Visit> visits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Visit currentVisit = document.toObject(Visit.class);
                        visits.add(currentVisit);
                    }
                    visitsAdapter = new VisitsAdapter(visits);

                    visitsRV.setLayoutManager(visitsLayoutManager);
                    visitsRV.setAdapter(visitsAdapter);

                    // click on image
                    visitsAdapter.setOnItemClickListener(position -> {
                        Toast.makeText(VisitsListActivity.this, visits.get(position).getAdopterUsername(), Toast.LENGTH_SHORT).show();
                        // TODO Update in firebase the status
                        visits.get(position).setStatus(Visit.STATUS_OFF);
                    });
                    Log.d(TAG, visits.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}