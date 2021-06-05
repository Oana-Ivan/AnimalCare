package com.example.animalcare.adopterOptions.recommendedAnimals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animalcare.R;

public class RecommendedAnimalsListFragment extends Fragment {

    public RecommendedAnimalsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommended_animals_list, container, false);
    }
}