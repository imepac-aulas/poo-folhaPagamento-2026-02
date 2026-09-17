package br.edu.imepac.dao;

import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Holerite;

import java.util.List;
import java.util.Optional;

public interface HoleriteDAO extends DAO<Holerite, Integer> {

    List<Holerite> buscarPorFolhaPagamento(int idFolhaPagamento);

    Optional<Holerite> buscarPorFuncionarioECompetencia(int idFuncionario, Competencia competencia);
}
