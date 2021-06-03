package com.example.animalcare.CRUD;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAnimalActivity extends AppCompatActivity {
    private EditText arrivingDateET, ageET, colorET, descriptionET;
    private RadioButton maleRB, femaleRB, dogRB, catRB, activRB, middleRB, inactivRB, smallRB, mediumRB, bigRB;
    private CheckBox diseaseCB;
    private AppCompatButton addAnimalBtn;

    private String arrivingDate, age, color, description;
    private double ageD;
    private String gender, species, animalID, image;
    private boolean disease;
    private int personalityType, size;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);

        initViews();

        addAnimalBtn.setOnClickListener(a -> {
            assignData();
            firestoreInit();

            if (!emptyFields() && radioSelected()) {
                // TODO Insert in database
                // TODO animalID si img
                Animal newAnimal = new Animal(arrivingDate, ageD, gender, species, color, description, disease, personalityType, size, animalID, image);
            }
        });

    }

    private boolean radioSelected() {
        if (!maleRB.isChecked() && !femaleRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for gender", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!dogRB.isChecked() && !catRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for species", Toast.LENGTH_LONG).show();
            return false;
        }
        if (activRB.isChecked() && middleRB.isChecked() && inactivRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for personality", Toast.LENGTH_LONG).show();
            return false;
        }
        if (smallRB.isChecked() && mediumRB.isChecked() && bigRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for size", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean emptyFields() {
        if (arrivingDate.equals("") || age.equals("") || color.equals("") || description.equals("")) {
            Toast.makeText(AddAnimalActivity.this, "All fields required!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");
    }



    private void assignData() {
        arrivingDate = arrivingDateET.getText().toString();
        age = ageET.getText().toString();
        ageD = Double.parseDouble(age);
        color = colorET.getText().toString();
        description = descriptionET.getText().toString();

        gender = (maleRB.isChecked() ? Animal.MALE : Animal.FEMALE);
        species = (dogRB.isChecked() ? Animal.DOG : Animal.CAT);
        disease = diseaseCB.isChecked();
        personalityType = (activRB.isChecked() ? 3 : (inactivRB.isChecked() ? 1 : 2));
        size = (bigRB.isChecked() ? 3 : (mediumRB.isChecked() ? 2 : 1));

    }

    private void initViews() {
        arrivingDateET = findViewById(R.id.activity_add_animal_et_arriving_date);
        ageET = findViewById(R.id.activity_add_animal_et_age);
        colorET = findViewById(R.id.activity_add_animal_et_color);
        descriptionET = findViewById(R.id.activity_add_animal_et_description);
        maleRB = findViewById(R.id.activity_add_animal_rb_male);
        femaleRB = findViewById(R.id.activity_add_animal_rb_female);
        dogRB = findViewById(R.id.activity_add_animal_rb_dog);
        catRB = findViewById(R.id.activity_add_animal_rb_cat);
        activRB = findViewById(R.id.activity_add_animal_rb_active);
        middleRB = findViewById(R.id.activity_add_animal_rb_activ_inactiv);
        inactivRB = findViewById(R.id.activity_add_animal_rb_inactiv);
        smallRB = findViewById(R.id.activity_add_animal_rb_small);
        mediumRB = findViewById(R.id.activity_add_animal_rb_medium);
        bigRB = findViewById(R.id.activity_add_animal_rb_big);
        diseaseCB = findViewById(R.id.activity_add_animal_cb_disease);
        addAnimalBtn = findViewById(R.id.activity_add_animal_btn_add);
    }
}