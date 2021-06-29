package com.example.animalcare.animalsActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.CRUD.UpdateAnimalActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.CurrentAnimal;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.currentAnimalID;

public class OptionsFragment extends Fragment {
    private AppCompatButton deleteBtn, updateBtn;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;

    private static final String update = "update";

    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        deleteBtn = view.findViewById(R.id.animal_fragment_options_delete);
        updateBtn = view.findViewById(R.id.animal_fragment_options_update);

        // Retrieve volunteers data from Firestore
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");

        deleteBtn.setOnClickListener(d -> {
            SharedPreferences sharedpreferences = getContext().getSharedPreferences(CurrentAnimal, Context.MODE_PRIVATE);
            String animalID = sharedpreferences.getString(currentAnimalID, "");

            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Remove animal")
                    .setMessage("Are you sure you want to remove this animal from the shelter database?")
                    .setPositiveButton("Yes", (dialog1, which) -> {
                        // Modify wasAdopted to true
                        // Get the data from FireStore for the current animal
                        animalsCollection.document(animalID).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Animal animal = document.toObject(Animal.class);
                                if (animal != null) {
                                    animal.setWasAdopted(true);
                                    // Update in FireStore
                                    db.collection("Animals")
                                            .document(animalID)
                                            .set(animal)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Animal removed " + animal.getWasAdopted(), Toast.LENGTH_SHORT).show();

                                                    getActivity().finish();
                                                    startActivity(new Intent(getContext(), AnimalsListActivity.class));
                                                }
                                                else {
                                                    String error = task1.getException().getMessage();
                                                    Toast.makeText(getContext(), "(FireStore Error) : " + error, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "Failed with: ", task.getException());
                            }
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        updateBtn.setOnClickListener(u -> {
            Intent intent = new Intent(getContext(), UpdateAnimalActivity.class);
            getActivity().finish();
            startActivity(intent);
        });

        return view;
    }
}