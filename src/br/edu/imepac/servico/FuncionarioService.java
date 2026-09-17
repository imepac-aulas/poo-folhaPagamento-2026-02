package br.edu.imepac.servico;

import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.entidades.Funcionario;

import java.util.List;
import java.util.Optional;

/**
 * Orquestra o cadastro/consulta de funcionários. A validação de dados
 * (idade mínima, idade negativa) já acontece dentro da própria entidade
 * (Funcionario.setIdade) — aqui só delegamos a persistência ao DAO.
 */
public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public Funcionario cadastrar(Funcionario funcionario) {
        return funcionarioDAO.salvar(funcionario);
    }

    public Optional<Funcionario> buscarPorId(int id) {
        return funcionarioDAO.buscarPorId(id);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioDAO.listarTodos();
    }
}
