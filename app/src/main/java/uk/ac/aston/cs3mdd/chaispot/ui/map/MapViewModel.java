package uk.ac.aston.cs3mdd.chaispot.ui.map;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {
    private MutableLiveData<Location> currentLocation;
    private MutableLiveData<Boolean> locationUpdates;

    private MapViewModel() {
        super();
        locationUpdates = new MutableLiveData<Boolean>(false);

        currentLocation = new MutableLiveData<>(null);
    }

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location mCurrentLocation) {
        this.currentLocation.setValue(mCurrentLocation);
    }

    public LiveData<Boolean> getLocationUpdates() {
        return locationUpdates;
    }

    public void setLocationUpdates(Boolean locationUpdates) {
        this.locationUpdates.setValue(locationUpdates);
    }

}