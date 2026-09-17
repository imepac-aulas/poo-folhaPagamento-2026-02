package br.edu.imepac.dao.mysql;

import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.entidades.Bibliotecario;
import br.edu.imepac.entidades.Coordenador;
import br.edu.imepac.entidades.Funcionario;
import br.edu.imepac.entidades.Professor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Professor, Coordenador e Bibliotecario são gravados numa única
 * tabela (funcionario), discriminada pela coluna "tipo" — não há
 * mapeamento objeto-relacional automático (sem framework), então quem
 * decide qual subclasse reconstruir na leitura é este DAO.
 */
public class FuncionarioDAOMySQL implements FuncionarioDAO {

    private static final String COLUNAS =
            "tipo, nome, email, idade, quantidade_aulas, valor_aula, valor_base, percentual, quantidade_alunos";

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        String sql = funcionario.getId() == 0
                ? "INSERT INTO funcionario (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "UPDATE funcionario SET tipo = ?, nome = ?, email = ?, idade = ?, quantidade_aulas = ?, "
                        + "valor_aula = ?, valor_base = ?, percentual = ?, quantidade_alunos = ? WHERE id = ?";

        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherParametros(comando, funcionario);
            if (funcionario.getId() != 0) {
                comando.setInt(10, funcionario.getId());
            }
            comando.executeUpdate();

            if (funcionario.getId() == 0) {
                try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                    if (chaveGerada.next()) {
                        funcionario.setId(chaveGerada.getInt(1));
                    }
                }
            }
            return funcionario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionário", e);
        }
    }

    @Override
    public Optional<Funcionario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(montar(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por id", e);
        }
    }

    @Override
    public List<Funcionario> listarTodos() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                funcionarios.add(montar(resultado));
            }
            return funcionarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        try (Connection conexao = ConexaoMySQL.obter();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM funcionario WHERE id = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir funcionário", e);
        }
    }

    private void preencherParametros(PreparedStatement comando, Funcionario funcionario) throws SQLException {
        comando.setString(1, tipoDe(funcionario));
        comando.setString(2, funcionario.getNome());
        comando.setString(3, funcionario.getEmail());
        comando.setInt(4, funcionario.getIdade());

        if (funcionario instanceof Professor professor) {
            comando.setInt(5, professor.getQuantidadeAulas());
            comando.setBigDecimal(6, BigDecimal.valueOf(professor.getValorAula()));
            comando.setNull(7, Types.DECIMAL);
            comando.setNull(8, Types.DECIMAL);
            comando.setNull(9, Types.INTEGER);
        } else if (funcionario instanceof Coordenador coordenador) {
            comando.setNull(5, Types.INTEGER);
            comando.setNull(6, Types.DECIMAL);
            comando.setBigDecimal(7, BigDecimal.valueOf(coordenador.getValorBase()));
            comando.setBigDecimal(8, BigDecimal.valueOf(coordenador.getPercentual()));
            comando.setInt(9, coordenador.getQuantidadeAlunos());
        } else if (funcionario instanceof Bibliotecario bibliotecario) {
            comando.setNull(5, Types.INTEGER);
            comando.setNull(6, Types.DECIMAL);
            comando.setBigDecimal(7, BigDecimal.valueOf(bibliotecario.getValorBase()));
            comando.setNull(8, Types.DECIMAL);
            comando.setNull(9, Types.INTEGER);
        } else {
            throw new IllegalArgumentException("Tipo de funcionário desconhecido: " + funcionario.getClass());
        }
    }

    private String tipoDe(Funcionario funcionario) {
        if (funcionario instanceof Professor) {
            return "PROFESSOR";
        }
        if (funcionario instanceof Coordenador) {
            return "COORDENADOR";
        }
        if (funcionario instanceof Bibliotecario) {
            return "BIBLIOTECARIO";
        }
        throw new IllegalArgumentException("Tipo de funcionário desconhecido: " + funcionario.getClass());
    }

    private Funcionario montar(ResultSet resultado) throws SQLException {
        String tipo = resultado.getString("tipo");
        Funcionario funcionario = switch (tipo) {
            case "PROFESSOR" -> {
                Professor professor = new Professor();
                professor.setQuantidadeAulas(resultado.getInt("quantidade_aulas"));
                professor.setValorAula(resultado.getBigDecimal("valor_aula").doubleValue());
                yield professor;
            }
            case "COORDENADOR" -> {
                Coordenador coordenador = new Coordenador();
                coordenador.setValorBase(resultado.getBigDecimal("valor_base").doubleValue());
                coordenador.setPercentual(resultado.getBigDecimal("percentual").doubleValue());
                coordenador.setQuantidadeAlunos(resultado.getInt("quantidade_alunos"));
                yield coordenador;
            }
            case "BIBLIOTECARIO" -> {
                Bibliotecario bibliotecario = new Bibliotecario();
                bibliotecario.setValorBase(resultado.getBigDecimal("valor_base").doubleValue());
                yield bibliotecario;
            }
            default -> throw new IllegalStateException("Tipo de funcionário desconhecido no banco: " + tipo);
        };

        funcionario.setId(resultado.getInt("id"));
        funcionario.setNome(resultado.getString("nome"));
        funcionario.setEmail(resultado.getString("email"));
        funcionario.setIdade(resultado.getInt("idade"));
        return funcionario;
    }
}
