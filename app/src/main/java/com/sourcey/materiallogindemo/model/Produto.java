package com.sourcey.materiallogindemo.model;

public class Produto {
    private int Id;
    private String descricao;
    private String codigo;
    private String psiProdotoId;
    private String tropigaliaProdotoId;
    private int user_id;

    public Produto() {
    }

    public Produto(String descricao, String codigo, String psiProdotoId, String tropigaliaProdotoId, int user_id) {
        this.descricao = descricao;
        this.codigo = codigo;
        this.psiProdotoId = psiProdotoId;
        this.tropigaliaProdotoId = tropigaliaProdotoId;
        this.user_id = user_id;
    }

    public Produto(int id, String descricao, String codigo, String psiProdotoId, String tropigaliaProdotoId, int user_id) {
        Id = id;
        this.descricao = descricao;
        this.codigo = codigo;
        this.psiProdotoId = psiProdotoId;
        this.tropigaliaProdotoId = tropigaliaProdotoId;
        this.user_id = user_id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPsiProdotoId() {
        return psiProdotoId;
    }

    public void setPsiProdotoId(String psiProdotoId) {
        this.psiProdotoId = psiProdotoId;
    }

    public String getTropigaliaProdotoId() {
        return tropigaliaProdotoId;
    }

    public void setTropigaliaProdotoId(String tropigaliaProdotoId) {
        this.tropigaliaProdotoId = tropigaliaProdotoId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
