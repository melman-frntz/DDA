package com.example.monitor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "earthquakes")
public class Earthquake {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name="city")
    private String place;
    private double magnitude;
    private long time;
    private double latitude;
    private double longitude;

    public Earthquake(String id, String place, double magnitude, long time, double latitude, double longitude) {
        this.id = id;
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Earthquake)) return false;
        Earthquake that = (Earthquake) o;
        return Double.compare(that.getMagnitude(), getMagnitude()) == 0 && getTime() == that.getTime() && Double.compare(that.getLatitude(), getLatitude()) == 0 && Double.compare(that.getLongitude(), getLongitude()) == 0 && getId().equals(that.getId()) && getPlace().equals(that.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPlace(), getMagnitude(), getTime(), getLatitude(), getLongitude());
    }
}
