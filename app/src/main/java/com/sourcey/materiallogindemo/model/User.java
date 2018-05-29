package com.sourcey.materiallogindemo.model;

public class User {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String distrito;
    private String provincia;
    private String grupo;

    public User(String nome, String email, String senha, String distrito, String provincia, String grupo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.distrito = distrito;
        this.provincia = provincia;
        this.grupo = grupo;
    }

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
