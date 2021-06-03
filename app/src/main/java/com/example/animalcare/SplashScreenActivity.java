package com.example.animalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.usersMainScreens.AdminHomeActivity;
import com.example.animalcare.usersMainScreens.AdopterHomeActivity;
import com.example.animalcare.usersMainScreens.VolunteerHomeActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.animalcare.authentication.RegisterActivity.ADOPTER;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int timeOut = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
                        // Redirect to VolunteerHomeActivity
                        Intent intent = new Intent(SplashScreenActivity.this, VolunteerHomeActivity.class);
                        startActivity(intent);
                    }
                    else { // if (userRole.equals(ADMIN))
                        // Redirect to AdminHomeActivity
                        Intent intent = new Intent(SplashScreenActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }, timeOut);
    }

}
