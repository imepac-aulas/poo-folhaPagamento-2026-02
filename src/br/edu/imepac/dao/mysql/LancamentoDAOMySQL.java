package br.edu.imepac.dao.mysql;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.dao.LancamentoDAO;
import br.edu.imepac.entidades.Aditivo;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Desconto;
import br.edu.imepac.entidades.Funcionario;
import br.edu.imepac.entidades.Holerite;
import br.edu.imepac.entidades.Lancamento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Aditivo e Desconto são gravados numa única tabela (lancamento),
 * discriminada pela coluna "tipo". holerite_id começa nulo e só é
 * preenchido quando o lançamento é aplicado a um Holerite (na geração
 * da folha).
 */
public class LancamentoDAOMySQL implements LancamentoDAO {

    private final FuncionarioDAO funcionarioDAO;
    private final CompetenciaDAO competenciaDAO;
    private final HoleriteDAO holeriteDAO;

    public LancamentoDAOMySQL(FuncionarioDAO funcionarioDAO, CompetenciaDAO competenciaDAO, HoleriteDAO holeriteDAO) {
        this.funcionarioDAO = funcionarioDAO;
        this.competenciaDAO = competenciaDAO;
        this.holeriteDAO = holeriteDAO;
    }

    @Override
    public Lancamento salvar(Lancamento lancamento) {
        String sql = lancamento.getId() == 0
                ? "INSERT INTO lancamento (tipo, descricao, valor, funcionario_id, competencia_id, holerite_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE lancamento SET tipo = ?, descricao = ?, valor = ?, funcionario_id = ?, "
                        + "competencia_id = ?, holerite_id = ? WHERE id = ?";

        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, tipoDe(lancamento));
            comando.setString(2, lancamento.getDescricao());
            comando.setBigDecimal(3, BigDecimal.valueOf(lancamento.getValor()));
            comando.setInt(4, lancamento.getFuncionario().getId());
            comando.setInt(5, lancamento.getCompetencia().getId());
            if (lancamento.getHolerite() == null) {
                comando.setNull(6, Types.INTEGER);
            } else {
                comando.setInt(6, lancamento.getHolerite().getId());
            }
            if (lancamento.getId() != 0) {
                comando.setInt(7, lancamento.getId());
            }
            comando.executeUpdate();

            if (lancamento.getId() == 0) {
                try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                    if (chaveGerada.next()) {
                        lancamento.setId(chaveGerada.getInt(1));
                    }
                }
            }
            return lancamento;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar lançamento", e);
        }
    }

    @Override
    public Optional<Lancamento> buscarPorId(Integer id) {
        String sql = "SELECT * FROM lancamento WHERE id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lançamento por id", e);
        }
    }

    @Override
    public List<Lancamento> listarTodos() {
        String sql = "SELECT * FROM lancamento";
        List<Lancamento> lancamentos = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                lancamentos.add(montar(resultado));
            }
            return lancamentos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar lançamentos", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM lancamento WHERE id = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir lançamento", e);
        }
    }

    @Override
    public List<Lancamento> buscarPorFuncionario(int idFuncionario) {
        String sql = "SELECT * FROM lancamento WHERE funcionario_id = ?";
        List<Lancamento> lancamentos = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idFuncionario);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lancamentos.add(montar(resultado));
                }
            }
            return lancamentos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lançamentos por funcionário", e);
        }
    }

    @Override
    public List<Lancamento> buscarPorCompetencia(Competencia competencia) {
        String sql = "SELECT * FROM lancamento WHERE competencia_id = ?";
        List<Lancamento> lancamentos = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, competencia.getId());
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lancamentos.add(montar(resultado));
                }
            }
            return lancamentos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lançamentos por competência", e);
        }
    }

    private String tipoDe(Lancamento lancamento) {
        if (lancamento instanceof Aditivo) {
            return "ADITIVO";
        }
        if (lancamento instanceof Desconto) {
            return "DESCONTO";
        }
        throw new IllegalArgumentException("Tipo de lançamento desconhecido: " + lancamento.getClass());
    }

    private Lancamento montar(ResultSet resultado) throws SQLException {
        String tipo = resultado.getString("tipo");
        Lancamento lancamento = switch (tipo) {
            case "ADITIVO" -> new Aditivo();
            case "DESCONTO" -> new Desconto();
            default -> throw new IllegalStateException("Tipo de lançamento desconhecido no banco: " + tipo);
        };

        lancamento.setId(resultado.getInt("id"));
        lancamento.setDescricao(resultado.getString("descricao"));
        lancamento.setValor(resultado.getBigDecimal("valor").doubleValue());

        int idFuncionario = resultado.getInt("funcionario_id");
        Funcionario funcionario = funcionarioDAO.buscarPorId(idFuncionario)
                .orElseThrow(() -> new NoSuchElementException(
                        "Funcionário #" + idFuncionario + " referenciado pelo lançamento não foi encontrado"));
        lancamento.setFuncionario(funcionario);

        int idCompetencia = resultado.getInt("competencia_id");
        Competencia competencia = competenciaDAO.buscarPorId(idCompetencia)
                .orElseThrow(() -> new NoSuchElementException(
                        "Competência #" + idCompetencia + " referenciada pelo lançamento não foi encontrada"));
        lancamento.setCompetencia(competencia);

        int idHolerite = resultado.getInt("holerite_id");
        if (!resultado.wasNull()) {
            Holerite holerite = holeriteDAO.buscarPorId(idHolerite)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Holerite #" + idHolerite + " referenciado pelo lançamento não foi encontrado"));
            lancamento.setHolerite(holerite);
        }

        return lancamento;
    }
}
