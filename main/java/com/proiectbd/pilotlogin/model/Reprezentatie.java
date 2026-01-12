package com.proiectbd.pilotlogin.model;

public class Reprezentatie {
    private int id_reprezentatie;
    private String tip_reprezentatie;     // "Spectacol" sau "Repetitie"
    private int data_reprezentatie;       // format: YYYYMMDD (ex: 20250312)
    private int ora_inceput;              // format: HHMM (ex: 1800)
    private int ora_sfarsit;              // format: HHMM (ex: 2000)
    private int id_sala;

    private String nume_sala;

    public int getId_reprezentatie() { return id_reprezentatie; }
    public void setId_reprezentatie(int id_reprezentatie) { this.id_reprezentatie = id_reprezentatie; }

    public String getTip_reprezentatie() { return tip_reprezentatie; }
    public void setTip_reprezentatie(String tip_reprezentatie) { this.tip_reprezentatie = tip_reprezentatie; }

    private String tip_spectacol;

    public String getTip_spectacol() { return tip_spectacol; }
    public void setTip_spectacol(String tip_spectacol) { this.tip_spectacol = tip_spectacol; }

    public int getData_reprezentatie() { return data_reprezentatie; }
    public void setData_reprezentatie(int data_reprezentatie) { this.data_reprezentatie = data_reprezentatie; }

    public int getOra_inceput() { return ora_inceput; }
    public void setOra_inceput(int ora_inceput) { this.ora_inceput = ora_inceput; }

    public int getOra_sfarsit() { return ora_sfarsit; }
    public void setOra_sfarsit(int ora_sfarsit) { this.ora_sfarsit = ora_sfarsit; }

    public int getId_sala() { return id_sala; }
    public void setId_sala(int id_sala) { this.id_sala = id_sala; }

    public String getNume_sala() { return nume_sala; }
    public void setNume_sala(String nume_sala) { this.nume_sala = nume_sala; }
}
