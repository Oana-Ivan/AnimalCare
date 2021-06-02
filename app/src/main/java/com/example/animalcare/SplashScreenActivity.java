package com.example.animalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.animalcare.usersMainScreens.AdopterHomeActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.animalcare.authentication.RegisterActivity.ADMIN;
import static com.example.animalcare.authentication.RegisterActivity.ADOPTER;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int timeOut = 2000;
    // cloud database
    public FirebaseFirestore db;
    public CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("firstName", "Ada");
//        user.put("lastName", "Lovelace");
//        user.put("email", "1815");
//        user.put("hashPassword", "1815");
//        user.put("role", "ADOPTER");
//        user.put("username", "1815");
//
//        // Add a new document with a generated ID
//        db.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//
//        db.collection("Users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData().get("firstName"));
//                                Toast.makeText(SplashScreenActivity.this, document.getData().get("firstName").toString(), Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });


        // Redirect to Main Activity after 2 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                // Retrieve username from sharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Username, "");
                if (username.equals("")) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    String userRole = sharedpreferences.getString(UserRole, "");
                    if (userRole.equals(ADOPTER)) {
                        // Redirect to AdopterHomeActivity
                        Intent intent = new Intent(SplashScreenActivity.this, AdopterHomeActivity.class);
                        startActivity(intent);
                    }
                    else if (userRole.equals(VOLUNTEER)) {
                        // TODO Redirect to VolunteerHomeActivity
                        Intent intent = new Intent(SplashScreenActivity.this, AdopterHomeActivity.class);
                        startActivity(intent);
                    }
                    else { // if (userRole.equals(ADMIN))
                        // TODO Redirect to AdminHomeActivity
                        Intent intent = new Intent(SplashScreenActivity.this, AdopterHomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }, timeOut);
    }

}
