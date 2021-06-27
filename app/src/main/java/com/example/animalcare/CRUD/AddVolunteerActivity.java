package com.example.animalcare.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.authentication.LoginActivity;
import com.example.animalcare.models.BasicUser;
import com.example.animalcare.models.Volunteer;
import com.example.animalcare.usersMainScreens.VolunteerHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.LOG_ROUNDS;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class AddVolunteerActivity extends AppCompatActivity {
    private EditText usernameET, firstNameET, lastNameET, emailET, passwordET, startDateET, startHourET, endHourET;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB;
    AppCompatButton addVolunteerBtn;

    private String username, firstName, lastName, email, password, startDate, startHour, endHour;
    private int startHourInt, endHourInt;
    private List<String> workingDays;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference volunteersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_volunteer);
        initViews();

        addVolunteerBtn.setOnClickListener(c -> {
            assignValues();
            if (!fieldsEmpty() && !noWorkingDaySelected()) {

                // verify if volunteer already exists in the data base
                db = FirebaseFirestore.getInstance();
                volunteersCollection = db.collection("Volunteers");

                volunteersCollection.document(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Volunteer volunteer = document.toObject(Volunteer.class);
                        if (volunteer != null) {
                            Toast.makeText(AddVolunteerActivity.this, "Username taken", Toast.LENGTH_LONG).show();
                            usernameET.setText("");
                        }
                        else {
                            // hashing password
                            String hashPwd = BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));

                            workingDays = new ArrayList<>();
                            if (mondayCB.isChecked())
                                workingDays.add(Volunteer.MONDAY);
                            if (tuesdayCB.isChecked())
                                workingDays.add(Volunteer.TUESDAY);
                            if (wednesdayCB.isChecked())
                                workingDays.add(Volunteer.WEDNESDAY);
                            if (thursdayCB.isChecked())
                                workingDays.add(Volunteer.THURSDAY);
                            if (fridayCB.isChecked())
                                workingDays.add(Volunteer.FRIDAY);

                            startHourInt = Integer.parseInt(startHourET.getText().toString());
                            endHourInt = Integer.parseInt(endHourET.getText().toString());
                            Volunteer newVolunteer = new Volunteer(username, firstName, lastName, email, hashPwd, startHourInt, endHourInt, workingDays, startDate);

                            // Add volunteer to collection using username as unique ID
                            volunteersCollection.document(username)
                                    .set(newVolunteer)
                                    .addOnSuccessListener((OnSuccessListener) o -> {
                                        Log.d(TAG, "Added new volunteer");
                                        finish();
                                        startActivity(new Intent(AddVolunteerActivity.this, VolunteersListActivity.class));
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                });

            }

        });
    }

    private void initViews() {
        usernameET = findViewById(R.id.activity_add_volunteer_et_username);
        firstNameET = findViewById(R.id.activity_add_volunteer_et_first_name);
        lastNameET = findViewById(R.id.activity_add_volunteer_et_last_name);
        emailET = findViewById(R.id.activity_add_volunteer_et_email);
        passwordET = findViewById(R.id.activity_add_volunteer_et_password);
        startDateET = findViewById(R.id.activity_add_volunteer_et_starting_date);
        startHourET = findViewById(R.id.activity_add_volunteer_et_starting_hour);
        endHourET = findViewById(R.id.activity_add_volunteer_et_ending_hour);

        mondayCB = findViewById(R.id.activity_add_volunteer_cb_monday);
        tuesdayCB = findViewById(R.id.activity_add_volunteer_cb_tuesday);
        wednesdayCB = findViewById(R.id.activity_add_volunteer_cb_wednesday);
        thursdayCB = findViewById(R.id.activity_add_volunteer_cb_thursday);
        fridayCB = findViewById(R.id.activity_add_volunteer_cb_friday);

        addVolunteerBtn = findViewById(R.id.activity_add_volunteer_btn_add);
    }

    private void assignValues() {
        username = usernameET.getText().toString();
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        startDate = startDateET.getText().toString();
        startHour = startHourET.getText().toString();
        endHour = endHourET.getText().toString();
    }

    private boolean fieldsEmpty() {
        if (username.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || startDate.equals("") || startHour.equals("") || endHour.equals("")){
            Toast.makeText(AddVolunteerActivity.this, "All fields required!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean noWorkingDaySelected() {
        if (!mondayCB.isChecked() && !tuesdayCB.isChecked() && !wednesdayCB.isChecked() && !thursdayCB.isChecked() && !fridayCB.isChecked()) {
            Toast.makeText(AddVolunteerActivity.this, "Select at least one working day!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
