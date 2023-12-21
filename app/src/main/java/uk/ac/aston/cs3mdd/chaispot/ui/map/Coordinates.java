package uk.ac.aston.cs3mdd.chaispot.ui.map;

import java.io.Serializable;

public class Coordinates  implements Serializable {
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}