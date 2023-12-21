package uk.ac.aston.cs3mdd.chaispot.ui.favourites;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.cs3mdd.chaispot.R;
import uk.ac.aston.cs3mdd.chaispot.databinding.FragmentFavouritesBinding;
import uk.ac.aston.cs3mdd.chaispot.model.FavoritesAdapter;
import uk.ac.aston.cs3mdd.chaispot.model.PlaceDatabase;
import uk.ac.aston.cs3mdd.chaispot.model.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FavouritesFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener {

    private FragmentFavouritesBinding binding;
    private PlaceDatabase database;
    private List<Places> favoritePlacesList;
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView favoritesRecyclerView;
    private FavouritesViewModel favouritesViewModel;
    private EditText searchEditText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        favouritesViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);

        // initialising the database
        database = PlaceDatabase.getInstance(getContext());

        // initialising recyclerview and its adapter

        CompletableFuture<List<Places>> completableFuture = CompletableFuture.supplyAsync(()->database.placeDao().getAllFavouritePlaces());
        while (!completableFuture.isDone()){
            // Task in progress
        }
        try {
            favouritesViewModel.refreshPlaces(completableFuture.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            favoritePlacesList = completableFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Add search functionality
        searchEditText = binding.getRoot().findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFavorites(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });

        return binding.getRoot();
    }
    private void filterFavorites(String query) {
        List<Places> filteredList = new ArrayList<>();
        for (Places place : favoritePlacesList) {
            // Filter based on place names
            if (place.getPlaceName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(place);
            }
        }
        favouritesViewModel.refreshPlaces(filteredList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //refreshFavoritesList();
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoritesAdapter = new FavoritesAdapter(favouritesViewModel.getAllFavouritePlaces().getValue(), this,this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final Observer<List<Places>> placeListObserver = new Observer<List<Places>>() {
            @Override
            public void onChanged(List<Places> places) {
                if (places.isEmpty()) {
                    // If the favorites list is empty, show the placeholder text
                    binding.placeholderTextView.setVisibility(View.VISIBLE);
                    binding.favoritesRecyclerView.setVisibility(View.GONE);
                } else {
                    // If there are favorites, show the RecyclerView and hide the placeholder text
                    binding.placeholderTextView.setVisibility(View.GONE);
                    binding.favoritesRecyclerView.setVisibility(View.VISIBLE);

                    // Update the RecyclerView data
                    favoritesAdapter.updateData(places);
                }
            }
        };
        favouritesViewModel.getAllFavouritePlaces().observe(getViewLifecycleOwner(),placeListObserver);
        super.onViewCreated(view, savedInstanceState);
    }


    //this method refreshes the favourites list and updates the recyclerview
    public void refreshFavoritesList() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Places>> listCompletableFuture = CompletableFuture.supplyAsync(()->database.placeDao().getAllFavouritePlaces());
        while (!listCompletableFuture.isDone()){
            //Task in progress
        }
        favouritesViewModel.refreshPlaces(listCompletableFuture.get());
    }

    @Override
    public void onEditClick(Places place, View view) {
        FavouritesFragmentDirections.ActionNavigationFavouritesToEditFragment action =
                FavouritesFragmentDirections.actionNavigationFavouritesToEditFragment(place);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDeleteClick(Places placeToDelete) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> task =  CompletableFuture.runAsync(()->database.placeDao().deletePlace(placeToDelete));
        while (!task.isDone()){
            //Task in progress
        }
        refreshFavoritesList();
    }


}
