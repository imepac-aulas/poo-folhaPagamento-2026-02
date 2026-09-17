package br.edu.imepac.servico;

import br.edu.imepac.dao.LancamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Lancamento;

import java.util.List;

/**
 * Orquestra o cadastro/consulta de aditivos e descontos. Funciona de
 * forma polimórfica: recebe/retorna Lancamento e não precisa saber se é
 * um Aditivo ou um Desconto.
 *
 * A competência do lançamento não é escolhida de uma lista existente —
 * é resolvida (ou criada, se for a primeira vez que esse mês/ano
 * aparece) através de {@link CompetenciaService#buscarOuCriar(int, int)}.
 */
public class LancamentoService {

    private final LancamentoDAO lancamentoDAO;
    private final CompetenciaService competenciaService;

    public LancamentoService(LancamentoDAO lancamentoDAO, CompetenciaService competenciaService) {
        this.lancamentoDAO = lancamentoDAO;
        this.competenciaService = competenciaService;
    }

    public Lancamento cadastrar(Lancamento lancamento, int mes, int ano) {
        Competencia competencia = competenciaService.buscarOuCriar(mes, ano);
        competencia.adicionarLancamento(lancamento);
        return lancamentoDAO.salvar(lancamento);
    }

    public List<Lancamento> listarPorFuncionario(int idFuncionario) {
        return lancamentoDAO.buscarPorFuncionario(idFuncionario);
    }

    public List<Lancamento> listarPorCompetencia(int mes, int ano) {
        return competenciaService.buscarPorMesEAno(mes, ano)
                .map(lancamentoDAO::buscarPorCompetencia)
                .orElseGet(List::of);
    }
}
