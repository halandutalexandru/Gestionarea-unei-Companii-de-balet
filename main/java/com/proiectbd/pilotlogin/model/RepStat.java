package com.proiectbd.pilotlogin.model;

public class RepStat {
    private int id_reprezentatie;
    private String tip_reprezentatie;
    private int data_reprezentatie;
    private int ora_inceput;
    private String nume_sala;
    private int capacitate;

    private int vandute;
    private int rezervate;
    private int libere;
    private int incasari;

    public int getId_reprezentatie() { return id_reprezentatie; }
    public void setId_reprezentatie(int id_reprezentatie) { this.id_reprezentatie = id_reprezentatie; }

    public String getTip_reprezentatie() { return tip_reprezentatie; }
    public void setTip_reprezentatie(String tip_reprezentatie) { this.tip_reprezentatie = tip_reprezentatie; }

    public int getData_reprezentatie() { return data_reprezentatie; }
    public void setData_reprezentatie(int data_reprezentatie) { this.data_reprezentatie = data_reprezentatie; }

    public int getOra_inceput() { return ora_inceput; }
    public void setOra_inceput(int ora_inceput) { this.ora_inceput = ora_inceput; }

    public String getNume_sala() { return nume_sala; }
    public void setNume_sala(String nume_sala) { this.nume_sala = nume_sala; }

    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) { this.capacitate = capacitate; }

    public int getVandute() { return vandute; }
    public void setVandute(int vandute) { this.vandute = vandute; }

    public int getRezervate() { return rezervate; }
    public void setRezervate(int rezervate) { this.rezervate = rezervate; }

    public int getLibere() { return libere; }
    public void setLibere(int libere) { this.libere = libere; }

    public int getIncasari() { return incasari; }
    public void setIncasari(int incasari) { this.incasari = incasari; }

    public double getGradOcupareVandute() {
        if (capacitate <= 0) return 0;
        return (vandute * 100.0) / capacitate;
    }
}
