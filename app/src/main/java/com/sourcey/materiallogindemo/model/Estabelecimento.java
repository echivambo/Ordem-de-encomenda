package com.sourcey.materiallogindemo.model;

public class Estabelecimento {
    private int id;
    private int clienteId;
    private String nomeEstabelecimento;
    private String lat;
    private String lng;
    private int userId;

    public Estabelecimento() {
    }

    public Estabelecimento(int clienteId, String nomeEstabelecimento, String lat, String lng, int userId) {
        this.clienteId = clienteId;
        this.nomeEstabelecimento = nomeEstabelecimento;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
