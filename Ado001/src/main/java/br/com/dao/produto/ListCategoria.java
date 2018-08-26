/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao.produto;

import br.com.connection.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author jacks
 */
public class ListCategoria {
    private Connection conn;
    private ResultSet rst;
    private PreparedStatement pst;
    private String sql;
    StringBuilder result;
    private String [] cbView;
    
    public ListCategoria(){
        setSql("");
        conn = null;
        rst = null;
        pst = null;
        result = new StringBuilder();
        
    }
    /**
     * Função tem como objetivo
     * Listar as opções de categoria 
     * no combo box da tela cadastrar produto.
     * @return Vetor de String ou null. 
     */
    public String [] listCat() {
        
        try {
            conn = Conexao.getConnection();

            setSql("SELECT c.NOME FROM CATEGORIA c ORDER BY id");

            pst = conn.prepareStatement(getSql());
            
            rst = pst.executeQuery();
            
            result.append("Selecione... ;");
            
            while(rst.next())
                result.append(rst.getString("NOME")).append(";");
            
            return cbView = result.toString().split(";");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // Inicio de get e set;
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }    
    // Fim de get e set.
}
