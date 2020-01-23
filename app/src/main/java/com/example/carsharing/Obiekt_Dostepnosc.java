package com.example.carsharing;

import java.io.Serializable;

public class Obiekt_Dostepnosc {
    private String samochod;
    private String samochodID;

    private String start_date;
    private String end_date;

    public String getSamochodID() {
        return samochodID;
    }

    public void setSamochodID(String samochodID) {
        this.samochodID = samochodID;
    }

    public Obiekt_Dostepnosc(String samochodID) {
        this.samochodID = samochodID;
    }

    public Obiekt_Dostepnosc(String samochod,String samochodID, String start_date, String end_date) {
        this.samochod = samochod;
        this.samochodID = samochodID;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getSamochod() {
        return samochod;
    }

    public void setSamochod(String samochod) {
        this.samochod = samochod;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
