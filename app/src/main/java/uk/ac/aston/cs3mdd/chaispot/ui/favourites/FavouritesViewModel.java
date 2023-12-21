package uk.ac.aston.cs3mdd.chaispot.ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cs3mdd.chaispot.model.Places;

public class FavouritesViewModel extends ViewModel {

    private MutableLiveData<List<Places>> allPlaces;

    public FavouritesViewModel() {
        super();
        allPlaces = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Places>> getAllFavouritePlaces(){
        return allPlaces;
    }

    public void refreshPlaces(List<Places> places){
        allPlaces.setValue(places);
    }
}