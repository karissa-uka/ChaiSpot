package uk.ac.aston.cs3mdd.chaispot.ui.home;

// a shared viewmodel for displaying the places clicked on the home fragment in the map fragment
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import uk.ac.aston.cs3mdd.chaispot.model.MyPlace;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<MyPlace> selectedPlace = new MutableLiveData<>();

    public void setSelectedPlace(MyPlace place) {
        selectedPlace.setValue(place);
    }

    public LiveData<MyPlace> getSelectedPlace() {
        return selectedPlace;
    }

    public void clearSelectedPlace() {
        selectedPlace.setValue(null); // Clear the selected place by setting the value to null
    }
}
