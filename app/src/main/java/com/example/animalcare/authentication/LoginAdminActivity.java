package com.example.animalcare.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.MainActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Admin;
import com.example.animalcare.usersMainScreens.AdminHomeActivity;

import static com.example.animalcare.authentication.RegisterActivity.ADMIN;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class LoginAdminActivity extends AppCompatActivity {
    private EditText usernameET, passwordET;
    private String username, password;
    private AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        usernameET = findViewById(R.id.activity_login_admin_et_username);
        passwordET = findViewById(R.id.activity_login_admin_et_password);
        loginBtn = findViewById(R.id.activity_login_admin_btn_login);

        loginBtn.setOnClickListener(r -> {
            username = usernameET.getText().toString();
            password = passwordET.getText().toString();

            Admin admin = Admin.getInstance();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginAdminActivity.this, "All fields required", Toast.LENGTH_LONG).show();
            }
            else if (admin.verifyCredentials(username, password)) {
                Toast.makeText(LoginAdminActivity.this, "Welcome, admin", Toast.LENGTH_LONG).show();

                // Save username and user role in shared preferences
                SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Username, username);
                editor.putString(UserRole, ADMIN);
                editor.apply();

                // Redirect to admin page
                Intent intent = new Intent(LoginAdminActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginAdminActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
            }
        });
    }
}