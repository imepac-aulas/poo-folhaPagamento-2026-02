package br.edu.imepac.dao.lista;

import br.edu.imepac.dao.LancamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Lancamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de LancamentoDAO que guarda os dados em uma lista em
 * memória. Funciona igual para Aditivo e Desconto (polimorfismo) — o
 * DAO nunca precisa saber qual dos dois é.
 */
public class LancamentoDAOLista implements LancamentoDAO {

    private final List<Lancamento> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public Lancamento salvar(Lancamento lancamento) {
        if (lancamento.getId() == 0) {
            lancamento.setId(proximoId++);
        } else {
            excluir(lancamento.getId());
        }
        dados.add(lancamento);
        return lancamento;
    }

    @Override
    public Optional<Lancamento> buscarPorId(Integer id) {
        return dados.stream()
                .filter(lancamento -> lancamento.getId() == id)
                .findFirst();
    }

    @Override
    public List<Lancamento> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public void excluir(Integer id) {
        dados.removeIf(lancamento -> lancamento.getId() == id);
    }

    @Override
    public List<Lancamento> buscarPorFuncionario(int idFuncionario) {
        return dados.stream()
                .filter(lancamento -> lancamento.getFuncionario() != null
                        && lancamento.getFuncionario().getId() == idFuncionario)
                .toList();
    }

    @Override
    public List<Lancamento> buscarPorCompetencia(Competencia competencia) {
        return dados.stream()
                .filter(lancamento -> lancamento.getCompetencia() == competencia)
                .toList();
    }
}
