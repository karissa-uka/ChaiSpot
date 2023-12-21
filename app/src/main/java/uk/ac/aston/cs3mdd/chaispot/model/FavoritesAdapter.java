package uk.ac.aston.cs3mdd.chaispot.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.cs3mdd.chaispot.R;
import uk.ac.aston.cs3mdd.chaispot.ui.favourites.OnDeleteClickListener;
import uk.ac.aston.cs3mdd.chaispot.ui.favourites.OnEditClickListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private PlaceDatabase database;
    private List<Places> favoritePlacesList;
    public OnEditClickListener editClickListener;

    public OnDeleteClickListener deleteClickListener;

    public FavoritesAdapter(List<Places> favoritePlacesList, OnEditClickListener editClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.favoritePlacesList = favoritePlacesList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = onDeleteClickListener;
        this.database = database;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item_list, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Places favoritePlace = favoritePlacesList.get(position);
        holder.favoriteName.setText(favoritePlace.getPlaceName());

        // Click listener for the entire item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(favoritePlace, v);
                }
            }
        });

        // Click listener for the "Edit" button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(favoritePlace, v);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteClickListener.onDeleteClick(favoritePlace);
                } catch (ExecutionException e) {
                    // Handle the exception
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void updateData(List<Places> places){
        this.favoritePlacesList = places;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return favoritePlacesList.size();
    }



    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView favoriteName;
        ImageButton editButton;
        ImageButton deleteButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteName = itemView.findViewById(R.id.favorite_name);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
