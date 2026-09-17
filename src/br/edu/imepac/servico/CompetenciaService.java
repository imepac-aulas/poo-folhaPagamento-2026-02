package br.edu.imepac.servico;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.entidades.Competencia;

import java.util.List;
import java.util.Optional;

/**
 * Concentra o acesso a Competencia. Nenhum menu cadastra uma
 * Competencia diretamente — ela nasce implicitamente na primeira vez
 * que um Lancamento é registrado para um mês/ano ainda não existente
 * (ver {@link #buscarOuCriar(int, int)}), conforme o fluxo descrito no
 * README.
 */
public class CompetenciaService {

    private final CompetenciaDAO competenciaDAO;

    public CompetenciaService(CompetenciaDAO competenciaDAO) {
        this.competenciaDAO = competenciaDAO;
    }

    public Optional<Competencia> buscarPorMesEAno(int mes, int ano) {
        return competenciaDAO.buscarPorMesEAno(mes, ano);
    }

    /**
     * Busca a competência do mês/ano informado; se ainda não existir
     * nenhum lançamento (nem folha) daquele período, cria uma nova.
     */
    public Competencia buscarOuCriar(int mes, int ano) {
        return competenciaDAO.buscarPorMesEAno(mes, ano)
                .orElseGet(() -> {
                    Competencia competencia = new Competencia();
                    competencia.setMes(mes);
                    competencia.setAno(ano);
                    return competenciaDAO.salvar(competencia);
                });
    }

    public List<Competencia> listarTodas() {
        return competenciaDAO.listarTodos();
    }
}
