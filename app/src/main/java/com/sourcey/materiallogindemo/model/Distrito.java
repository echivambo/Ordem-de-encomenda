package com.sourcey.materiallogindemo.model;

public class Distrito {
    private int id;
    private String nome;
    private int privinciaID;
    private String province;

    public Distrito(String nome, int privinciaID, String province) {
        this.nome = nome;
        this.privinciaID = privinciaID;
        this.province = province;
    }

    public Distrito() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPrivinciaID() {
        return privinciaID;
    }

    public void setPrivinciaID(int privinciaID) {
        this.privinciaID = privinciaID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
