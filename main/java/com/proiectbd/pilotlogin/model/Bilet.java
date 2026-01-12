package com.proiectbd.pilotlogin.model;

public class Bilet {
    private int id_bilet;
    private int id_reprezentatie;
    private int nr_loc;
    private int pret;
    private String status;
    private int id_sala;

    private String tip_reprezentatie;
    private int data_reprezentatie;
    private int ora_inceput;
    private String nume_sala;

    public int getId_bilet() { return id_bilet; }
    public void setId_bilet(int id_bilet) { this.id_bilet = id_bilet; }

    public int getId_reprezentatie() { return id_reprezentatie; }
    public void setId_reprezentatie(int id_reprezentatie) { this.id_reprezentatie = id_reprezentatie; }

    public int getNr_loc() { return nr_loc; }
    public void setNr_loc(int nr_loc) { this.nr_loc = nr_loc; }

    public int getPret() { return pret; }
    public void setPret(int pret) { this.pret = pret; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getId_sala() { return id_sala; }
    public void setId_sala(int id_sala) { this.id_sala = id_sala; }

    public String getTip_reprezentatie() { return tip_reprezentatie; }
    public void setTip_reprezentatie(String tip_reprezentatie) { this.tip_reprezentatie = tip_reprezentatie; }

    public int getData_reprezentatie() { return data_reprezentatie; }
    public void setData_reprezentatie(int data_reprezentatie) { this.data_reprezentatie = data_reprezentatie; }

    public int getOra_inceput() { return ora_inceput; }
    public void setOra_inceput(int ora_inceput) { this.ora_inceput = ora_inceput; }

    public String getNume_sala() { return nume_sala; }
    public void setNume_sala(String nume_sala) { this.nume_sala = nume_sala; }

    private String tip_spectacol;

    public String getTip_spectacol() {
        return tip_spectacol;
    }

    public void setTip_spectacol(String tip_spectacol) {
        this.tip_spectacol = tip_spectacol;
    }

}
