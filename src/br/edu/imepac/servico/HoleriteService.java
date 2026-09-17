package br.edu.imepac.servico;

import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.entidades.Holerite;

import java.util.List;
import java.util.Optional;

/**
 * Consulta de holerites (UC9). Resolve mês/ano em Competencia através
 * de {@link CompetenciaService} antes de delegar ao DAO.
 */
public class HoleriteService {

    private final HoleriteDAO holeriteDAO;
    private final CompetenciaService competenciaService;

    public HoleriteService(HoleriteDAO holeriteDAO, CompetenciaService competenciaService) {
        this.holeriteDAO = holeriteDAO;
        this.competenciaService = competenciaService;
    }

    public Optional<Holerite> consultarPorFuncionarioECompetencia(int idFuncionario, int mes, int ano) {
        return competenciaService.buscarPorMesEAno(mes, ano)
                .flatMap(competencia -> holeriteDAO.buscarPorFuncionarioECompetencia(idFuncionario, competencia));
    }

    public List<Holerite> listarPorFolhaPagamento(int idFolhaPagamento) {
        return holeriteDAO.buscarPorFolhaPagamento(idFolhaPagamento);
    }
}
