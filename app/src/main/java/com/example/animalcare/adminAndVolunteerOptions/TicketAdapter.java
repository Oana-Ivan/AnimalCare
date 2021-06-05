package com.example.animalcare.adminAndVolunteerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalcare.CRUD.VolunteersAdapter;
import com.example.animalcare.R;
import com.example.animalcare.models.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private ArrayList<Ticket> tickets;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public TicketAdapter(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        TicketViewHolder ticketViewHolder = new TicketViewHolder(view, onItemClickListener);
        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        String text = ticket.getText().substring(0, (ticket.getText().length() / 2)) + "...";
        String status = "Status: " + ticket.getStatus();

        holder.titleTV.setText(ticket.getTitle());
        holder.usernameTV.setText(ticket.getUsername());
        holder.textTV.setText(text);
        holder.statusTV.setText(status);
        if (ticket.getStatus().equals(Ticket.CLOSED)) {
            holder.changeStatusTV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, usernameTV, textTV, statusTV, changeStatusTV;

        public TicketViewHolder(@NonNull View itemView, TicketAdapter.OnItemClickListener listener) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.ticket_item_title);
            usernameTV = itemView.findViewById(R.id.ticket_item_volunteer_name);
            textTV = itemView.findViewById(R.id.ticket_item_text_row_1);
            statusTV = itemView.findViewById(R.id.ticket_item_status);
            changeStatusTV = itemView.findViewById(R.id.ticket_item_change_status);

            changeStatusTV.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
