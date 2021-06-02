package com.example.animalcare.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.models.BasicUser;
import com.example.animalcare.roomDatabase.AppDatabase;
import com.example.animalcare.roomDatabase.dao.UserDAO;
import com.example.animalcare.roomDatabase.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameET, lastNameET, usernameET, emailET, passwordET, verifyPasswordET;
    private AppCompatButton btnRegister;
    private String firstName, lastName, username, email, password, verifyPassword;
    private static final int LOG_ROUNDS = 5;
    public static final String UserPREFERENCES = "UserPreferences";
    public static final String Username = "username";
    public static final String UserRole = "UserRole";
    public static final String ADOPTER = "ADOPTER";
    public static final String VOLUNTEER = "VOLUNTEER";
    public static final String ADMIN = "ADMIN";
    int userId = 0;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewsInit();
        firestoreInit();

        btnRegister.setOnClickListener(r -> {
            // get values of views (information provided by user)
            assignValues();

            // if user provided all information (all fields are required)
            if (!emptyFields()) {

                // verify if the username already exists
                usersCollection.document(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Username taken");
                            Toast.makeText(RegisterActivity.this, "Username taken", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Try adding new user");
                            if (passwordsMatch()) {
                                // hashing password
                                String hashPwd = BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));

                                // create new user object
                                BasicUser user = new BasicUser(username, firstName, lastName, email, hashPwd);

                                // Add user to collection using username as unique ID
                                usersCollection.document(username)
                                        .set(user)
                                        .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Added new user"))
                                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                                // Save username and user role in shared preferences
                                SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Username, username);
                                editor.putString(UserRole, ADOPTER);
                                editor.apply();

                                // TODO Redirect to user page
                            }
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                });
            }
        });
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
    }

    private void viewsInit() {
        usernameET = findViewById(R.id.activity_register_et_username);
        firstNameET = findViewById(R.id.activity_register_et_first_name);
        lastNameET = findViewById(R.id.activity_register_et_last_name);
        emailET = findViewById(R.id.activity_register_et_email);
        passwordET = findViewById(R.id.activity_register_et_password);
        verifyPasswordET = findViewById(R.id.activity_register_et_password_2);
        btnRegister = findViewById(R.id.activity_register_btn_register);
    }

    private void assignValues() {
        username = usernameET.getText().toString();
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        verifyPassword = verifyPasswordET.getText().toString();
    }
    private boolean emptyFields() {
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "All fields required", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean passwordsMatch() {
        // check if passwords match
        if (!password.equals(verifyPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            passwordET.setText("");
            verifyPasswordET.setText("");
            return false;
        }
        return true;
    }
}
