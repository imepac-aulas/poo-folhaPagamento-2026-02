package br.edu.imepac.dao;

import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.FolhaPagamento;

import java.util.Optional;

public interface FolhaPagamentoDAO extends DAO<FolhaPagamento, Integer> {

    Optional<FolhaPagamento> buscarPorCompetencia(Competencia competencia);
}
