package com.example.animalcare.adopterOptions.recommendedAnimals;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.animalcare.R;

public class RecommendedAnimalsTestFragment extends Fragment {
    private CheckBox q1a1, q1a2, q1a3, q2a1, q2a2, q2a3, q3a1, q3a2, q3a3;
    private RadioButton q4L, q4M, q4H, q5L, q5M, q5H;
    private AppCompatButton submitBtn;

    public RecommendedAnimalsTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_animals_test, container, false);
        initViews(view);

        submitBtn.setOnClickListener(b -> {
            if (!notAnsweredAllQuestions()) {
                calculateResults();
                RecommendedAnimalsActivity.recAnimalsFromTest = true;
                // Change to fragment Results
                RecommendedAnimalsListFragment resultsFragment = new RecommendedAnimalsListFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.activity_recommended_animals_fr, resultsFragment);
                ft.commit();
            }
        });

        return view;
    }

    // TODO results test
    private void calculateResults() {
    }

    private void initViews(View view) {
        q1a1 = view.findViewById(R.id.fragment_rec_test_q1_op1);
        q1a2 = view.findViewById(R.id.fragment_rec_test_q1_op2);
        q1a3 = view.findViewById(R.id.fragment_rec_test_q1_op3);
        q2a1 = view.findViewById(R.id.fragment_rec_test_q2_op1);
        q2a2 = view.findViewById(R.id.fragment_rec_test_q2_op2);
        q2a3 = view.findViewById(R.id.fragment_rec_test_q2_op3);
        q3a1 = view.findViewById(R.id.fragment_rec_test_q3_op1);
        q3a2 = view.findViewById(R.id.fragment_rec_test_q3_op2);
        q3a3 = view.findViewById(R.id.fragment_rec_test_q3_op3);

        q4L = view.findViewById(R.id.fragment_rec_test_q4_a1);
        q4M = view.findViewById(R.id.fragment_rec_test_q4_a2);
        q4H = view.findViewById(R.id.fragment_rec_test_q4_a3);
        q5L = view.findViewById(R.id.fragment_rec_test_q5_a1);
        q5M = view.findViewById(R.id.fragment_rec_test_q5_a2);
        q5H = view.findViewById(R.id.fragment_rec_test_q5_a3);

        submitBtn = view.findViewById(R.id.fragment_rec_test_btn_submit);

    }
    private boolean notAnsweredAllQuestions() {
        if (!q1a1.isChecked() && !q1a2.isChecked() && !q1a3.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 1!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q2a1.isChecked() && !q2a2.isChecked() && !q2a3.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 2!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q3a1.isChecked() && !q3a2.isChecked() && !q3a3.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 3!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q4L.isChecked() && !q4M.isChecked() && !q4H.isChecked()){
            Toast.makeText(getContext(), "Please answer question 4!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q5L.isChecked() && !q5M.isChecked() && !q5H.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 5!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}