package br.edu.imepac.dao;

import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.Lancamento;

import java.util.List;

public interface LancamentoDAO extends DAO<Lancamento, Integer> {

    List<Lancamento> buscarPorFuncionario(int idFuncionario);

    List<Lancamento> buscarPorCompetencia(Competencia competencia);
}
