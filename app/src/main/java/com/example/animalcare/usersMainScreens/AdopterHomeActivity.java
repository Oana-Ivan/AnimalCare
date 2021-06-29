package com.example.animalcare.usersMainScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.MainActivity;
import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.visits.AddVisitActivity;
import com.example.animalcare.adopterOptions.recommendedAnimals.RecommendedAnimalsActivity;
import com.example.animalcare.adopterOptions.savedAnimals.SavedAnimalsActivity;
import com.example.animalcare.models.BasicUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;


public class AdopterHomeActivity extends AppCompatActivity {
    private TextView nameTV, emailTV, logOutTV;
    private String name, email;
    private LinearLayout scheduleLL, allAnimalsLL, recAnimalsLL, savedAnimalsLL;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        viewsInit();
        firestoreInit();

        // Retrieve username from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Username, "");

        // Get user first name, last name and email from database
        usersCollection.document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                BasicUser user = document.toObject(BasicUser.class);
                if (user != null) {
                    name = "username: " + username + " (Full name: " + user.getFirstName() + " " + user.getLastName().toUpperCase() + ")";
                    email = "email: " + user.getEmail();
                    nameTV.setText(name);
                    emailTV.setText(email);
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });

        // Added onClick events for the actions list items
        scheduleLL.setOnClickListener(r -> openActivity("schedule"));
        allAnimalsLL.setOnClickListener(r -> openActivity("allAnimals"));
        recAnimalsLL.setOnClickListener(r -> openActivity("recAnimals"));
        savedAnimalsLL.setOnClickListener(r -> openActivity("savedAnimals"));
        logOutTV.setOnClickListener(r -> {
            // delete username from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            // redirect to main activity
            openActivity("main");
        });

    }

    private void openActivity(String activityName) {
        switch (activityName) {
            case "schedule":
                startActivity(new Intent(AdopterHomeActivity.this, AddVisitActivity.class));
                break;
            case "allAnimals":
                startActivity(new Intent(AdopterHomeActivity.this, AnimalsListActivity.class));
                break;
            case "recAnimals":
                startActivity(new Intent(AdopterHomeActivity.this, RecommendedAnimalsActivity.class));
                break;
            case "savedAnimals":
                startActivity(new Intent(AdopterHomeActivity.this, SavedAnimalsActivity.class));
                break;
            case "main":
                startActivity(new Intent(AdopterHomeActivity.this, MainActivity.class));
                break;
        }
    }

    private void viewsInit() {
        nameTV = findViewById(R.id.activity_adopter_home_tv_name);
        emailTV = findViewById(R.id.activity_adopter_home_tv_email);
        logOutTV = findViewById(R.id.activity_adopter_home_logout);
        scheduleLL = findViewById(R.id.activity_adopter_home_ll_schedule);
        allAnimalsLL = findViewById(R.id.activity_adopter_home_ll_all_animals);
        recAnimalsLL = findViewById(R.id.activity_adopter_home_ll_rec_animals);
        savedAnimalsLL = findViewById(R.id.activity_adopter_home_ll_saved_animals);
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
    }
}