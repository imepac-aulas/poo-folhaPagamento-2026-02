package br.edu.imepac.dao.lista;

import br.edu.imepac.dao.FolhaPagamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.FolhaPagamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de FolhaPagamentoDAO que guarda os dados em uma lista
 * em memória.
 */
public class FolhaPagamentoDAOLista implements FolhaPagamentoDAO {

    private final List<FolhaPagamento> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public FolhaPagamento salvar(FolhaPagamento folhaPagamento) {
        if (folhaPagamento.getId() == 0) {
            folhaPagamento.setId(proximoId++);
        } else {
            excluir(folhaPagamento.getId());
        }
        dados.add(folhaPagamento);
        return folhaPagamento;
    }

    @Override
    public Optional<FolhaPagamento> buscarPorId(Integer id) {
        return dados.stream()
                .filter(folha -> folha.getId() == id)
                .findFirst();
    }

    @Override
    public List<FolhaPagamento> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public void excluir(Integer id) {
        dados.removeIf(folha -> folha.getId() == id);
    }

    @Override
    public Optional<FolhaPagamento> buscarPorCompetencia(Competencia competencia) {
        return dados.stream()
                .filter(folha -> folha.getCompetencia() == competencia)
                .findFirst();
    }
}
