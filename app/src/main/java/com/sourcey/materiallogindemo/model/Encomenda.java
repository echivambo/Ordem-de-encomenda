package com.sourcey.materiallogindemo.model;


public class Encomenda {
    private int id;
    private String data_encomenda;
    private String data_entrega;
    private String numero_transacao;
    private String nuemro_item_transacao;
    private int cliente_id;
    private String nome_cliente;
    private String estabelecimento;
    private double quantidade;
    private boolean entregue;
    private boolean status;
    private String comentario;
    private String unidade_medida;
    private int produto_id;
    private String descricao_produto;
    private int user_id;

    public Encomenda() {
    }

    public Encomenda(String data_encomenda, String data_entrega, String numero_transacao, String nuemro_item_transacao, int cliente_id, String nome_cliente, String estabelecimento, double quantidade, boolean entregue, boolean status, String comentario, String unidade_medida, int produto_id, String descricao_produto, int user_id) {
        this.data_encomenda = data_encomenda;
        this.data_entrega = data_entrega;
        this.numero_transacao = numero_transacao;
        this.nuemro_item_transacao = nuemro_item_transacao;
        this.cliente_id = cliente_id;
        this.nome_cliente = nome_cliente;
        this.estabelecimento = estabelecimento;
        this.quantidade = quantidade;
        this.entregue = entregue;
        this.status = status;
        this.comentario = comentario;
        this.unidade_medida = unidade_medida;
        this.produto_id = produto_id;
        this.descricao_produto = descricao_produto;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData_encomenda() {
        return data_encomenda;
    }

    public void setData_encomenda(String data_encomenda) {
        this.data_encomenda = data_encomenda;
    }

    public String getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(String data_entrega) {
        this.data_entrega = data_entrega;
    }

    public String getNumero_transacao() {
        return numero_transacao;
    }

    public void setNumero_transacao(String numero_transacao) {
        this.numero_transacao = numero_transacao;
    }

    public String getNuemro_item_transacao() {
        return nuemro_item_transacao;
    }

    public void setNuemro_item_transacao(String nuemro_item_transacao) {
        this.nuemro_item_transacao = nuemro_item_transacao;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
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

    public String getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public String getDescricao_produto() {
        return descricao_produto;
    }

    public void setDescricao_produto(String descricao_produto) {
        this.descricao_produto = descricao_produto;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
