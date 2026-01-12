package com.proiectbd.pilotlogin.model;

public class DistributieItem {
    private int id_reprezentatie;
    private int id_dansator;
    private String nume;
    private String prenume;
    private String rol_in_reprezentatie;

    public int getId_reprezentatie() { return id_reprezentatie; }
    public void setId_reprezentatie(int id_reprezentatie) { this.id_reprezentatie = id_reprezentatie; }

    public int getId_dansator() { return id_dansator; }
    public void setId_dansator(int id_dansator) { this.id_dansator = id_dansator; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getRol_in_reprezentatie() { return rol_in_reprezentatie; }
    public void setRol_in_reprezentatie(String rol_in_reprezentatie) { this.rol_in_reprezentatie = rol_in_reprezentatie; }
}
