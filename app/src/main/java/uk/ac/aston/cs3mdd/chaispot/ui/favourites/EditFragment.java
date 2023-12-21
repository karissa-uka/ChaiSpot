package uk.ac.aston.cs3mdd.chaispot.ui.favourites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import uk.ac.aston.cs3mdd.chaispot.MainActivity;
import uk.ac.aston.cs3mdd.chaispot.databinding.FragmentEditBinding;
import uk.ac.aston.cs3mdd.chaispot.model.PlaceDao;
import uk.ac.aston.cs3mdd.chaispot.model.PlaceDatabase;
import uk.ac.aston.cs3mdd.chaispot.model.Places;
import uk.ac.aston.cs3mdd.chaispot.model.ReviewDao;
import uk.ac.aston.cs3mdd.chaispot.model.Reviews;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;
    private Places places;
    private PlaceDatabase placeDatabase;
    public TextView reviewTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);
        places = EditFragmentArgs.fromBundle(getArguments()).getPlace();
        placeDatabase = PlaceDatabase.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewTextView = binding.review;

        // Check if places is not null before accessing its methods
        if (places != null) {
            // Update UI with place details
            binding.placeNameTextView.setText(places.getPlaceName());
            ReviewDao reviewDao = placeDatabase.reviewDao();
            CompletableFuture<List<Reviews>> future = CompletableFuture.supplyAsync(()-> reviewDao.getReviewsForPlace(places.getId()));
            while(!future.isDone()){
                //Task in progress
            }
            try {
                List<Reviews> reviews = future.get();
                if(reviews.size() > 0){
                  Reviews reviews1 = reviews.get(0);
                  reviewTextView.setText(reviews1.getReviewText());
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            // Display saved notes if available
            if (places.getNotes() != null && !places.getNotes().isEmpty()) {
                binding.notesEditText.setText(places.getNotes());
            }

            // Add click listener for save notes button
            binding.saveNotesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle save notes functionality
                    saveOrUpdatePlace();
                }
            });
        }
    }

    private void saveOrUpdatePlace() {
        String notes = binding.notesEditText.getText().toString();

        // Check if places is not null
        if (places != null) {
            // Get an instance of the PlaceDatabase
            PlaceDatabase database = PlaceDatabase.getInstance(requireContext());
            // Get an instance of the ReviewDao
            ReviewDao reviewDao = database.reviewDao();
            CompletableFuture<List<Reviews>> future = CompletableFuture.supplyAsync(()-> reviewDao.getReviewsForPlace(places.getId()));
            while (!future.isDone()){
                // Task in progress
            }
            List<Reviews> reviews = null;
            try {
                reviews = future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (reviews.size() > 0){
                Reviews reviews1 = reviews.get(0);
                reviews1.setReviewText(notes);
                CompletableFuture<Void> updatefuture = CompletableFuture.runAsync(()-> reviewDao.updateReview(reviews1));
                while (!updatefuture.isDone()){
                    // Task in progress
                }
                refreshReviewTextView(reviews1.getReviewText());
            } else {
                // Create or update the review associated with this place
                Reviews review = new Reviews();
                review.setReviewText(notes);
                review.setPlaceId(places.getId());

                CompletableFuture<Long> createfuture = CompletableFuture.supplyAsync(()-> reviewDao.createReview(review));
                while (!createfuture.isDone()){

                }
                refreshReviewTextView(review.getReviewText());
            }
        }
    }


    private void saveNotes() {
        // Displays a toast message so users can see when a note has been saved
        Toast.makeText(requireContext(), "Notes saved!", Toast.LENGTH_SHORT).show();

        // Display the saved notes in the EditText
        String notes = binding.notesEditText.getText().toString();
        binding.notesEditText.setText(notes);
    }

    private void refreshReviewTextView(String reviewText){
        reviewTextView.setText(reviewText);
    }
}