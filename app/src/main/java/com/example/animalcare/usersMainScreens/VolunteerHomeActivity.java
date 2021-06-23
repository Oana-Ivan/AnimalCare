package com.example.animalcare.usersMainScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.MainActivity;
import com.example.animalcare.R;
import com.example.animalcare.adminAndVolunteerOptions.ShelterManagementActivity;
import com.example.animalcare.adminAndVolunteerOptions.VisitsListActivity;
import com.example.animalcare.authentication.LoginActivity;
import com.example.animalcare.authentication.LoginAdminActivity;
import com.example.animalcare.authentication.RegisterActivity;
import com.example.animalcare.models.Volunteer;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hitomi.cmlibrary.CircleMenu;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class VolunteerHomeActivity extends AppCompatActivity {
    private TextView logOut, nameTV, emailTV, programTV;
    private String name, email, program;
    private static final int timeOut = 500;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference volunteersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.activity_volunteer_home_circle_menu);
        logOut = findViewById(R.id.activity_volunteer_home_logout);
        nameTV = findViewById(R.id.activity_volunteer_home_tv_name);
        emailTV = findViewById(R.id.activity_volunteer_home_tv_email);
        programTV = findViewById(R.id.activity_volunteer_home_tv_program);

        firestoreInit();

        // Retrieve username from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Username, "");

        // Get user first name, last name and email from database
        volunteersCollection.document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Volunteer volunteer = document.toObject(Volunteer.class);
                if (volunteer != null) {
                    name = "username: " + username + " (Full name: " + volunteer.getFirstName() + " " + volunteer.getLastName().toUpperCase() + ")";
                    email = "email: " + volunteer.getEmail();
                    program = "Program: " + volunteer.getStartingHour() + ":00 - " + volunteer.getEndingHour() + ":00 " + volunteer.getWorkingDays();
                    nameTV.setText(name);
                    emailTV.setText(email);
                    programTV.setText(program);
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });

        circleMenu.setMainMenu(Color.parseColor("#024E36"), R.drawable.dog_home, R.drawable.cancel)
                .addSubMenu(Color.parseColor("#86C194"), R.drawable.cat)
                .addSubMenu(Color.parseColor("#86C194"), R.drawable.home)
                .addSubMenu(Color.parseColor("#86C194"), R.drawable.clock)
                .setOnMenuSelectedListener(this::openActivity);

        logOut.setOnClickListener(r -> {
            // delete username from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            // redirect to main activity
            startActivity(new Intent(VolunteerHomeActivity.this, MainActivity.class));
        });
    }

    private void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        volunteersCollection = db.collection("Volunteers");
    }

    private void openActivity(int option) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                finish();
                switch (option) {
                    case 0:
                        // Animals
                        startActivity(new Intent(VolunteerHomeActivity.this, AnimalsListActivity.class));
                        break;
                    case 1:
                        // Shelter Management
                        startActivity(new Intent(VolunteerHomeActivity.this, ShelterManagementActivity.class));
                        break;
                    case 2:
                        // Visits
                        startActivity(new Intent(VolunteerHomeActivity.this, VisitsListActivity.class));
                        break;
                }
            }
        }, timeOut);

    }
}