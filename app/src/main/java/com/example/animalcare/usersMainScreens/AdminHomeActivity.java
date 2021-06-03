package com.example.animalcare.usersMainScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.animalcare.CRUD.VolunteersListActivity;
import com.example.animalcare.MainActivity;
import com.example.animalcare.R;
import com.example.animalcare.SplashScreenActivity;
import com.example.animalcare.authentication.LoginActivity;
import com.example.animalcare.authentication.LoginAdminActivity;
import com.example.animalcare.authentication.RegisterActivity;
import com.example.animalcare.models.Admin;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.animalcare.authentication.RegisterActivity.ADOPTER;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class AdminHomeActivity extends AppCompatActivity {
    private TextView logOut;
    private static final int timeOut = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.activity_admin_home_circle_menu);
        logOut = findViewById(R.id.activity_admin_home_logout);

        circleMenu.setMainMenu(Color.parseColor("#024E36"), R.drawable.dog_home, R.drawable.cancel)
                .addSubMenu(Color.parseColor("#79BC89"), R.drawable.cat)
                .addSubMenu(Color.parseColor("#79BC89"), R.drawable.contact)
                .addSubMenu(Color.parseColor("#79BC89"), R.drawable.home)
                .addSubMenu(Color.parseColor("#79BC89"), R.drawable.clock)
                .setOnMenuSelectedListener(this::openActivity);

        logOut.setOnClickListener(r -> {
            // delete username from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            // redirect to main activity
            startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
        });
    }
    private void openActivity(int option) {
        // TODO Add redirect to activities
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                switch (option) {
                    case 0:
                        // Animals
                        startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                        break;
                    case 1:
                        // Volunteers
                        startActivity(new Intent(AdminHomeActivity.this, VolunteersListActivity.class));
                        break;
                    case 2:
                        // Shelter Management
                        startActivity(new Intent(AdminHomeActivity.this, LoginAdminActivity.class));
                        break;
                    case 3:
                        // Visits
                        startActivity(new Intent(AdminHomeActivity.this, LoginAdminActivity.class));
                        break;
                }
            }
        }, timeOut);

    }
}