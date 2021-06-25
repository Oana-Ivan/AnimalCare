package com.example.animalcare.animalsActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.CurrentAnimal;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.currentAnimalID;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

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
                    .setTitle("Are you sure you want to delete this animal?")
                    .setMessage("")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            animalsCollection.document(animalID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }

                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                            // TODO Was adopted
                            Toast.makeText(getContext(), "Animal deleted", Toast.LENGTH_SHORT).show();

//                            SharedPreferences.Editor editor = sharedpreferences.edit();
//                            editor.putString(update, "yes");
//                            editor.apply();

                            getActivity().finish();
                            startActivity(new Intent(getContext(), AnimalsListActivity.class));

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        updateBtn.setOnClickListener(u -> {
//            SharedPreferences sharedpreferences = getContext().getSharedPreferences(CurrentAnimal, Context.MODE_PRIVATE);
//            String animalID = sharedpreferences.getString(currentAnimalID, "");

            Intent intent = new Intent(getContext(), UpdateAnimalActivity.class);
//            intent.putExtra("Animal", )
            startActivity(intent);
        });

        return view;
    }
}