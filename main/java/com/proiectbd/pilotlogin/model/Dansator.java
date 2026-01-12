package com.proiectbd.pilotlogin.model;

public class Dansator {
    private int id_dansator;
    private String nume;
    private String prenume;
    private String cnp;
    private int data_angajarii; // YYYYMMDD
    private String rang;

    public int getId_dansator() { return id_dansator; }
    public void setId_dansator(int id_dansator) { this.id_dansator = id_dansator; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }

    public int getData_angajarii() { return data_angajarii; }
    public void setData_angajarii(int data_angajarii) { this.data_angajarii = data_angajarii; }

    public String getRang() { return rang; }
    public void setRang(String rang) { this.rang = rang; }
}
