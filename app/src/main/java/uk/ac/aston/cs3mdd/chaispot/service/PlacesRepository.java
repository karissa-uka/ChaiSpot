package uk.ac.aston.cs3mdd.chaispot.service;

import android.util.Log;

import retrofit2.Call;
import uk.ac.aston.cs3mdd.chaispot.MainActivity;
import uk.ac.aston.cs3mdd.chaispot.model.PlacesList;
import uk.ac.aston.cs3mdd.chaispot.model.SingletonData;


public class PlacesRepository {
    private GetNearbyPlaces nearbyPlacesService;

    public PlacesRepository(GetNearbyPlaces placesService) {
        this.nearbyPlacesService = placesService;
    }

    public Call<PlacesList> getNearbyPlaces(String latLon, int radius, String type) {
        Log.i(MainActivity.TAG, "Places key is " + SingletonData.getInstance().getPlacesKey());
        // Fields should be specified based on free data available
        // See https://developers.google.com/maps/documentation/places/web-service/usage-and-billing
        // Make sure the MyPlace class also contains these fields
        String fields = "name,icon,place_id,rating,geometry";
        return nearbyPlacesService.getNearbyPlaces(latLon, radius, type, fields, SingletonData.getInstance().getPlacesKey());
    }
}
