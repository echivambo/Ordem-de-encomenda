package com.sourcey.materiallogindemo.model;

import java.util.Date;

public class Encomenda {
    private int id;
    private Date dataEncomenda;
    private Date dataEntrega;
    private int clienteId;
    private String nomeCliente;
    private String estabelecimento;
    private double quantidade;
    private boolean entregue;
    private boolean status;
    private String comentario;
    private String unidadeMedida;
    private int produtoId;
    private String descricaoProduto;
    private int user_id;

    public Encomenda() {
    }

    public Encomenda(Date dataEncomenda, Date dataEntrega, int clienteId, String nomeCliente, String estabelecimento, double quantidade, boolean entregue, boolean status, String comentario, String unidadeMedida, int produtoId, String descricaoProduto, int user_id) {
        this.dataEncomenda = dataEncomenda;
        this.dataEntrega = dataEntrega;
        this.clienteId = clienteId;
        this.estabelecimento = estabelecimento;
        this.quantidade = quantidade;
        this.entregue = entregue;
        this.status = status;
        this.comentario = comentario;
        this.unidadeMedida = unidadeMedida;
        this.produtoId = produtoId;
        this.descricaoProduto = descricaoProduto;
        this.user_id = user_id;
        this.nomeCliente = nomeCliente;
    }

    public Encomenda(int id, Date dataEncomenda, Date dataEntrega, int clienteId, String nomeCliente, String estabelecimento, double quantidade, boolean entregue, boolean status, String comentario, String unidadeMedida, int produtoId, String descricaoProduto, int user_id) {
        this.id = id;
        this.dataEncomenda = dataEncomenda;
        this.dataEntrega = dataEntrega;
        this.clienteId = clienteId;
        this.estabelecimento = estabelecimento;
        this.quantidade = quantidade;
        this.entregue = entregue;
        this.status = status;
        this.comentario = comentario;
        this.unidadeMedida = unidadeMedida;
        this.produtoId = produtoId;
        this.descricaoProduto = descricaoProduto;
        this.user_id = user_id;
        this.nomeCliente = nomeCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(Date dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
