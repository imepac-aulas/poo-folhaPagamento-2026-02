package br.edu.imepac.dao.mysql;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.dao.FolhaPagamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.FolhaPagamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Depende de CompetenciaDAO pra resolver a FK competencia_id de volta
 * num objeto Competencia completo ao ler do banco (sem framework de
 * ORM, quem faz esse "join manual" é o próprio DAO).
 */
public class FolhaPagamentoDAOMySQL implements FolhaPagamentoDAO {

    private final CompetenciaDAO competenciaDAO;

    public FolhaPagamentoDAOMySQL(CompetenciaDAO competenciaDAO) {
        this.competenciaDAO = competenciaDAO;
    }

    @Override
    public FolhaPagamento salvar(FolhaPagamento folhaPagamento) {
        String sql = folhaPagamento.getId() == 0
                ? "INSERT INTO folha_pagamento (competencia_id, data_geracao) VALUES (?, ?)"
                : "UPDATE folha_pagamento SET competencia_id = ?, data_geracao = ? WHERE id = ?";

        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setInt(1, folhaPagamento.getCompetencia().getId());
            comando.setDate(2, Date.valueOf(folhaPagamento.getDataGeracao()));
            if (folhaPagamento.getId() != 0) {
                comando.setInt(3, folhaPagamento.getId());
            }
            comando.executeUpdate();

            if (folhaPagamento.getId() == 0) {
                try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                    if (chaveGerada.next()) {
                        folhaPagamento.setId(chaveGerada.getInt(1));
                    }
                }
            }
            return folhaPagamento;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar folha de pagamento", e);
        }
    }

    @Override
    public Optional<FolhaPagamento> buscarPorId(Integer id) {
        String sql = "SELECT * FROM folha_pagamento WHERE id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar folha de pagamento por id", e);
        }
    }

    @Override
    public List<FolhaPagamento> listarTodos() {
        String sql = "SELECT * FROM folha_pagamento";
        List<FolhaPagamento> folhas = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                folhas.add(montar(resultado));
            }
            return folhas;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar folhas de pagamento", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM folha_pagamento WHERE id = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir folha de pagamento", e);
        }
    }

    @Override
    public Optional<FolhaPagamento> buscarPorCompetencia(Competencia competencia) {
        String sql = "SELECT * FROM folha_pagamento WHERE competencia_id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, competencia.getId());
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar folha de pagamento por competência", e);
        }
    }

    private FolhaPagamento montar(ResultSet resultado) throws SQLException {
        FolhaPagamento folha = new FolhaPagamento();
        folha.setId(resultado.getInt("id"));
        folha.setDataGeracao(resultado.getDate("data_geracao").toLocalDate());

        int idCompetencia = resultado.getInt("competencia_id");
        Competencia competencia = competenciaDAO.buscarPorId(idCompetencia)
                .orElseThrow(() -> new NoSuchElementException(
                        "Competência #" + idCompetencia + " referenciada pela folha não foi encontrada"));
        folha.setCompetencia(competencia);

        return folha;
    }
}
