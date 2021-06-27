package com.example.animalcare.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.models.Volunteer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.LOG_ROUNDS;
import static com.example.animalcare.models.Volunteer.FRIDAY;
import static com.example.animalcare.models.Volunteer.MONDAY;
import static com.example.animalcare.models.Volunteer.THURSDAY;
import static com.example.animalcare.models.Volunteer.TUESDAY;
import static com.example.animalcare.models.Volunteer.WEDNESDAY;

public class UpdateVolunteerActivity extends AppCompatActivity {
    private Volunteer currentVolunteer;

    private EditText usernameET, firstNameET, lastNameET, emailET, passwordET, startDateET, startHourET, endHourET;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB;
    AppCompatButton updateVolunteerBtn;

    private String username, firstName, lastName, email, password, startDate, startHour, endHour;
    private int startHourInt, endHourInt;
    private List<String> workingDays;

    // cloud database
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_volunteer);

        initViews();

        updateVolunteerBtn.setOnClickListener(c -> {
            assignValues();
            if (!fieldsEmpty() && !noWorkingDaySelected()) {
                // hashing password
                String hashPwd = BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));

                workingDays = new ArrayList<>();
                if (mondayCB.isChecked())
                    workingDays.add(MONDAY);
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

                currentVolunteer.setUsername(username);
                currentVolunteer.setFirstName(firstName);
                currentVolunteer.setLastName(lastName);
                currentVolunteer.setEmail(email);
                if (!password.equals("-")) {
                    currentVolunteer.setHashPassword(hashPwd);
                }
                currentVolunteer.setStartingHour(startHourInt);
                currentVolunteer.setEndingHour(endHourInt);
                currentVolunteer.setWorkingDays(workingDays);
                currentVolunteer.setStartingDate(startDate);

                // Add volunteer to collection using username as unique ID
                db = FirebaseFirestore.getInstance();
                db.collection("Volunteers").document(currentVolunteer.getUsername())
                        .set(currentVolunteer)
                        .addOnSuccessListener((OnSuccessListener) o -> {
                            Log.d(TAG, "Updated volunteer");
                            finish();
                            startActivity(new Intent(UpdateVolunteerActivity.this, VolunteersListActivity.class));
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error updating volunteer", e));

            }

        });
    }

    private void initViews() {
        currentVolunteer = (Volunteer) getIntent().getSerializableExtra("VOLUNTEER");

        usernameET = findViewById(R.id.activity_update_volunteer_et_username);
        firstNameET = findViewById(R.id.activity_update_volunteer_et_first_name);
        lastNameET = findViewById(R.id.activity_update_volunteer_et_last_name);
        emailET = findViewById(R.id.activity_update_volunteer_et_email);
        passwordET = findViewById(R.id.activity_update_volunteer_et_password);
        startDateET = findViewById(R.id.activity_update_volunteer_et_starting_date);
        startHourET = findViewById(R.id.activity_update_volunteer_et_starting_hour);
        endHourET = findViewById(R.id.activity_update_volunteer_et_ending_hour);

        mondayCB = findViewById(R.id.activity_update_volunteer_cb_monday);
        tuesdayCB = findViewById(R.id.activity_update_volunteer_cb_tuesday);
        wednesdayCB = findViewById(R.id.activity_update_volunteer_cb_wednesday);
        thursdayCB = findViewById(R.id.activity_update_volunteer_cb_thursday);
        fridayCB = findViewById(R.id.activity_update_volunteer_cb_friday);

        updateVolunteerBtn = findViewById(R.id.activity_update_volunteer_btn_update);

        // Default values
        usernameET.setText(currentVolunteer.getUsername());
        firstNameET.setText(currentVolunteer.getFirstName());
        lastNameET.setText(currentVolunteer.getLastName());
        emailET.setText(currentVolunteer.getEmail());
        passwordET.setText("-");
        startDateET.setText(currentVolunteer.getStartingDate());
        startHourET.setText(String.valueOf(currentVolunteer.getStartingHour()));
        endHourET.setText(String.valueOf(currentVolunteer.getEndingHour()));

        if (currentVolunteer.getWorkingDays().contains(MONDAY)) {
            mondayCB.setChecked(true);
        }
        if (currentVolunteer.getWorkingDays().contains(TUESDAY)) {
            tuesdayCB.setChecked(true);
        }
        if (currentVolunteer.getWorkingDays().contains(WEDNESDAY)) {
            wednesdayCB.setChecked(true);
        }
        if (currentVolunteer.getWorkingDays().contains(THURSDAY)) {
            thursdayCB.setChecked(true);
        }
        if (currentVolunteer.getWorkingDays().contains(FRIDAY)) {
            fridayCB.setChecked(true);
        }
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
            Toast.makeText(UpdateVolunteerActivity.this, "All fields required!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean noWorkingDaySelected() {
        if (!mondayCB.isChecked() && !tuesdayCB.isChecked() && !wednesdayCB.isChecked() && !thursdayCB.isChecked() && !fridayCB.isChecked()) {
            Toast.makeText(UpdateVolunteerActivity.this, "Select at least one working day!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}