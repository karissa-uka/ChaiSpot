package uk.ac.aston.cs3mdd.chaispot.ui.map;

import static com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.chaispot.R;
import uk.ac.aston.cs3mdd.chaispot.databinding.FragmentMapBinding;
import uk.ac.aston.cs3mdd.chaispot.model.MyPlace;
import uk.ac.aston.cs3mdd.chaispot.model.PlacesViewModel;
import uk.ac.aston.cs3mdd.chaispot.model.SingletonData;
import uk.ac.aston.cs3mdd.chaispot.service.GetNearbyPlaces;
import uk.ac.aston.cs3mdd.chaispot.service.PlacesRepository;
import uk.ac.aston.cs3mdd.chaispot.ui.home.SharedViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "UKC";
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private MapViewModel locationViewModel;
    private PlacesViewModel placesViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentMapBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.i(TAG, "mapview oncreateview");

        locationViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        SingletonData.getInstance().setPlacesKey(getResources().getString(R.string.place_key));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "mapview onviewcreated");
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.i(TAG, "Map Ready ");

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (locationViewModel != null) {
                Location location = locationViewModel.getCurrentLocation().getValue();

//                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//                fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null)
//                        .addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {

                    Log.i(TAG, "Got Location " + location);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    GetNearbyPlaces service = retrofit.create(GetNearbyPlaces.class);

                    String locString  = "" + location.getLatitude() + "," + location.getLongitude();
                    Log.i("AJB", "Location: " + locString);

                    placesViewModel.requestNearbyPlaces(new PlacesRepository(service), locString, 5000, "cafe");

                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title("Your Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    for (MyPlace myPlace : placesViewModel.getAllPlaces().getValue()) {
                        LatLng selectedPlaceLatLng = new LatLng(myPlace.getGeometry().getLocation().getLat(), myPlace.getGeometry().getLocation().getLng());
                        googleMap.addMarker(new MarkerOptions()
                                .position(selectedPlaceLatLng)
                                .title(myPlace.getName()));
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceLatLng, 15));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }


                    /*MyPlace selectedPlace = sharedViewModel.getSelectedPlace().getValue();
                    if (selectedPlace != null) {
                        LatLng selectedPlaceLatLng = new LatLng(selectedPlace.getGeometry().getLocation().getLat(), selectedPlace.getGeometry().getLocation().getLng());
                        googleMap.addMarker(new MarkerOptions()
                                .position(selectedPlaceLatLng)
                                .title(selectedPlace.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceLatLng, 15));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        sharedViewModel.clearSelectedPlace();
                    }*/
                } else {
                    Log.i("UKC", "onMapReady: Last known location is null");
                }
                //});
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}