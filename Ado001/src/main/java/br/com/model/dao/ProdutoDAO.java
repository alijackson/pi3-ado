/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.model.dao;

import br.com.connection.Conexao;
import br.com.model.produto.Produto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author david.sdcruz
 */
public class ProdutoDAO {

    //converter data
    private SimpleDateFormat dataEntrada = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dataBanco = new SimpleDateFormat("yyyy-MM-dd");

    public void excluir(int id) {

        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement("DELETE FROM produto WHERE idProduto = ?;");
            stmt.setInt(1, id);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluído  com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao atualizar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt);
        }

    }

    public void editarProduto(Produto p, int id) {

        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT codProduto,nomeProduto, fornecedor,preco,qtd,descricao, genero, dataFab, plataforma FROM produto  WHERE idProduto = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {

                p.setCodProd(rs.getString("codProduto"));
                p.setNome(rs.getString("nomeProduto"));
                p.setFornecedor(rs.getString("fornecedor"));
                p.setPreco(Float.parseFloat(rs.getString("preco")));
                p.setQtd(Integer.parseInt(rs.getString("qtd")));
                p.setDescricao(rs.getString("descricao"));
                p.setGenero(rs.getString("genero"));

                String dataB = dataBanco.format(rs.getDate("dataFab"));
                Date dataSql = java.sql.Date.valueOf(dataB);
                String dataSaidaTela = dataEntrada.format(dataSql);

                p.setData(dataSaidaTela);
                p.setPlataforma(rs.getString("plataforma"));

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs);

        }

    }
    
    public List<Produto> read() {
        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Produto> produtos = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM produto");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Produto p = new Produto();
                
                p.setId(rs.getInt("idProduto"));
                p.setCodProd(rs.getString("codProduto"));
                p.setNome(rs.getString("nomeProduto"));
                p.setFornecedor(rs.getString("fornecedor"));
                p.setPreco(rs.getFloat("preco"));
                p.setQtd(rs.getInt("qtd"));
                p.setDescricao(rs.getString("descricao"));
                p.setGenero(rs.getString("genero"));
                p.setData(String.valueOf(rs.getDate("dataFab")));
                p.setPlataforma(rs.getString("plataforma"));
                produtos.add(p);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs);
            
        }
        return produtos;
        
    }

    public void update(Produto p) {

        Connection con = Conexao.getConnection();
        PreparedStatement stmt = null;

        try {

            // função que alterar o formato da data de  dd/MM/yyyy para yyyy/MM/dd
            Date data1 = alterarFormatoData(dataEntrada, dataBanco, p);

            stmt = con.prepareStatement("UPDATE produto SET codProduto = ?,nomeProduto = ?,fornecedor = ?,preco = ?,qtd = ?, "
                    + "descricao = ?, genero = ?, dataFab = ?, plataforma = ? WHERE idProduto = ?");
            stmt.setString(1, p.getCodProd());
            stmt.setString(2, p.getNome());
            stmt.setString(3, p.getFornecedor());
            stmt.setFloat(4, p.getPreco());
            stmt.setInt(5, p.getQtd());
            stmt.setString(6, p.getDescricao());
            stmt.setString(7, p.getGenero());
            stmt.setDate(8, data1);
            stmt.setString(9, p.getPlataforma());
            stmt.setInt(10, p.getId());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Alterado com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao atualizar" + ex);
        } finally {
            Conexao.closeConnection(con, stmt);
        }

    }

    private Date alterarFormatoData(SimpleDateFormat dataEntrada, SimpleDateFormat dataBanco, Produto p) {

        String srt;
        Date data1 = null;

        try {
            srt = dataBanco.format(dataEntrada.parse(p.getData()));
            data1 = java.sql.Date.valueOf(srt);
        } catch (ParseException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data1;

    }

}
