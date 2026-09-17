package br.edu.imepac.dao.lista;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.entidades.Competencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de CompetenciaDAO que guarda os dados em uma lista em
 * memória.
 */
public class CompetenciaDAOLista implements CompetenciaDAO {

    private final List<Competencia> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public Competencia salvar(Competencia competencia) {
        if (competencia.getId() == 0) {
            competencia.setId(proximoId++);
        } else {
            excluir(competencia.getId());
        }
        dados.add(competencia);
        return competencia;
    }

    @Override
    public Optional<Competencia> buscarPorId(Integer id) {
        return dados.stream()
                .filter(competencia -> competencia.getId() == id)
                .findFirst();
    }

    @Override
    public List<Competencia> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public void excluir(Integer id) {
        dados.removeIf(competencia -> competencia.getId() == id);
    }

    @Override
    public Optional<Competencia> buscarPorMesEAno(int mes, int ano) {
        return dados.stream()
                .filter(competencia -> competencia.getMes() == mes && competencia.getAno() == ano)
                .findFirst();
    }
}
