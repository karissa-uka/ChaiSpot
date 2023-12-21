package uk.ac.aston.cs3mdd.chaispot.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.ac.aston.cs3mdd.chaispot.model.PlacesList;

public interface GetNearbyPlaces {


    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?parameters
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.4867,-1.8882&radius=1500&type=restaurant&fields=name,icon,place_id,rating,geometry&key=YOUR_API_KEY
    @GET("json?")
    Call<PlacesList> getNearbyPlaces(@Query("location") String latLon,
                                     @Query("radius") int radius,
                                     @Query("type") String type,
                                     @Query("fields") String fields,
                                     @Query("key") String apikey);
}
