package com.example.animalcare.adopterOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.animalcare.CRUD.AddAnimalActivity;
import com.example.animalcare.R;
import com.example.animalcare.adopterOptions.roomDatabase.AdopterOptionsDatabase;
import com.example.animalcare.adopterOptions.roomDatabase.dao.SavedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.models.Visit;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class AddVisitActivity extends AppCompatActivity {
    private Spinner dayS, monthS, yearS, hourS, minutesS;
    private AppCompatButton addVisitBtn;
    private String[] day, month, year, hour, minutes;

    // cloud database
    private FirebaseFirestore db;
    private CollectionReference visitsCollection;
    private int collectionSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        init();

        addVisitBtn.setOnClickListener(b -> {
            // TODO Add checks for visit date

            // get the number of animals in collection
            visitsCollection.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        collectionSize++;
                    }
                    Toast.makeText(AddVisitActivity.this, "No of visits: " + collectionSize, Toast.LENGTH_LONG).show();
                    collectionSize++;
                    String visitID = "visit_" + collectionSize;

                    // Retrieve username from sharedPreferences
                    SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                    String username = sharedpreferences.getString(Username, "");

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
                }
            });

        });
    }

    private void init() {
        dayS = findViewById(R.id.activity_add_visit_s_choose_day);
        monthS = findViewById(R.id.activity_add_visit_s_choose_month);
        yearS = findViewById(R.id.activity_add_visit_s_choose_year);
        hourS = findViewById(R.id.activity_add_visit_s_choose_hour);
        minutesS = findViewById(R.id.activity_add_visit_s_choose_minutes);
        addVisitBtn = findViewById(R.id.activity_add_visit_btn);

        day = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
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