package com.example.carsharing;

public class Obiekt_LokalizacjaSamochodu {

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

    public String getCzasOdczytu() {
        return CzasOdczytu;
    }

    public void setCzasOdczytu(String czasOdczytu) {
        CzasOdczytu = czasOdczytu;
    }

    public String getTypPaliwa() {
        return TypPaliwa;
    }

    public void setTypPaliwa(String typPaliwa) {
        TypPaliwa = typPaliwa;
    }

    public String getWartoscPaliwa() {
        return WartoscPaliwa;
    }

    public void setWartoscPaliwa(String wartoscPaliwa) {
        WartoscPaliwa = wartoscPaliwa;
    }

    public Obiekt_LokalizacjaSamochodu(String resourceName, String latitude, String longitude, String czasOdczytu, String typPaliwa, String wartoscPaliwa, String uzytkownik) {
        ResourceName = resourceName;
        Latitude = latitude;
        Longitude = longitude;
        CzasOdczytu = czasOdczytu;
        TypPaliwa = typPaliwa;
        WartoscPaliwa = wartoscPaliwa;
        Uzytkownik = uzytkownik;
    }

    private String ResourceName;
    private String Latitude;
    private String Longitude;
    private String CzasOdczytu;
    private String TypPaliwa;
    private String WartoscPaliwa;
    private String Uzytkownik;

    public String getUzytkownik() {
        return Uzytkownik;
    }

    public void setUzytkownik(String uzytkownik) {
        Uzytkownik = uzytkownik;
    }
}
