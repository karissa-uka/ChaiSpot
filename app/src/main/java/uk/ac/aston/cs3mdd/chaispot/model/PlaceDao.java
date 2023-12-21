package uk.ac.aston.cs3mdd.chaispot.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM places")
    List<Places> getAllFavouritePlaces();

    @Insert
    long createPlace(Places place);

    @Update
    void updatePlace(Places place);

    @Delete
    void deletePlace(Places place);

}
