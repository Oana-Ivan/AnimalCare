package com.example.animalcare.adopterOptions.recommendedAnimals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.animalcare.CRUD.AnimalsAdapter;
import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.AdopterOptionsDatabase;
import com.example.animalcare.adopterOptions.roomDatabase.dao.RecommendedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.RecommendedAnimal;
import com.example.animalcare.animalsActivities.AnimalDetailsActivity;
import com.example.animalcare.models.Animal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.adopterOptions.recommendedAnimals.RecommendedAnimalsTestFragment.attention;
import static com.example.animalcare.adopterOptions.recommendedAnimals.RecommendedAnimalsTestFragment.caring;
import static com.example.animalcare.adopterOptions.recommendedAnimals.RecommendedAnimalsTestFragment.wantsCat;
import static com.example.animalcare.adopterOptions.recommendedAnimals.RecommendedAnimalsTestFragment.wantsDog;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.models.Animal.CAT;
import static com.example.animalcare.models.Animal.DOG;

public class RecommendedAnimalsListFragment extends Fragment {
    private RecyclerView animalsRV;
    private AnimalsAdapter animalsAdapter;
    private RecyclerView.LayoutManager animalsLayoutManager;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;

    public RecommendedAnimalsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recommended_animals_list, container, false);

        animalsRV = view.findViewById(R.id.fragment_rec_list_rv);
        animalsRV.setHasFixedSize(true);
        animalsLayoutManager = new LinearLayoutManager(getContext());


        // Save data in Room database, table recommended_animals
        AdopterOptionsDatabase dbLocal = Room.databaseBuilder(getContext(), AdopterOptionsDatabase.class, "db-app").allowMainThreadQueries().build();
        RecommendedAnimalDAO recommendedAnimalDAO = dbLocal.recommendedAnimalDAO();
        // Retrieve username from sharedPreferences
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Username, "");

        if (RecommendedAnimalsActivity.recAnimalsFromTest) {
            // Remove results from previous test
            recommendedAnimalDAO.clearTable();

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
                            // TODO Add only the items corresponding to the test

                            if (currentAnimal.getAttentionLevelRequired() <= attention
                                    && currentAnimal.getCaringLevelRequired() <= caring
                                    && ((currentAnimal.getSpecies().equals(DOG) && wantsDog)
                                        || (currentAnimal.getSpecies().equals(CAT) && wantsCat))) {
                                animals.add(currentAnimal);
                                int id = recommendedAnimalDAO.getNoOfRecommendedAnimals() + 1;
                                RecommendedAnimal recAnimal = new RecommendedAnimal(id, currentAnimal.getAnimalID(), username, currentAnimal.getAge(), currentAnimal.getGender(),
                                        currentAnimal.getSpecies(), currentAnimal.getColor(), currentAnimal.getDescription(), currentAnimal.getDisease(),
                                        currentAnimal.getImage(), currentAnimal.getArrivingDate(), currentAnimal.getBreed(), currentAnimal.getSize(),
                                        currentAnimal.getPersonalityType(), currentAnimal.getAttentionLevelRequired(), currentAnimal.getAttentionLevelRequired());
                                recommendedAnimalDAO.insertAll(recAnimal);
                            }
                        }

                        animalsAdapter = new AnimalsAdapter(animals);
                        animalsRV.setLayoutManager(animalsLayoutManager);
                        animalsRV.setAdapter(animalsAdapter);

                        // click on animal
                        animalsAdapter.setOnItemClickListener(position -> {
                            Toast.makeText(getContext(), animals.get(position).getSpecies(), Toast.LENGTH_SHORT).show();
                            //  String usernameToRemove = animals.get(position).getAnimalID();
                            Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
                            intent.putExtra("Animal", animals.get(position));
                            startActivity(intent);
                        });

                        Log.d(TAG, animals.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else {
            // Retrieve animals data from Room database, table "recommended_animals"
            List<RecommendedAnimal> recAnimals = recommendedAnimalDAO.findByAdopterID(username);
            List<Animal> animals = new ArrayList<>();

            for (int i = 0; i < recAnimals.size(); i++) {
                RecommendedAnimal currentAnimal = recAnimals.get(i);
                Animal animal = new Animal(currentAnimal.getArrivingDate(), currentAnimal.getAge(), currentAnimal.getGender(),
                        currentAnimal.getSpecies(), currentAnimal.getColor(), currentAnimal.getDescription(), currentAnimal.isDisease(),
                        currentAnimal.getPersonalityType(), currentAnimal.getSize(), currentAnimal.getAnimalId(), currentAnimal.getImage());
                animals.add(animal);
            }

            // Initialize recycle view
            animalsAdapter = new AnimalsAdapter((ArrayList<Animal>) animals);
            animalsRV.setLayoutManager(animalsLayoutManager);
            animalsRV.setAdapter(animalsAdapter);
        }

        return view;
    }
}