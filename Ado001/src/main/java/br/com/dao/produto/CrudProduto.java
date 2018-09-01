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
import java.util.List;
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
    /*
    public ArrayList<Produto> printAllProduto()
            throws SQLException {

        Produto prt = new Produto();

        conn = Conexao.getConnection();

        setSql("SELECT * FROM PRODUTO p "
                + "INNER JOIN PRODUTO_CATEGORIA c ON "
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

    }*/
    public List<Produto> read() {
        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Produto> produtos = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT p.*, c.NOME FROM PRODUTO p "
                    + "INNER JOIN PRODUTO_CATEGORIA pc ON p.ID = pc.ID_PRODUTO "
                    + "INNER JOIN CATEGORIA c ON pc.ID_CATEGORIA = c.ID");
            rs = stmt.executeQuery();
//            rs.first();
            while (rs.next()) {
                Produto p = new Produto();

                p.setId(rs.getInt("p.ID"));
                p.setNome(rs.getString("p.NOME"));
                p.setDescricao(rs.getString("p.DESCRICAO"));
                p.setpCompra(rs.getFloat("p.PRECO_COMPRA"));
                p.setpVenda(rs.getFloat("p.PRECO_VENDA"));
                p.setQuant(rs.getInt("p.QUANTIDADE"));
                p.setTime(rs.getTimestamp("p.DT_CADASTRO"));
                p.setCategoria(rs.getString("c.NOME"));
                produtos.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs);

        }
        return produtos;

    }

    public Produto readOne(int produto) {
        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto p = new Produto();

        try {
            stmt = con.prepareStatement("SELECT p.*, c.NOME FROM PRODUTO p "
                    + "INNER JOIN PRODUTO_CATEGORIA pc ON p.ID = pc.ID_PRODUTO "
                    + "INNER JOIN CATEGORIA c ON pc.ID_CATEGORIA = c.ID "
                    + "WHERE p.ID = ?");
            stmt.setInt(1, produto);

            rs = stmt.executeQuery();
            rs.next();

            p.setId(rs.getInt("p.ID"));
            p.setNome(rs.getString("p.NOME"));
            p.setDescricao(rs.getString("p.DESCRICAO"));
            p.setpCompra(rs.getFloat("p.PRECO_COMPRA"));
            p.setpVenda(rs.getFloat("p.PRECO_VENDA"));
            p.setQuant(rs.getInt("p.QUANTIDADE"));
            p.setTime(rs.getTimestamp("p.DT_CADASTRO"));
            p.setCategoria(rs.getString("c.NOME"));

        } catch (SQLException ex) {
            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs);

        }
        return p;
    }

    public void excluir(int id) {

        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {

            stmt = con.prepareStatement("DELETE FROM PRODUTO_CATEGORIA  WHERE ID_PRODUTO = ?");
            stmt.setInt(1, id);
            stmt2 = con.prepareStatement("DELETE FROM PRODUTO  WHERE ID = ?");
            stmt2.setInt(1, id);

            stmt.executeUpdate();
            stmt2.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluído  com sucesso");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao atualizar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt);
        }
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

    public void updateCateg(Produto prt, Connection conn) throws SQLException {
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
