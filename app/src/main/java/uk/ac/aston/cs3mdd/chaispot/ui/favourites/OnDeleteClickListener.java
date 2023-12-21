package uk.ac.aston.cs3mdd.chaispot.ui.favourites;

import java.util.concurrent.ExecutionException;

import uk.ac.aston.cs3mdd.chaispot.model.Places;

public interface OnDeleteClickListener {
    void onDeleteClick(Places placeToDelete) throws ExecutionException, InterruptedException;
}
