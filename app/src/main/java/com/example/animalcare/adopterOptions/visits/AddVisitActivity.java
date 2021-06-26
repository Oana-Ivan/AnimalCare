package com.example.animalcare.adopterOptions.visits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.AdopterOptionsDatabase;
import com.example.animalcare.adopterOptions.roomDatabase.dao.SavedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.models.Visit;
import com.example.animalcare.usersMainScreens.AdopterHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class AddVisitActivity extends AppCompatActivity {
    private Spinner dayS, monthS, yearS, hourS, minutesS;
    private String[] day, month, year, hour, minutes;
    private AppCompatButton addVisitBtn;
    private TextView previousVisitsTV;
    private RecyclerView previousVisitsRV;

    private PreviousVisitsAdapter visitsAdapter;
    private RecyclerView.LayoutManager visitsLayoutManager;

    // cloud database
    private FirebaseFirestore db;
    private CollectionReference visitsCollection;
    private int collectionSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        init();

        // Retrieve username from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Username, "");

        addVisitBtn.setOnClickListener(b -> {
            // Check visit date and hour
            if (validateDateAndHour()) {
                // get the number of animals in collection
                visitsCollection.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            collectionSize++;
                        }
                        Toast.makeText(AddVisitActivity.this, "No of visits: " + collectionSize, Toast.LENGTH_LONG).show();
                        collectionSize++;
                        String visitID = "visit_" + collectionSize;


                        // Get ids for animals saved by user
                        List<String> ids = new ArrayList<>();
                        // Retrieve all saved animals from Room Database
                        AdopterOptionsDatabase db = Room.databaseBuilder(this, AdopterOptionsDatabase.class, "db-app").allowMainThreadQueries().build();
                        SavedAnimalDAO savedAnimalDAO = db.savedAnimalDAO();
                        List<SavedAnimal> savedAnimals = savedAnimalDAO.findByAdopterID(username);
                        for (int i = 0; i < savedAnimals.size(); i++) {
                            ids.add(savedAnimals.get(i).getAnimalId());
                        }

                        // Create new visit
                        Visit visit = new Visit(visitID, username, Integer.parseInt(dayS.getSelectedItem().toString()),
                                Integer.parseInt(monthS.getSelectedItem().toString()), Integer.parseInt(yearS.getSelectedItem().toString()),
                                Integer.parseInt(hourS.getSelectedItem().toString()), Integer.parseInt(minutesS.getSelectedItem().toString()), ids);

                        // Add visit to collection
                        visitsCollection.document(visitID)
                                .set(visit)
                                .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Added new visit"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                        finish();
                        startActivity(new Intent(AddVisitActivity.this, AdopterHomeActivity.class));
                    }
                });
            }
        });

        visitsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Visit> visits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Visit currentVisit = document.toObject(Visit.class);
                    if (currentVisit.getAdopterUsername().equals(username)) {
                        visits.add(currentVisit);
                    }
                }
                if (visits.size() == 0) {
                    previousVisitsTV.setText("You not have previous visits added.");
                } else {
                    visitsAdapter = new PreviousVisitsAdapter(visits);
                    previousVisitsRV.setHasFixedSize(true);
                    visitsLayoutManager = new LinearLayoutManager(this);

                    previousVisitsRV.setLayoutManager(visitsLayoutManager);
                    previousVisitsRV.setAdapter(visitsAdapter);

                    // click on image
                    visitsAdapter.setOnItemClickListener(position -> {
                        Toast.makeText(AddVisitActivity.this, visits.get(position).toString(), Toast.LENGTH_SHORT).show();
                        visits.get(position).setStatus(Visit.STATUS_OFF);

                        // Update status of visit in collection
                        visitsCollection.document(visits.get(position).getVisitID())
                                .set(visits.get(position))
                                .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Updated visit"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
                    });
                }
                Log.d(TAG, visits.toString());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    private boolean validateDateAndHour() {
        LocalDate currentDate = LocalDate.now();
        try {
            LocalDate scheduleDate = LocalDate.of(Integer.parseInt(yearS.getSelectedItem().toString()),
                    Integer.parseInt(monthS.getSelectedItem().toString()),
                    Integer.parseInt(dayS.getSelectedItem().toString()));

            // Verify that the date is in the future
            if (scheduleDate.isBefore(currentDate)) {
                Toast.makeText(this, "Please choose a date in the future!", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Verify that the day is a working day
            DayOfWeek dayOfWeek = scheduleDate.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                Toast.makeText(this, "Please choose a working day!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (DateTimeException e) {
            int d = Integer.parseInt(dayS.getSelectedItem().toString());
            int m = Integer.parseInt(monthS.getSelectedItem().toString());
            int y = Integer.parseInt(yearS.getSelectedItem().toString());
            String date = ((d < 10) ? "0" + d : d) + "." + ((m < 10) ? "0" + m : m) + "." + y;
            Toast.makeText(this, "Please choose a valid date! " + date + " is not valid!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void init() {
        dayS = findViewById(R.id.activity_add_visit_s_choose_day);
        monthS = findViewById(R.id.activity_add_visit_s_choose_month);
        yearS = findViewById(R.id.activity_add_visit_s_choose_year);
        hourS = findViewById(R.id.activity_add_visit_s_choose_hour);
        minutesS = findViewById(R.id.activity_add_visit_s_choose_minutes);
        addVisitBtn = findViewById(R.id.activity_add_visit_btn);
        previousVisitsTV = findViewById(R.id.activity_add_visit_tv_previous_visits);
        previousVisitsRV = findViewById(R.id.activity_add_visit_rv_visits_list);

        day = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        month = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        year = new String[]{"2021", "2022", "2023"};
        hour = new String[]{"9", "10", "11", "12", "13", "14", "15", "16", "17"};
        minutes = new String[]{"00", "15", "30", "45"};

        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, day);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
        ArrayAdapter<String> adapterHour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hour);
        ArrayAdapter<String> adapterMinutes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);

        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dayS.setAdapter(adapterDay);
        monthS.setAdapter(adapterMonth);
        yearS.setAdapter(adapterYear);
        hourS.setAdapter(adapterHour);
        minutesS.setAdapter(adapterMinutes);

        db = FirebaseFirestore.getInstance();
        visitsCollection = db.collection("Visits");
    }
}