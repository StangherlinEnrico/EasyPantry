package com.stanga.easypantry.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Pantry;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<Pantry> pantries = new ArrayList<>();
    private OnPantryClickListener listener;

    public PantryAdapter(OnPantryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        Pantry currentPantry = pantries.get(position);
        holder.bind(currentPantry, listener);
    }

    @Override
    public int getItemCount() {
        return pantries.size();
    }

    public void setPantries(List<Pantry> pantries) {
        this.pantries = pantries;
        notifyDataSetChanged();
    }

    class PantryViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public PantryViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
        }

        public void bind(Pantry pantry, OnPantryClickListener listener) {
            textViewName.setText(pantry.name);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onPantryClick(pantry);
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onPantryLongClick(pantry);
                return true;
            });
        }
    }
}