package com.example.carsharing;

public class Obiekt_LokalizacjaSamochodu {

    private String ResourceName;
    private String Latitude;
    private String Longitude;
    private String Batery;
    private String CzasOdczytu;



    public String getCzasOdczytu() {
        return CzasOdczytu;
    }

    public void setCzasOdczytu(String czasOdczytu) {
        CzasOdczytu = czasOdczytu;
    }


    public Obiekt_LokalizacjaSamochodu(String czasOdczytu) {
        CzasOdczytu = czasOdczytu;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        ResourceName = resourceName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getBatery() {
        return Batery;
    }

    public void setBatery(String batery) {
        Batery = batery;
    }

    public Obiekt_LokalizacjaSamochodu(String resourceName, String latitude, String longitude, String batery,String czas) {
        ResourceName = resourceName;
        Latitude = latitude;
        Longitude = longitude;
        Batery = batery;
        CzasOdczytu=czas;
    }
}
