package com.example.carsharing;

public class Obiekt_Samochód {

    private String ResourceId;
    private String ResourceName;
    private String Parking;

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        ResourceName = resourceName;
    }

    public String getParking() {
        return Parking;
    }

    public void setParking(String parking) {
        Parking = parking;
    }

    public Obiekt_Samochód(String resourceId, String resourceName, String parking) {
        ResourceId = resourceId;
        ResourceName = resourceName;
        Parking = parking;
    }
}
