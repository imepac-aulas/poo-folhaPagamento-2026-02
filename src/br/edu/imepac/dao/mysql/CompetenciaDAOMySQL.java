package br.edu.imepac.dao.mysql;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.entidades.Competencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompetenciaDAOMySQL implements CompetenciaDAO {

    @Override
    public Competencia salvar(Competencia competencia) {
        String sql = competencia.getId() == 0
                ? "INSERT INTO competencia (mes, ano) VALUES (?, ?)"
                : "UPDATE competencia SET mes = ?, ano = ? WHERE id = ?";

        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setInt(1, competencia.getMes());
            comando.setInt(2, competencia.getAno());
            if (competencia.getId() != 0) {
                comando.setInt(3, competencia.getId());
            }
            comando.executeUpdate();

            if (competencia.getId() == 0) {
                try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                    if (chaveGerada.next()) {
                        competencia.setId(chaveGerada.getInt(1));
                    }
                }
            }
            return competencia;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar competência", e);
        }
    }

    @Override
    public Optional<Competencia> buscarPorId(Integer id) {
        return buscarUma("SELECT * FROM competencia WHERE id = ?", id);
    }

    @Override
    public List<Competencia> listarTodos() {
        String sql = "SELECT * FROM competencia";
        List<Competencia> competencias = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                competencias.add(montar(resultado));
            }
            return competencias;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar competências", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM competencia WHERE id = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir competência", e);
        }
    }

    @Override
    public Optional<Competencia> buscarPorMesEAno(int mes, int ano) {
        String sql = "SELECT * FROM competencia WHERE mes = ? AND ano = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, mes);
            comando.setInt(2, ano);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competência por mês/ano", e);
        }
    }

    private Optional<Competencia> buscarUma(String sql, int id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competência", e);
        }
    }

    private Competencia montar(ResultSet resultado) throws SQLException {
        Competencia competencia = new Competencia();
        competencia.setId(resultado.getInt("id"));
        competencia.setMes(resultado.getInt("mes"));
        competencia.setAno(resultado.getInt("ano"));
        return competencia;
    }
}
