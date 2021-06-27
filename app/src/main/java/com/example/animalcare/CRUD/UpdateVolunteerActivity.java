package com.example.animalcare.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.models.Volunteer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.LOG_ROUNDS;
import static com.example.animalcare.models.Volunteer.FRIDAY;
import static com.example.animalcare.models.Volunteer.MONDAY;
import static com.example.animalcare.models.Volunteer.THURSDAY;
import static com.example.animalcare.models.Volunteer.TUESDAY;
import static com.example.animalcare.models.Volunteer.WEDNESDAY;

public class UpdateVolunteerActivity extends AppCompatActivity {
    private Volunteer currentVolunteer;

    private TextView usernameTV;
    private EditText firstNameET, lastNameET, emailET, passwordET, startDateET, startHourET, endHourET;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB;
    AppCompatButton updateVolunteerBtn;

    private String firstName, lastName, email, password, startDate, startHour, endHour;
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
            if (!fieldsEmpty() && !noWorkingDaySelected() && validInput()) {
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

        usernameTV = findViewById(R.id.activity_update_volunteer_tv_username);
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
        String username = "Volunteer: " + currentVolunteer.getUsername();
        usernameTV.setText(username);
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
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        startDate = startDateET.getText().toString();
        startHour = startHourET.getText().toString();
        endHour = endHourET.getText().toString();
    }

    private boolean fieldsEmpty() {
        if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || startDate.equals("") || startHour.equals("") || endHour.equals("")){
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

    private boolean validInput() {

        if (!verifyDateFormat(startDate)) {
            Toast.makeText(UpdateVolunteerActivity.this, "Please enter a valid date! (Eg. 21/05/2021)", Toast.LENGTH_LONG).show();
            startDateET.setText("");
            return false;
        }
        if (!verifyRegex(startHour, "^(?:[9]|1[0-7]?)$")) {
            Toast.makeText(UpdateVolunteerActivity.this, "The starting hour should be a number between 9 and 17! (Eg. 9)", Toast.LENGTH_LONG).show();
            startHourET.setText("");
            return false;
        }
        if (!verifyRegex(endHour, "^(?:1[0-8]?)$")) {
            Toast.makeText(UpdateVolunteerActivity.this, "The ending hour should be a number between 10 and 18! (Eg. 14)", Toast.LENGTH_LONG).show();
            endHourET.setText("");
            return false;
        }

        if (Integer.parseInt(startHour) > Integer.parseInt(endHour)) {
            Toast.makeText(UpdateVolunteerActivity.this, "The ending hour should be after the starting hour (starting hour: " + startHour + ")" , Toast.LENGTH_LONG).show();
            endHourET.setText("");
            return false;
        }

        return true;
    }

    private boolean verifyRegex (String input, String regex) {
        // compiling regex
        Pattern p = Pattern.compile(regex);

        // Creates a matcher that will match input1 against regex
        Matcher m = p.matcher(input);

        // If match found and equal to input1
        return m.find() && m.group().equals(input);
    }

    private boolean verifyDateFormat (String input) {
        // regular expression for a floating point number
        String regex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

        // compiling regex
        Pattern p = Pattern.compile(regex);

        // Creates a matcher that will match input1 against regex
        Matcher m = p.matcher(input);

        // If match found and equal to input
        return m.find() && m.group().equals(input);
    }
}