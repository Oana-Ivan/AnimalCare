package com.example.animalcare.animalsActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animalcare.CRUD.AnimalsListActivity;
import com.example.animalcare.MainActivity;
import com.example.animalcare.R;
import com.example.animalcare.SplashScreenActivity;
import com.example.animalcare.models.Animal;

import static com.example.animalcare.authentication.RegisterActivity.ADMIN;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.UserRole;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.authentication.RegisterActivity.VOLUNTEER;

public class AnimalDetailsActivity extends AppCompatActivity {
    private Animal currentAnimal;
    private ImageView animalImg;
    private TextView animalInfoTV1, animalInfoTV2, caringInfoTV, animalSpeciesTV;
    private AppCompatButton btnAllAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        currentAnimal = (Animal) getIntent().getSerializableExtra("Animal");

        animalImg = findViewById(R.id.activity_animal_details_img);
        animalInfoTV1 = findViewById(R.id.activity_animal_details_tv_info_1);
        animalInfoTV2 = findViewById(R.id.activity_animal_details_tv_info_2);
        caringInfoTV = findViewById(R.id.activity_animal_details_tv_caring_informations);
        animalSpeciesTV = findViewById(R.id.activity_animal_details_tv_needs);
        btnAllAnimals = findViewById(R.id.activity_animal_details_btn_all_animals);

        // Set animal image
        Glide.with(this).load(currentAnimal.getImage()).into(animalImg);

        // Set text for information about animal
        String animalInfo1 = currentAnimal.getSpecies();
        String animalInfo2 = (currentAnimal.getGender().equals(Animal.FEMALE) ? "F" : "M") + ", " + currentAnimal.getAge()
                + " years, size: " + ((currentAnimal.getSize() == 1) ? "small" : ((currentAnimal.getSize() == 2) ? "medium" : "big"))
                + ( (currentAnimal.getBreed() != null) ? "\nBreed: " + currentAnimal.getBreed() : "")
                + "\nDescription: " + currentAnimal.getDescription()
                + "\nPersonality: " + ((currentAnimal.getPersonalityType() == 1) ? "inactive" : ((currentAnimal.getPersonalityType() == 2) ? "medium" : "active"))
                + "\nCaring level required: " + ((currentAnimal.getAttentionLevelRequired() == 1) ? "small" : ((currentAnimal.getAttentionLevelRequired() == 2) ? "medium" : "high"))
                +  (currentAnimal.hasDisease() ? "\n\"Suspected of disease" : "\nHealthy");
        animalInfoTV1.setText(animalInfo1);
        animalInfoTV2.setText(animalInfo2);

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
        String caringInfo = "";
        // TODO Add facts about animal
        if (currentAnimal.getSpecies().equals(Animal.DOG)) {
            caringInfo += "- Food and water;\n - Places to sleep\n - Exercise\n - Basic supplies\n - Grooming\n";
            if (currentAnimal.getAge() < 1) {
                caringInfo += "A junior dog will require more attention\n";
            }
            else caringInfo += ""; //"old\n";
        }
        else {
            caringInfo += "pisi\n";
            if (currentAnimal.getAge() < 1) {
                caringInfo += "junior\n";
            }
            else caringInfo += ""; //"old\n";
        }
        String needs = (currentAnimal.getSpecies().equals(Animal.DOG) ? "Dog needs" : "Cat needs");
        animalSpeciesTV.setText(needs);
        caringInfoTV.setText(caringInfo);

        // Redirect to All animals
        btnAllAnimals.setOnClickListener(b -> {
            startActivity(new Intent(AnimalDetailsActivity.this, AnimalsListActivity.class));
        });

    }
}