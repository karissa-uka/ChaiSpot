package uk.ac.aston.cs3mdd.chaispot.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Places.class,
        parentColumns = "id",
        childColumns = "id",
        onDelete = ForeignKey.CASCADE))
public class Reviews {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "review_text")
    private String reviewText;

    @ColumnInfo(name = "place_id")
    private int placeId;  // Foreign key referencing the Places table

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
