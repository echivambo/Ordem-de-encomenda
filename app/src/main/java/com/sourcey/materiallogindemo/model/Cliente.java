package com.sourcey.materiallogindemo.model;

public class Cliente {
    private int id;
    private String nome;
    private String nuit;
    private int provinciaID;
    private int distritoID;
    private String tipoCliente; //(Grossista/Retalhista)
    private String email;
    private String endereco;
    private int userID;

    public Cliente() {
    }

    public Cliente(String nome, String nuit, int provinciaID, int distritoID, String tipoCliente, String email, String endereco, int userID) {
        this.nome = nome;
        this.nuit = nuit;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.tipoCliente = tipoCliente;
        this.email = email;
        this.endereco = endereco;
        this.userID = userID;
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

    public String getNuit() {
        return nuit;
    }

    public void setNuit(String nuit) {
        this.nuit = nuit;
    }

    public int getProvinciaID() {
        return provinciaID;
    }

    public void setProvinciaID(int provinciaID) {
        this.provinciaID = provinciaID;
    }

    public int getDistritoID() {
        return distritoID;
    }

    public void setDistritoID(int distritoID) {
        this.distritoID = distritoID;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return  nome;
    }
}
