package uk.ac.aston.cs3mdd.chaispot.model;

import androidx.annotation.NonNull;

public class MyPlace {
    private String name;
    private String icon;
    private String place_id;
    private float rating;
    private Geometry geometry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Place called " + name + " at " + geometry.toString();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public double getLatitude() {
        // Assuming MyLocation has a getLat() method
        return geometry != null && geometry.getLocation() != null ? geometry.getLocation().getLat() : 0.0;
    }

    public double getLongitude() {
        // Assuming MyLocation has a getLng() method
        return geometry != null && geometry.getLocation() != null ? geometry.getLocation().getLng() : 0.0;
    }
}
