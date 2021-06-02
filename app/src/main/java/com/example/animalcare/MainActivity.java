package com.example.animalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalcare.authentication.LoginActivity;
import com.example.animalcare.authentication.LoginAdminActivity;
import com.example.animalcare.authentication.RegisterActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton loginBtn;
    private AppCompatButton registerBtn;
    private TextView loginAdminTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.activity_main_btn_login);
        registerBtn = findViewById(R.id.activity_main_btn_register);
        loginAdminTV = findViewById(R.id.activity_main_tv_login_admin);

        loginBtn.setOnClickListener(r -> redirectToActivity("login"));
        registerBtn.setOnClickListener(r -> redirectToActivity("register"));
        loginAdminTV.setOnClickListener(r -> redirectToActivity("loginAdmin"));

//        Realm.init(this);
//        App app = new App(new AppConfiguration.Builder(appID).build());

        // TODO Different output based on user role
//        Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();
    }

    private void redirectToActivity (String activityName) {
        Intent intent;

        switch (activityName) {
            case "login":
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case "register":
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case "loginAdmin":
                intent = new Intent(MainActivity.this, LoginAdminActivity.class);
                startActivity(intent);
                break;
        }
    }
}