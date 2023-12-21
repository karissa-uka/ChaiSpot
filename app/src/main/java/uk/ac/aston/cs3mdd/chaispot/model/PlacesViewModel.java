package uk.ac.aston.cs3mdd.chaispot.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.aston.cs3mdd.chaispot.MainActivity;
import uk.ac.aston.cs3mdd.chaispot.service.PlacesRepository;

public class PlacesViewModel extends ViewModel {

    private MutableLiveData<List<MyPlace>> allPlaces;
    private static MutableLiveData<ReviewDao> ReviewDao;

    public PlacesViewModel() {
        super();
        allPlaces = new MutableLiveData<List<MyPlace>>(new ArrayList<MyPlace>());
    }

    public LiveData<List<MyPlace>> getAllPlaces() {
        return allPlaces;
    }

    public void requestNearbyPlaces(PlacesRepository placesRepository, String loc, int radius, String type) {
        if (allPlaces.getValue().size() == 0) {
            Call<PlacesList> userCall = placesRepository.getNearbyPlaces(loc, radius, type);
            Log.i(MainActivity.TAG, "Call: " + userCall.request().toString());
            userCall.enqueue(new Callback<PlacesList>() {
                @Override
                public void onResponse(Call<PlacesList> call, Response<PlacesList> response) {
                    if (response.isSuccessful()) {
                        Log.i(MainActivity.TAG, "The response is " + response.body().toString());
                        addAll(response.body());
                    }
                }

                @Override
                public void onFailure(Call<PlacesList> call, Throwable t) {
                    // show error message to user
                    Log.i(MainActivity.TAG, "Error: " + t.toString());
                }
            });
        } else {
            Log.i("UKC", "Already got a list of users, not getting any more");
        }
    }

    private void addAll(PlacesList list) {
        allPlaces.getValue().addAll(list.getResults());
        allPlaces.setValue(allPlaces.getValue());
        Log.i("UKC", "Printing " + allPlaces.getValue().size() + " Places");
        for (MyPlace user : allPlaces.getValue()) {
            Log.i("UKC", user.toString());
        }
    }
}