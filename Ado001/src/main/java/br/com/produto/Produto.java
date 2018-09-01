/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.produto;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author jacks
 */
public class Produto {

    private String nome, descricao;
    private int quant;
    private float pCompra, pVenda;
    private Timestamp time;
    private int id;

    // Informações da categoria do produto.
    private String categoria;

    // Construtor do objeto.
    public Produto() {
        Calendar cal = Calendar.getInstance();

        setTime(new Timestamp(cal.get(Calendar.HOUR_OF_DAY)));

    }

    // Inicio get e set
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public float getpCompra() {
        return pCompra;
    }

    public void setpCompra(float pCompra) {
        this.pCompra = pCompra;
    }

    public float getpVenda() {
        return pVenda;
    }

    public void setpVenda(float pVenda) {
        this.pVenda = pVenda;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Fim get e set
    public String getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
