package com.example.animalcare.animalsActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Animal;

import static com.example.animalcare.authentication.RegisterActivity.ADMIN;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class AnimalDetailsActivity extends AppCompatActivity {
    private Animal currentAnimal;
    private ImageView animalImg;
    private TextView animalInfoTV1, animalInfoTV2, animalInfoTV3, caringInfoTV, animalSpeciesTV, availabilityTV;
    private AppCompatButton btnAllAnimals;

    public static final String CurrentAnimal = "CurrentAnimal";
    public static final String currentAnimalID = "currentAnimalID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        currentAnimal = (Animal) getIntent().getSerializableExtra("Animal");

        // Save animal id in sharedPreferences
        SharedPreferences sharedpreferencesA = getSharedPreferences(CurrentAnimal, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferencesA.edit();
        editor.putString(currentAnimalID, currentAnimal.getAnimalID());
        editor.apply();

        animalImg = findViewById(R.id.activity_animal_details_img);
        animalInfoTV1 = findViewById(R.id.activity_animal_details_tv_info_1);
        animalInfoTV2 = findViewById(R.id.activity_animal_details_tv_info_2);
        animalInfoTV3 = findViewById(R.id.activity_animal_details_tv_info_3);
        caringInfoTV = findViewById(R.id.activity_animal_details_tv_caring_informations);
        animalSpeciesTV = findViewById(R.id.activity_animal_details_tv_needs);
        availabilityTV = findViewById(R.id.activity_animal_details_tv_availability);
        btnAllAnimals = findViewById(R.id.activity_animal_details_btn_all_animals);

        // Set animal image
        if (currentAnimal.getImage() != null) {
            Glide.with(this).load(currentAnimal.getImage()).into(animalImg);
        }
        else {
//            animalImg.setBackgroundResource(R.drawable.paw);
        }

        // Set text for information about animal
        String animalInfo1 = currentAnimal.getSpecies();
        String animalInfo2 = (currentAnimal.getGender().equals(Animal.FEMALE) ? "F" : "M") + ", " + currentAnimal.getAge()
                + " years, size: " + ((currentAnimal.getSize() == 1) ? "small" : ((currentAnimal.getSize() == 2) ? "medium" : "big"))
                + ( (currentAnimal.getBreed() != null) ? "\nBreed: " + currentAnimal.getBreed() : "")
                + "\nDescription: " + currentAnimal.getDescription()
                + "\nPersonality: " + ((currentAnimal.getPersonalityType() == 1) ? "inactive" : ((currentAnimal.getPersonalityType() == 2) ? "medium" : "active"))
                + "\nCaring level required: " + ((currentAnimal.getCaringLevelRequired() == 1) ? "small" : ((currentAnimal.getCaringLevelRequired() == 2) ? "medium" : "high"))
                +  (currentAnimal.getDisease() ? "\nSuspected of disease" : "\nHealthy");
        animalInfoTV1.setText(animalInfo1);
        animalInfoTV2.setText(animalInfo2);

        // Set other information
        String animalInfo3 = "Arriving date: " + currentAnimal.getArrivingDate() + "\nColor: " + currentAnimal.getColor()
                + "\nAttention level required: " + ((currentAnimal.getAttentionLevelRequired() == 1) ? "small" : ((currentAnimal.getAttentionLevelRequired() == 2) ? "medium" : "high"));
        animalInfoTV3.setText(animalInfo3);

        // Set availability
        if (currentAnimal.getWasAdopted()) {
            availabilityTV.setVisibility(View.VISIBLE);
        }

        // Retrieve userRole from sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        String userRole = sharedpreferences.getString(UserRole, "");

        // Change options if userRole == ADMIN or VOLUNTEER
        if (userRole.equals(ADMIN) || userRole.equals(VOLUNTEER)) {
            OptionsFragment optionsFragment = new OptionsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_animal_details_fr_options, optionsFragment);
            ft.commit();
        }

        // Show caring information based on animal characteristics

        String needs = (currentAnimal.getSpecies().equals(Animal.DOG) ? "Dog needs" : "Cat needs");
        animalSpeciesTV.setText(needs);

        String caringInfo = "";
        if (currentAnimal.getSpecies().equals(Animal.DOG)) {
            caringInfo += "BASIC\n - Food and water;\n - Places to sleep;\n - Exercise;\n - Basic supplies;\n - Grooming.\n";
            if (currentAnimal.getAge() <= 1) {
                caringInfo += "\nBASED ON AGE\nA junior dog will require more attention, it will have a lot of energy and will require more checkups to the veterinarian.\n";
            }
            else caringInfo += "\nBASED ON AGE\nAn older dog will need less attention.";
        }
        else {
            caringInfo += "BASIC\n - Food and water;\n - Places to sleep;\n - Basic supplies.\n";
            if (currentAnimal.getAge() < 1) {
                caringInfo += "\nBASED ON AGE\nA junior cat will require more attention, it will have a lot of energy and will require more checkups to the veterinarian.\n";
            }
            else caringInfo += "\nBASED ON AGE\nAn older dog will need less attention.\n";
        }

        if (currentAnimal.getDisease()) {
            caringInfo += "\nILLNESSES\nThis animal is suspected of disease, so you need to be prepared to attend to his needs correspondingly. \nIt can give you a lot of love, but it will probably need more of your time.\n";
        }
        else {
            caringInfo += "\nILLNESSES\nThis animal is healthy based on the opinion of the shelter staff.";
        }

        if (currentAnimal.getAttentionLevelRequired() == 1) {
            caringInfo += "\n\nREQUIRED ATTENTION\nBased on the characteristics of the animal, it will require a low volume of attention.\nThe level of attention is based on characteristics such as age, personality, sex, etc.";
        } else if (currentAnimal.getAttentionLevelRequired() == 2) {
            caringInfo += "\n\nREQUIRED ATTENTION\nBased on the characteristics of the animal, it will require an average volume of attention.\nThe level of attention is based on characteristics such as age, personality, gender, etc.";
        } else if (currentAnimal.getAttentionLevelRequired() == 3) {
            caringInfo += "\n\nREQUIRED ATTENTION\nBased on the characteristics of the animal, it will require an additional volume of attention.\nThe level of attention is based on characteristics such as age, personality, gender, etc.";
        }


        if (currentAnimal.getCaringLevelRequired() == 1) {
            caringInfo += "\n\n! REQUIRED CARE\nBased on the characteristics of the animal, it will require a low volume of care.\nThe level of care is based on characteristics such as age, personality, gender, etc.";
        } else if (currentAnimal.getCaringLevelRequired() == 2) {
            caringInfo += "\n\n!! REQUIRED CARE\nBased on the characteristics of the animal, it will require an average volume of care.\nThe level of care is based on characteristics such as age, personality, gender, etc.";
        } else if (currentAnimal.getCaringLevelRequired() == 3) {
            caringInfo += "\n\n!!! REQUIRED CARE\nBased on the characteristics of the animal, it will require an additional volume of care.\nThe level of care is based on characteristics such as age, personality, gender, etc.";
        }

        caringInfoTV.setText(caringInfo);

        // Redirect to All animals
        btnAllAnimals.setOnClickListener(b -> {
            finish();
            startActivity(new Intent(AnimalDetailsActivity.this, AnimalsListActivity.class));
        });

    }
}