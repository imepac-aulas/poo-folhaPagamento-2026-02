package br.edu.imepac.dao.mysql;

import br.edu.imepac.dao.FolhaPagamentoDAO;
import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.FolhaPagamento;
import br.edu.imepac.entidades.Funcionario;
import br.edu.imepac.entidades.Holerite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HoleriteDAOMySQL implements HoleriteDAO {

    private final FuncionarioDAO funcionarioDAO;
    private final FolhaPagamentoDAO folhaPagamentoDAO;

    public HoleriteDAOMySQL(FuncionarioDAO funcionarioDAO, FolhaPagamentoDAO folhaPagamentoDAO) {
        this.funcionarioDAO = funcionarioDAO;
        this.folhaPagamentoDAO = folhaPagamentoDAO;
    }

    @Override
    public Holerite salvar(Holerite holerite) {
        String sql = holerite.getId() == 0
                ? "INSERT INTO holerite (folha_pagamento_id, funcionario_id, valor_bruto, valor_liquido) "
                        + "VALUES (?, ?, ?, ?)"
                : "UPDATE holerite SET folha_pagamento_id = ?, funcionario_id = ?, valor_bruto = ?, "
                        + "valor_liquido = ? WHERE id = ?";

        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setInt(1, holerite.getFolhaPagamento().getId());
            comando.setInt(2, holerite.getFuncionario().getId());
            comando.setBigDecimal(3, java.math.BigDecimal.valueOf(holerite.getValorBruto()));
            comando.setBigDecimal(4, java.math.BigDecimal.valueOf(holerite.getValorLiquido()));
            if (holerite.getId() != 0) {
                comando.setInt(5, holerite.getId());
            }
            comando.executeUpdate();

            if (holerite.getId() == 0) {
                try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                    if (chaveGerada.next()) {
                        holerite.setId(chaveGerada.getInt(1));
                    }
                }
            }
            return holerite;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar holerite", e);
        }
    }

    @Override
    public Optional<Holerite> buscarPorId(Integer id) {
        String sql = "SELECT * FROM holerite WHERE id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar holerite por id", e);
        }
    }

    @Override
    public List<Holerite> listarTodos() {
        String sql = "SELECT * FROM holerite";
        List<Holerite> holerites = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                holerites.add(montar(resultado));
            }
            return holerites;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar holerites", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM holerite WHERE id = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir holerite", e);
        }
    }

    @Override
    public List<Holerite> buscarPorFolhaPagamento(int idFolhaPagamento) {
        String sql = "SELECT * FROM holerite WHERE folha_pagamento_id = ?";
        List<Holerite> holerites = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idFolhaPagamento);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    holerites.add(montar(resultado));
                }
            }
            return holerites;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar holerites por folha de pagamento", e);
        }
    }

    @Override
    public Optional<Holerite> buscarPorFuncionarioECompetencia(int idFuncionario, Competencia competencia) {
        String sql = "SELECT h.* FROM holerite h "
                + "JOIN folha_pagamento f ON f.id = h.folha_pagamento_id "
                + "WHERE h.funcionario_id = ? AND f.competencia_id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idFuncionario);
            comando.setInt(2, competencia.getId());
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar holerite por funcionário e competência", e);
        }
    }

    private Holerite montar(ResultSet resultado) throws SQLException {
        Holerite holerite = new Holerite();
        holerite.setId(resultado.getInt("id"));
        holerite.setValorBruto(resultado.getBigDecimal("valor_bruto").doubleValue());
        holerite.setValorLiquido(resultado.getBigDecimal("valor_liquido").doubleValue());

        int idFuncionario = resultado.getInt("funcionario_id");
        Funcionario funcionario = funcionarioDAO.buscarPorId(idFuncionario)
                .orElseThrow(() -> new NoSuchElementException(
                        "Funcionário #" + idFuncionario + " referenciado pelo holerite não foi encontrado"));
        holerite.setFuncionario(funcionario);

        int idFolha = resultado.getInt("folha_pagamento_id");
        FolhaPagamento folha = folhaPagamentoDAO.buscarPorId(idFolha)
                .orElseThrow(() -> new NoSuchElementException(
                        "Folha de pagamento #" + idFolha + " referenciada pelo holerite não foi encontrada"));
        holerite.setFolhaPagamento(folha);

        return holerite;
    }
}
