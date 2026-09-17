package br.edu.imepac.dao.lista;

import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.entidades.Funcionario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de FuncionarioDAO que guarda os dados em uma lista em
 * memória. Não persiste em disco/banco — os dados somem quando o
 * programa termina. Serve para o sistema funcionar de ponta a ponta
 * antes de existir uma implementação com banco de dados de verdade.
 */
public class FuncionarioDAOLista implements FuncionarioDAO {

    private final List<Funcionario> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        if (funcionario.getId() == 0) {
            funcionario.setId(proximoId++);
        } else {
            excluir(funcionario.getId());
        }
        dados.add(funcionario);
        return funcionario;
    }

    @Override
    public Optional<Funcionario> buscarPorId(Integer id) {
        return dados.stream()
                .filter(funcionario -> funcionario.getId() == id)
                .findFirst();
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public void excluir(Integer id) {
        dados.removeIf(funcionario -> funcionario.getId() == id);
    }
}
