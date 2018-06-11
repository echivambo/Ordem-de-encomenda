package com.sourcey.materiallogindemo.model;

public class Cliente {
    private int id;
    private String nome;
    private String nuit;
    private int provincia_id;
    private int distrito_id;
    private String tipo_cliente; //(Grossista/Retalhista)
    private String email;
    private String contacto;
    private String endereco;
    private int status;
    private int user_id;

    public Cliente() {
    }

    public Cliente(int id, String nome, String nuit, int provincia_id, int distrito_id, String tipo_cliente, String email, String contacto, String endereco, int status, int user_id) {
        this.id = id;
        this.nome = nome;
        this.nuit = nuit;
        this.provincia_id = provincia_id;
        this.distrito_id = distrito_id;
        this.tipo_cliente = tipo_cliente;
        this.email = email;
        this.contacto = contacto;
        this.endereco = endereco;
        this.status = status;
        this.user_id = user_id;
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

    public int getProvincia_id() {
        return provincia_id;
    }

    public void setProvincia_id(int provincia_id) {
        this.provincia_id = provincia_id;
    }

    public int getDistrito_id() {
        return distrito_id;
    }

    public void setDistrito_id(int distrito_id) {
        this.distrito_id = distrito_id;
    }

    public String getTipo_cliente() {
        return tipo_cliente;
    }

    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
