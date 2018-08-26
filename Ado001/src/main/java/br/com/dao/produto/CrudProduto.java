/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao.produto;

import br.com.connection.Conexao;
import br.com.produto.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jacks
 */
public class CrudProduto {
    
    private Connection conn;
    private ResultSet rst;
    private PreparedStatement pst;
    private String sql;
    StringBuilder result;
    
    public CrudProduto(){
        setSql("");
        conn = null;
        rst = null;
        pst = null;
        result = new StringBuilder();

    }
    
    public void insertProd(Produto prt) throws SQLException{
        try {
            
            conn = Conexao.getConnection();

            setSql("INSERT INTO produto (NOME, DESCRICAO,"
                    + "PRECO_COMPRA, PRECO_VENDA, QUANTIDADE,"
                    + "DT_CADASTRO) VALUES "
                    + "(? ,? ,? , ?, ?, NOW()");
            
            pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, prt.getNome());
            pst.setString(2, prt.getDescricao());
            pst.setFloat(3, (Float)prt.getpCompra());
            pst.setFloat(4, (Float)prt.getpVenda());
            pst.setInt(5, prt.getQuant());
//            pst.setTimestamp(6, prt.getTime());
            
            pst.execute();
            
            rst = pst.getGeneratedKeys();
            
            if(rst.first()){
                prt.setId(rst.getInt("ID"));
            }
            insertCateg(prt, conn);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            
            //Se a conexão ainda estiver aberta, realiza seu fechamento
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    private void insertCateg (Produto prt, Connection conn)
    throws SQLException
    {
        try {
            
            conn = Conexao.getConnection();

            setSql("INSERT INTO PRODUTO_CATEGORIA "
                    + "(ID_PRODUTO, ID_CATEGORIA) "
                    + "VALUES "
                    + "(?,(SELECT ID FROM CATEGORIA "
                    + "WHERE NOME = ?))");
            
            pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pst.setInt(1, prt.getId());
            pst.setString(2, prt.getCategoria());
            
            pst.execute();
            
            rst = pst.getGeneratedKeys();
            
            if(rst.first()){
                prt.setId(rst.getInt("ID"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }    
    
}
