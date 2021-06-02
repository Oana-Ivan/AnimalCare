package com.example.animalcare.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.models.BasicUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.ADOPTER;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;


public class LoginActivity extends AppCompatActivity {
    private EditText usernameET, passwordET;
    private CheckBox isVolunteerCB;
    private AppCompatButton loginBtn;
    private String username, password;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference usersCollection;
    public CollectionReference volunteersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewsInit();

        loginBtn.setOnClickListener(r -> {
            assignValues();
            if (!emptyFields()) {
                firestoreInit();
                if (isVolunteerCB.isChecked()) {
                    volunteersCollection.document(username).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            BasicUser user = document.toObject(BasicUser.class);
                            if (user != null) {
                                if (passwordsMatch(password, user.getHashPassword())) {
                                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                                    // Save username and user role in shared preferences
                                    SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Username, username);
                                    editor.putString(UserRole, VOLUNTEER);
                                    editor.apply();
                                    // TODO Redirect to volunteer page
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    });
                }
                else {
                    usersCollection.document(username).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            BasicUser user = document.toObject(BasicUser.class);
                            if (user != null) {
                                if (passwordsMatch(password, user.getHashPassword())) {
                                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_LONG).show();

                                    // Save username and user role in shared preferences
                                    SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Username, username);
                                    editor.putString(UserRole, ADOPTER);
                                    editor.apply();
                                    // TODO Redirect to user page
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    });
                }
            }
        });
    }

    private boolean passwordsMatch(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }

    private void viewsInit() {
        usernameET = findViewById(R.id.activity_login_et_username);
        passwordET = findViewById(R.id.activity_login_et_password);
        isVolunteerCB = findViewById(R.id.activity_login_cb_volunteer);
        loginBtn = findViewById(R.id.activity_login_btn_login);
    }

    private void assignValues() {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
    }

    private boolean emptyFields() {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
        volunteersCollection = db.collection("Volunteers");
    }
}
