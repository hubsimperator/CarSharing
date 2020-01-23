package com.example.carsharing.Other;

import java.io.Serializable;

public class Obiekt_Rezerwacja implements Serializable {
	private Long nr;
	private String BookingId;
	private String StartDate;
	private String EndDate;
	private String AllDay;
	private String Subject;
	private String Eit_Resource;
	private String Eit_ResourceName;
	private String Eit_Uzytkownik;
	private String ReminderId;
	private String Location;
	private String Description;
	private String Status;
	private String GrupaProjektu;
	private String NrProjektu;

	public void setBookingId(String BookingId) {
		this.BookingId = BookingId;
	}
	public String getBookingId() {
		return BookingId;
	}

	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}
	public String getStartDate() {
		return StartDate;
	}

	public void setEndDate(String EndDate) {
		this.EndDate = EndDate;
	}
	public String getEndDate() {
		return EndDate;
	}

	public void setAllDay(String AllDay) {
		this.AllDay = AllDay;
	}
	public String getAllDay() {
		return AllDay;
	}

	public void setSubject(String Subject) {
		this.Subject = Subject;
	}
	public String getSubject() {
		return Subject;
	}

	public void setEit_Resource(String Eit_Resource) {
		this.Eit_Resource = Eit_Resource;
	}
	public String getEit_Resource() {
		return Eit_Resource;
	}

	public void setEit_ResourceName(String Eit_ResourceName) {
		this.Eit_ResourceName = Eit_ResourceName;
	}
	public String getEit_ResourceName() {
		return Eit_ResourceName;
	}

	public void setEit_Uzytkownik(String Eit_Uzytkownik) {
		this.Eit_Uzytkownik = Eit_Uzytkownik;
	}
	public String getEit_Uzytkownik() {
		return Eit_Uzytkownik;
	}

	public void setReminderId(String ReminderId) {
		this.ReminderId = ReminderId;
	}
	public String getReminderId() {
		return ReminderId;
	}

	public void setLocation(String Location) {
		this.Location = Location;
	}
	public String getLocation() {
		return Location;
	}

	public void setGrupaProjektu(String GrupaProjektu) {
		this.GrupaProjektu = GrupaProjektu;
	}
	public String getGrupaProjektu() {
		return GrupaProjektu;
	}

	public void setNrProjektu(String NrProjektu) {
		this.NrProjektu = NrProjektu;
	}
	public String getNrProjektu(){
		return NrProjektu;
	}
	public void setDescription(String Description) {
		this.Description = Description;
	}
	public String getDescription() {
		return Description;
	}

	public String getStatus() {
		return Status;
	}
	public void setStatus(String Status) {
		this.Status = Status;
	}
	public Long getNr() {
		return nr;
	}
	public void setNr(Long nr) {
		this.nr = nr;
	}
}
