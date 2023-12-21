package uk.ac.aston.cs3mdd.chaispot.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE place_id = :placeId")
    List<Reviews> getReviewsForPlace(int placeId);

    @Insert
    long createReview(Reviews review);

    @Update
    void updateReview(Reviews review);

    @Delete
    void deleteReview(Reviews review);
}
