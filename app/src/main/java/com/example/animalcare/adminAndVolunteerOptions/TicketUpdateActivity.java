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
import com.example.animalcare.models.Ticket;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.authentication.RegisterActivity.UserPREFERENCES;
import static com.example.animalcare.authentication.RegisterActivity.Username;

public class TicketUpdateActivity extends AppCompatActivity {
    private Ticket currentTicket;
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
        setContentView(R.layout.activity_ticket_update);

        currentTicket = (Ticket) getIntent().getSerializableExtra("TICKET");

        titleET = findViewById(R.id.activity_update_ticket_et_title);
        textET = findViewById(R.id.activity_update_ticket_et_text);
        addBtn = findViewById(R.id.activity_update_ticket_btn_add_ticket);

        titleET.setText(currentTicket.getTitle());
        textET.setText(currentTicket.getText());

        db = FirebaseFirestore.getInstance();
        ticketsCollection = db.collection("Tickets");

        addBtn.setOnClickListener(b -> {
            title = titleET.getText().toString();
            text = textET.getText().toString();

            if (title.equals("") || text.equals("")) {
                Toast.makeText(TicketUpdateActivity.this, "Fill both fields!", Toast.LENGTH_LONG).show();
            }
            else {
                // Update ticket in collection
                currentTicket.setTitle(title);
                currentTicket.setText(text);
                SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:sss'Z'");
                String date = ISO_8601_FORMAT.format(new Date());
                currentTicket.setDate(date);

                ticketsCollection.document(currentTicket.getTicketID())
                        .set(currentTicket)
                        .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Added new ticket"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                // Redirect to TicketsListActivity
                finish();
                startActivity(new Intent(TicketUpdateActivity.this, TicketsListActivity.class));
            }
        });

    }
}