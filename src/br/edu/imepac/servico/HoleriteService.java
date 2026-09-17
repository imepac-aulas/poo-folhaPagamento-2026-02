package br.edu.imepac.servico;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Holerite;

import java.util.List;
import java.util.Optional;

/**
 * Consulta de holerites (UC9). Resolve mês/ano em Competencia antes de
 * delegar ao DAO.
 */
public class HoleriteService {

    private final HoleriteDAO holeriteDAO;
    private final CompetenciaDAO competenciaDAO;

    public HoleriteService(HoleriteDAO holeriteDAO, CompetenciaDAO competenciaDAO) {
        this.holeriteDAO = holeriteDAO;
        this.competenciaDAO = competenciaDAO;
    }

    public Optional<Holerite> consultarPorFuncionarioECompetencia(int idFuncionario, int mes, int ano) {
        return competenciaDAO.buscarPorMesEAno(mes, ano)
                .flatMap(competencia -> holeriteDAO.buscarPorFuncionarioECompetencia(idFuncionario, competencia));
    }

    public List<Holerite> listarPorFolhaPagamento(int idFolhaPagamento) {
        return holeriteDAO.buscarPorFolhaPagamento(idFolhaPagamento);
    }
}
