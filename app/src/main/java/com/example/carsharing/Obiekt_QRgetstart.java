package com.example.carsharing;

public class Obiekt_QRgetstart {
    private String BookingId;
    private String GrProjektu;
    private String NrProjektu;
    private String Error;
    private String Status;
    private String StartDate;
    private String EndDate;

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Obiekt_QRgetstart(String status) {
        Status = status;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getGrProjektu() {
        return GrProjektu;
    }

    public void setGrProjektu(String grProjektu) {
        GrProjektu = grProjektu;
    }

    public String getNrProjektu() {
        return NrProjektu;
    }

    public void setNrProjektu(String nrProjektu) {
        NrProjektu = nrProjektu;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public Obiekt_QRgetstart(String bookingId, String grProjektu, String nrProjektu, String error,String status,String startDate,String endDate) {
        BookingId = bookingId;
        GrProjektu = grProjektu;
        NrProjektu = nrProjektu;
        Error = error;
        Status=status;
        StartDate=startDate;
        EndDate=endDate;
    }
}
