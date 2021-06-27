package com.example.animalcare.adminAndVolunteerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.animalcare.CRUD.VolunteersAdapter;
import com.example.animalcare.CRUD.VolunteersListActivity;
import com.example.animalcare.R;
import com.example.animalcare.models.Ticket;
import com.example.animalcare.models.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.models.Ticket.CLOSED;
import static com.example.animalcare.models.Ticket.OPEN;

public class TicketsListActivity extends AppCompatActivity {
    private RecyclerView ticketsRV;
    private TicketAdapter ticketAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference ticketsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets_list);

        ticketsRV = findViewById(R.id.activity_tickets_list_rv);
        ticketsRV.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        // Retrieve tickets data from FireStore
        db = FirebaseFirestore.getInstance();
        ticketsCollection = db.collection("Tickets");
        ticketsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Ticket> tickets = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ticket currentTicket = document.toObject(Ticket.class);
                        if (currentTicket.getStatus().equals(OPEN)){
                            tickets.add(currentTicket);
                        }
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ticket currentTicket = document.toObject(Ticket.class);
                        if (currentTicket.getStatus().equals(CLOSED)){
                            tickets.add(currentTicket);
                        }
                    }
                    ticketAdapter = new TicketAdapter(tickets);

                    ticketsRV.setLayoutManager(layoutManager);
                    ticketsRV.setAdapter(ticketAdapter);

                    // click on close ticket
                    ticketAdapter.setOnItemClickListener(position -> {
                        Toast.makeText(TicketsListActivity.this, tickets.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                        tickets.get(position).setStatus(Ticket.CLOSED);
                        ticketsCollection.document(tickets.get(position).getTicketID())
                                .set(tickets.get(position))
                                .addOnSuccessListener((OnSuccessListener) o -> Log.d(TAG, "Added new user"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));


                    }, position -> {
                        Intent intent = new Intent(TicketsListActivity.this, TicketDetailsActivity.class);
                        intent.putExtra("TICKET", tickets.get(position));
                        startActivity(intent);
                    });
                    Log.d(TAG, tickets.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}