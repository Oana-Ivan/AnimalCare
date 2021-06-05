package com.example.animalcare.adopterOptions.recommendedAnimals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.animalcare.R;

public class RecommendedAnimalsActivity extends AppCompatActivity {
    public static boolean recAnimalsFromTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_animals);
    }
}