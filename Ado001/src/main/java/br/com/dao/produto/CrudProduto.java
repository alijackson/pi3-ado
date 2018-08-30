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
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author jacks
 */
public class CrudProduto {

    private Connection conn;
    private ResultSet rst;
    private PreparedStatement pst;
    private String sql;
    private StringBuilder result;
    private ArrayList<Produto> list;

    public CrudProduto() {
        setSql("");

        result = new StringBuilder();

    }

    public void insertProd(Produto prt) throws SQLException {
        try {

            conn = Conexao.getConnection();

            setSql("INSERT INTO PRODUTO (NOME, DESCRICAO, "
                    + "PRECO_COMPRA, PRECO_VENDA, QUANTIDADE, "
                    + "DT_CADASTRO) VALUES "
                    + "(? ,? ,? , ?, ?, NOW())");

            pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setString(1, prt.getNome());
            pst.setString(2, prt.getDescricao());
            pst.setFloat(3, (Float) prt.getpCompra());
            pst.setFloat(4, (Float) prt.getpVenda());
            pst.setInt(5, prt.getQuant());
//            pst.setTimestamp(6, prt.getTime());

            pst.execute();

            rst = pst.getGeneratedKeys();

            if (rst.first()) {
                prt.setId(rst.getInt(1));
            }
            insertCateg(prt, conn);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inserir os dados na fonte de dados:\n"
                    + e, "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {

            //Se a conexão ainda estiver aberta, realiza seu fechamento
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }

    /**
     * Vincular categoria ao pruduto.
     *
     * @param prt
     * @param conn
     * @throws SQLException
     */
    private void insertCateg(Produto prt, Connection conn)
            throws SQLException {
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

            if (rst.first()) {
                prt.setId(rst.getInt("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inserir os dados na fonte de dados:\n"
                    + e, "Erro", JOptionPane.ERROR_MESSAGE);
        }
        Conexao.closeConnection(conn);
    }

    /**
     * Coleta informações do banco de dados e guarda em um array list
     *
     * @return
     * @throws SQLException
     */
    public ArrayList<Produto> printAllProduto()
            throws SQLException {

        
        
        Produto prt = new Produto();

        conn = Conexao.getConnection();

        setSql("SELECT * FROM PRODUTO p "
                + "INNER JOIN CATEGORIA c ON "
                + "p.ID = c.ID ORDER BY p.NOME");

        pst = conn.prepareStatement(sql);

        rst = pst.executeQuery();

        while (rst.next()) {
            if (list == null) {
                list = new ArrayList<>();
            }

            prt.setId(rst.getInt("p.ID"));
            prt.setNome(rst.getString("p.NOME"));
            prt.setDescricao(rst.getString("p.DESCRICAO"));
            prt.setpCompra(rst.getFloat("p.PRECO_COMPRA"));
            prt.setpVenda(rst.getFloat("p.PRECO_VENDA"));
            prt.setQuant(rst.getInt("QUANTIDADE"));
            prt.setCategoria(rst.getString("c.NOME"));

            setAddList(prt);

        }

        return getList();

    }

    public void updateProd(Produto prt) throws SQLException {
        try {

            conn = Conexao.getConnection();

            setSql("UPDATE PRODUTO SET NOME=?, DESCRICAO=?, "
                    + "PRECO_COMPRA=?, PRECO_VENDA=?, QUANTIDADE=? "
                    + "WHERE (ID=?) ");
            
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, prt.getNome());
            pst.setString(2, prt.getDescricao());
            pst.setDouble(3, prt.getpCompra());
            pst.setDouble(4, prt.getpVenda());
            pst.setInt(5, prt.getQuant());
            pst.setInt(6, prt.getId());
            
            pst.execute();

            updateCateg(prt, conn);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar os dados na fonte de dados:\n"
                    + e, "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public void updateCateg(Produto prt, Connection conn) throws SQLException{
        try {

            setSql("UPDATE PRODUTO_CATEGORIA SET ID_CATEGORIA=(SELECT ID FROM CATEGORIA WHERE (NOME=?)) WHERE (ID_PRODUTO=?) ");

            pst = conn.prepareStatement(sql);

            pst.setString(1, prt.getCategoria());
            pst.setInt(2, prt.getId());

            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar os dados na fonte de dados:\n"
                    + e, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ArrayList<Produto> getList() {
        return this.list;
    }

    public void setList(ArrayList<Produto> list) {
        this.list = list;
    }

    public void setAddList(Produto prt) {
        this.list.add(prt);
    }

}
