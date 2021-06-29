package com.example.animalcare.adopterOptions.recommendedAnimals;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animalcare.R;

public class RecommendedAnimalsMainFragment extends Fragment {
    private AppCompatButton btnTest, btnResults;

    public RecommendedAnimalsMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_animals_main, container, false);
        btnTest = view.findViewById(R.id.fragment_recommended_main_btn_test);
        btnResults = view.findViewById(R.id.fragment_recommended_main_btn_results);

        btnTest.setOnClickListener(b -> {
            RecommendedAnimalsTestFragment testFragment = new RecommendedAnimalsTestFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_recommended_animals_fr, testFragment);
            ft.commit();
        });

        btnResults.setOnClickListener(b -> {
            RecommendedAnimalsActivity.recAnimalsFromTest = false;
            RecommendedAnimalsListFragment resultsFragment = new RecommendedAnimalsListFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_recommended_animals_fr, resultsFragment);
            ft.commit();
        });

        return view;
    }
}