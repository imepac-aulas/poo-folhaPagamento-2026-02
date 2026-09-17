package br.edu.imepac.dao;

import br.edu.imepac.entidades.Competencia;

import java.util.Optional;

public interface CompetenciaDAO extends DAO<Competencia, Integer> {

    Optional<Competencia> buscarPorMesEAno(int mes, int ano);
}
