package com.example.animalcare.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animalcare.R;
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

public class VolunteersListActivity extends AppCompatActivity {
    private RecyclerView volunteersRV;
    private VolunteersAdapter volunteersAdapter;
    private RecyclerView.LayoutManager volunteersLayoutManager;

    private ImageView addBtn;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference volunteersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteers_list);

        volunteersRV = findViewById(R.id.activity_volunteers_list_rv);
        volunteersRV.setHasFixedSize(true);
        volunteersLayoutManager = new LinearLayoutManager(this);

        // Retrieve volunteers data from Firestore
        db = FirebaseFirestore.getInstance();
        volunteersCollection = db.collection("Volunteers");
        volunteersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Volunteer> volunteers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Volunteer currentVolunteer = document.toObject(Volunteer.class);
                    volunteers.add(currentVolunteer);
                }
                volunteersAdapter = new VolunteersAdapter(volunteers);

                volunteersRV.setLayoutManager(volunteersLayoutManager);
                volunteersRV.setAdapter(volunteersAdapter);

                // click on delete image
                volunteersAdapter.setOnItemClickListener(position -> {
                    AlertDialog dialog = new AlertDialog.Builder(VolunteersListActivity.this)
                            .setTitle("Remove volunteer " + volunteers.get(position).getUsername())
                            .setMessage("Are you sure you want to remove this volunteer?")
                            .setPositiveButton("Yes", (dialog1, which) -> {
                                Toast.makeText(VolunteersListActivity.this, volunteers.get(position).getUsername() + "deleted.", Toast.LENGTH_SHORT).show();
                                String usernameToRemove = volunteers.get(position).getUsername();
                                volunteersCollection.document(usernameToRemove)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                finish();
                                                startActivity(getIntent());
                                            }

                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                }, position -> {
                    Intent intent = new Intent(VolunteersListActivity.this, UpdateVolunteerActivity.class);
                    intent.putExtra("VOLUNTEER", volunteers.get(position));
                    finish();
                    startActivity(intent);
                });
                Log.d(TAG, volunteers.toString());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        addBtn = findViewById(R.id.activity_volunteers_list_img_add);
        addBtn.setOnClickListener(r -> {
            // Redirect to add volunteer
            finish();
            startActivity(new Intent(VolunteersListActivity.this, AddVolunteerActivity.class));
        });

    }
}