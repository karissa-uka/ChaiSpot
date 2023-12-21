package uk.ac.aston.cs3mdd.chaispot.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.chaispot.R;
import uk.ac.aston.cs3mdd.chaispot.databinding.FragmentHomeBinding;
import uk.ac.aston.cs3mdd.chaispot.model.MyPlace;
import uk.ac.aston.cs3mdd.chaispot.model.PlaceDatabase;
import uk.ac.aston.cs3mdd.chaispot.model.PlaceListAdapter;
import uk.ac.aston.cs3mdd.chaispot.model.Places;
import uk.ac.aston.cs3mdd.chaispot.model.PlacesViewModel;
import uk.ac.aston.cs3mdd.chaispot.model.SingletonData;
import uk.ac.aston.cs3mdd.chaispot.service.GetNearbyPlaces;
import uk.ac.aston.cs3mdd.chaispot.service.PlacesRepository;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;

    private PlacesViewModel placesViewModel;
    private SharedViewModel sharedViewModel;
    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private PlaceListAdapter adapterView;

    private PlaceDatabase database;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Use PlacesViewModel instead of HomeViewModel
        placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = binding.getRoot();
        SingletonData.getInstance().setPlacesKey(getResources().getString(R.string.place_key));
        database = PlaceDatabase.getInstance(getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            initializeRecycleView();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GetNearbyPlaces service = retrofit.create(GetNearbyPlaces.class);

            placesViewModel.requestNearbyPlaces(new PlacesRepository(service), "52.4867,-1.8882", 5000, "cafe");

        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void initializeRecycleView() {
        final Observer<List<MyPlace>> userListObserver = new Observer<List<MyPlace>>() {
            @Override
            public void onChanged(@Nullable final List<MyPlace> placeList) {
                adapterView.updateData(placeList);
            }
        };

        recyclerView = getView().findViewById(R.id.places_list_view);

        adapterView = new PlaceListAdapter(getContext(), new ArrayList<>(),
                new PlaceListAdapter.OnPlaceItemClickListener() {
                    @Override
                    public void onPlaceItemClick(MyPlace place) {
                        Log.d("HomeFragment", "Item clicked: " + place.getName());
                        sharedViewModel.setSelectedPlace(place);
                        // Navigate to MapFragment when a place is clicked
                        //NavHostFragment.findNavController(HomeFragment.this)
                        //        .navigate(R.id.action_navigation_home_to_navigation_map);
                    }
                },
                new PlaceListAdapter.OnAddItemClickListener() {
                    @Override
                    public void onAddItemClick(MyPlace place) {
                        Log.d("HomeFragment", "Add button clicked: " + place.getName());
                        try {
                            addPlacetoFavourites(place);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        recyclerView.setAdapter(adapterView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        placesViewModel.getAllPlaces().observe(getViewLifecycleOwner(), userListObserver);
    }

    private void addPlacetoFavourites(MyPlace place) throws ExecutionException, InterruptedException {
        Places favoritePlace = new Places();
        favoritePlace.setPlaceName(place.getName());
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> database.placeDao().createPlace(favoritePlace));
        while (!completableFuture.isDone()) {
            // Task in progress
        }
        long id = completableFuture.get();
        if (id > 0) {
            Toast.makeText(getContext(), "This place has been added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add place to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
