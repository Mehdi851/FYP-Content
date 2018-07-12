package com.jawad.driverapp;

/**
 * Created by Jawad on 3/29/2018.
 */

public class DriverDatail {
    String DriverID;
    String DriverName;
    double lat;
    double lng;

    public DriverDatail(String driverID, String driverName, double lat, double lng) {
        DriverID = driverID;
        DriverName = driverName;
        this.lat = lat;
        this.lng = lng;
    }

    public String getDriverID() {
        return DriverID;
    }

    public String getDriverName() {
        return DriverName;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public DriverDatail(){

    }

}
