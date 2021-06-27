package com.example.animalcare.adminAndVolunteerOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.animalcare.R;
import com.example.animalcare.models.Animal;
import com.example.animalcare.models.Ticket;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;
import static com.example.animalcare.models.Ticket.CLOSED;
import static com.example.animalcare.models.Ticket.OPEN;
import static com.example.animalcare.models.Visit.STATUS_OFF;

public class TicketDetailsActivity extends AppCompatActivity {
    private TextView titleTV, textTV, usernameTV, closedTV, updateTV;
    private String title, text, username, closed, update;
    private Ticket currentTicket;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference ticketsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        initViews();

        if (currentTicket.getStatus().equals(OPEN)) {
            closedTV.setOnClickListener(c -> {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Tickets - " + currentTicket.getTitle())
                        .setMessage("Are you sure you want to mark this ticket as closed?")
                        .setPositiveButton("Yes", (dialog1, which) -> {
                            currentTicket.setStatus(CLOSED);
                            db = FirebaseFirestore.getInstance();
                            db.collection("Tickets")
                                    .document(currentTicket.getTicketID())
                                    .set(currentTicket)
                                    .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Updated status of ticket"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error updating ticket", e));
                            // Redirect to all visits
                            finish();
                            startActivity(new Intent(TicketDetailsActivity.this, TicketsListActivity.class));
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            });

            // Retrieve username from sharedPreferences
            SharedPreferences sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
            String username = sharedpreferences.getString(Username, "");
            if (username.equals(currentTicket.getUsername())) {
                updateTV.setVisibility(View.VISIBLE);
            }

            updateTV.setOnClickListener(c -> {
                Intent intent = new Intent(TicketDetailsActivity.this, TicketUpdateActivity.class);
                intent.putExtra("TICKET", currentTicket);
                startActivity(intent);
            });
        }
        else {
            closedTV.setText("This ticket is closed.");
        }

    }

    private void initViews() {
        titleTV = findViewById(R.id.activity_ticket_details_title);
        textTV = findViewById(R.id.activity_ticket_details_text);
        usernameTV = findViewById(R.id.activity_ticket_details_username);
        closedTV = findViewById(R.id.activity_ticket_details_tv_set_closed);
        updateTV = findViewById(R.id.activity_ticket_details_tv_update);

        currentTicket = (Ticket) getIntent().getSerializableExtra("TICKET");
        String details = currentTicket.getUsername() + "\n" + currentTicket.getDate().substring(0, currentTicket.getDate().indexOf("T"))
                + ", " + currentTicket.getDate().substring(currentTicket.getDate().indexOf("T") + 1, currentTicket.getDate().indexOf("T") + 6);
        titleTV.setText(currentTicket.getTitle());
        textTV.setText(currentTicket.getText());
        usernameTV.setText(details);
    }
}