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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.LOG_ROUNDS;

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
            if (!fieldsEmpty() && !noWorkingDaySelected() && validInput()) {

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

    private boolean validInput() {

        if (!verifyDateFormat(startDate)) {
            Toast.makeText(AddVolunteerActivity.this, "Please enter a valid date! (Eg. 21/05/2021)", Toast.LENGTH_LONG).show();
            startDateET.setText("");
            return false;
        }
        if (!verifyRegex(startHour, "^(?:[9]|1[0-7]?)$")) {
            Toast.makeText(AddVolunteerActivity.this, "The starting hour should be a number between 9 and 17! (Eg. 9)", Toast.LENGTH_LONG).show();
            startHourET.setText("");
            return false;
        }
        if (!verifyRegex(endHour, "^(?:1[0-8]?)$")) {
            Toast.makeText(AddVolunteerActivity.this, "The ending hour should be a number between 10 and 18! (Eg. 14)", Toast.LENGTH_LONG).show();
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
