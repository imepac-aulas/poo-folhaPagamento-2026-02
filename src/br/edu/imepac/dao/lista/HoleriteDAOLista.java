package br.edu.imepac.dao.lista;

import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Holerite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de HoleriteDAO que guarda os dados em uma lista em
 * memória.
 */
public class HoleriteDAOLista implements HoleriteDAO {

    private final List<Holerite> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public Holerite salvar(Holerite holerite) {
        if (holerite.getId() == 0) {
            holerite.setId(proximoId++);
        } else {
            excluir(holerite.getId());
        }
        dados.add(holerite);
        return holerite;
    }

    @Override
    public Optional<Holerite> buscarPorId(Integer id) {
        return dados.stream()
                .filter(holerite -> holerite.getId() == id)
                .findFirst();
    }

    @Override
    public List<Holerite> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public void excluir(Integer id) {
        dados.removeIf(holerite -> holerite.getId() == id);
    }

    @Override
    public List<Holerite> buscarPorFolhaPagamento(int idFolhaPagamento) {
        return dados.stream()
                .filter(holerite -> holerite.getFolhaPagamento() != null
                        && holerite.getFolhaPagamento().getId() == idFolhaPagamento)
                .toList();
    }

    @Override
    public Optional<Holerite> buscarPorFuncionarioECompetencia(int idFuncionario, Competencia competencia) {
        return dados.stream()
                .filter(holerite -> holerite.getFuncionario().getId() == idFuncionario
                        && holerite.getFolhaPagamento() != null
                        && holerite.getFolhaPagamento().getCompetencia() == competencia)
                .findFirst();
    }
}
