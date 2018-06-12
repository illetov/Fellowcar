package com.fellowcar.android.data.realm;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class TripRealmObject extends RealmObject {
    @RealmField(name = "idsd")
    private int id;
    private String from;
    private String to;
    private String driverName;
    private String statusTrip;
    private String driverPhotoURL;
    private String carPhotoURL;
    private int driverAge;
    private int distanceWay;
    private int distanceNearby;
    private int price;
    private int duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getStatusTrip() {
        return statusTrip;
    }

    public void setStatusTrip(String statusTrip) {
        this.statusTrip = statusTrip;
    }

    public String getDriverPhotoURL() {
        return driverPhotoURL;
    }

    public void setDriverPhotoURL(String driverPhotoURL) {
        this.driverPhotoURL = driverPhotoURL;
    }

    public String getCarPhotoURL() {
        return carPhotoURL;
    }

    public void setCarPhotoURL(String carPhotoURL) {
        this.carPhotoURL = carPhotoURL;
    }

    public int getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(int driverAge) {
        this.driverAge = driverAge;
    }

    public int getDistanceWay() {
        return distanceWay;
    }

    public void setDistanceWay(int distanceWay) {
        this.distanceWay = distanceWay;
    }

    public int getDistanceNearby() {
        return distanceNearby;
    }

    public void setDistanceNearby(int distanceNearby) {
        this.distanceNearby = distanceNearby;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
