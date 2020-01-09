package com.example.carsharing.Other;

public class Obiekt_Projekt {
	private Long nr;
	private String Grupa_Projektu;
	private String Nr_Projektu;
	private String Nazwa;
	private String Status;

	public void setGrupa_Projektu(String Grupa_Projektu) {
		this.Grupa_Projektu = Grupa_Projektu;
	}
	public String getGrupa_Projektu() {
		return Grupa_Projektu;
	}

	public void setNr_Projektu(String Nr_Projektu) {
		this.Nr_Projektu = Nr_Projektu;
	}
	public String getNr_Projektu() {
		return Nr_Projektu;
	}

	public void setNazwa(String Nazwa) {
		this.Nazwa = Nazwa;
	}
	public String getNazwa() {
		return Nazwa;
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
