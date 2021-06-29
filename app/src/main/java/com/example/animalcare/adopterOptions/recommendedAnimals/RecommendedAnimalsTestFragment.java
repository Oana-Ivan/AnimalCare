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
    private CheckBox q0a1, q0a2;
    private RadioButton q1a1, q1a2, q1a3, q2a1, q2a2, q3a1, q3a2, q3a3,  q4a1, q4a2, q4a3,  q5a1, q5a2;
    private AppCompatButton submitBtn;

    public static int attention;
    public static int caring;
    public static boolean wantsDog;
    public static boolean wantsCat;

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

    private void calculateResults() {
        int potentialProvidedAttention = 0;
        int potentialProvidedCaring = 0;

        // question 0
        wantsDog = (q0a1.isChecked());
        wantsCat = (q0a2.isChecked());

        // question 1
        potentialProvidedAttention += (q1a1.isChecked() ? 0 : (q1a2.isChecked() ? 20 : 10));
        potentialProvidedCaring += (q1a1.isChecked() ? 0 : (q1a2.isChecked() ? 20 : 10));

        // question 3
        potentialProvidedAttention += (q3a1.isChecked() ? 0 : (q3a2.isChecked() ? 10 : 20));
        potentialProvidedCaring += (q3a1.isChecked() ? 0 : (q3a2.isChecked() ? 10 : 20));

        // question 4
        potentialProvidedAttention += (q4a1.isChecked() ? 20 : (q4a2.isChecked() ? 10 : 0));

        // question 5
        potentialProvidedAttention += (q5a1.isChecked() ? 10 : 0);
        potentialProvidedCaring += (q5a1.isChecked() ? 10 : 0);

        attention = (potentialProvidedAttention < 20) ? 1 : ((potentialProvidedAttention < 40) ? 2 : 3);
        caring = (potentialProvidedCaring < 20) ? 1 : ((potentialProvidedCaring < 40) ? 2 : 3);
    }

    private void initViews(View view) {
        q0a1 = view.findViewById(R.id.fragment_rec_test_q0_op1);
        q0a2 = view.findViewById(R.id.fragment_rec_test_q0_op2);

        q1a1 = view.findViewById(R.id.fragment_rec_test_q1_op1);
        q1a2 = view.findViewById(R.id.fragment_rec_test_q1_op2);
        q1a3 = view.findViewById(R.id.fragment_rec_test_q1_op3);

        q2a1 = view.findViewById(R.id.fragment_rec_test_q2_op1);
        q2a2 = view.findViewById(R.id.fragment_rec_test_q2_op2);

        q3a1 = view.findViewById(R.id.fragment_rec_test_q3_op1);
        q3a2 = view.findViewById(R.id.fragment_rec_test_q3_op2);
        q3a3 = view.findViewById(R.id.fragment_rec_test_q3_op3);

        q4a1 = view.findViewById(R.id.fragment_rec_test_q4_a1);
        q4a2 = view.findViewById(R.id.fragment_rec_test_q4_a2);
        q4a3 = view.findViewById(R.id.fragment_rec_test_q4_a3);

        q5a1 = view.findViewById(R.id.fragment_rec_test_q5_a1);
        q5a2 = view.findViewById(R.id.fragment_rec_test_q5_a2);

        submitBtn = view.findViewById(R.id.fragment_rec_test_btn_submit);

    }

    private boolean notAnsweredAllQuestions() {
        if (!q0a1.isChecked() && !q0a2.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 1!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q1a1.isChecked() && !q1a2.isChecked() && !q1a3.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 2!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q2a1.isChecked() && !q2a2.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 3!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q3a1.isChecked() && !q3a2.isChecked() && !q3a3.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 4!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q4a1.isChecked() && !q4a2.isChecked() && !q4a3.isChecked()){
            Toast.makeText(getContext(), "Please answer question 5!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!q5a1.isChecked() && !q5a2.isChecked()) {
            Toast.makeText(getContext(), "Please answer question 6!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}