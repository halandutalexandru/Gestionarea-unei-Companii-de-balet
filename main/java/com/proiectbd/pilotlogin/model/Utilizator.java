package com.proiectbd.pilotlogin.model;

public class Utilizator {

    private int id_utilizator;
    private String username;
    private String parola;
    private String grad;
    private Integer id_dansator;

    public int getId_utilizator() {
        return id_utilizator;
    }

    public void setId_utilizator(int id_utilizator) {
        this.id_utilizator = id_utilizator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Integer getId_dansator() {
        return id_dansator;
    }

    public void setId_dansator(Integer id_dansator) {
        this.id_dansator = id_dansator;
    }
}
