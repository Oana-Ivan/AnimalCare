package com.example.animalcare.adminAndVolunteerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animalcare.R;
import com.example.animalcare.authentication.RegisterActivity;
import com.example.animalcare.models.Ticket;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class AddTicketActivity extends AppCompatActivity {
    private EditText titleET, textET;
    private AppCompatButton addBtn;
    private String title, text;

    // cloud database
    private FirebaseFirestore db;
    private CollectionReference ticketsCollection;
    private static int ticketsSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        titleET = findViewById(R.id.activity_add_ticket_et_title);
        textET = findViewById(R.id.activity_add_ticket_et_text);
        addBtn = findViewById(R.id.activity_add_ticket_btn_add_ticket);

        db = FirebaseFirestore.getInstance();
        ticketsCollection = db.collection("Tickets");

        addBtn.setOnClickListener(b -> {
            title = titleET.getText().toString();
            text = textET.getText().toString();

            if (title.equals("") || text.equals("")) {
                Toast.makeText(AddTicketActivity.this, "Fill both fields!", Toast.LENGTH_LONG).show();
            }
            else {
                // get the number of tickets in collection to generate new ID
                ticketsCollection.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ticketsSize++;
                        }
                        String id = "ticket_" + (ticketsSize + 1);

                        // Retrieve username from sharedPreferences
                        SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
                        String username = sharedpreferences.getString(Username, "");

                        Ticket newTicket = new Ticket(id, username, title, text);

                        // Add ticket to collection
                        ticketsCollection.document(id)
                                .set(newTicket)
                                .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Added new ticket"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                        // Redirect to TicketsListActivity
                        finish();
                        startActivity(new Intent(AddTicketActivity.this, TicketsListActivity.class));
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

            }
        });

    }
}