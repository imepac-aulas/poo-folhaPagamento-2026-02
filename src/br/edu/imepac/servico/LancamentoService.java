package br.edu.imepac.servico;

import br.edu.imepac.dao.LancamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Lancamento;

import java.util.List;

/**
 * Orquestra o cadastro/consulta de aditivos e descontos. Funciona de
 * forma polimórfica: recebe/retorna Lancamento e não precisa saber se é
 * um Aditivo ou um Desconto.
 */
public class LancamentoService {

    private final LancamentoDAO lancamentoDAO;

    public LancamentoService(LancamentoDAO lancamentoDAO) {
        this.lancamentoDAO = lancamentoDAO;
    }

    public Lancamento cadastrar(Lancamento lancamento) {
        return lancamentoDAO.salvar(lancamento);
    }

    public List<Lancamento> listarPorFuncionario(int idFuncionario) {
        return lancamentoDAO.buscarPorFuncionario(idFuncionario);
    }

    public List<Lancamento> listarPorCompetencia(Competencia competencia) {
        return lancamentoDAO.buscarPorCompetencia(competencia);
    }
}
