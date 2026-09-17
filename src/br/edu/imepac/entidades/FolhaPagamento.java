package br.edu.imepac.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composição: os Holerite não existem sem a FolhaPagamento que os
 * originou. A composição é bidirecional — cada Holerite guarda a
 * referência de volta — para permitir navegar até a Competencia a
 * partir de um Holerite isolado (Holerite -> FolhaPagamento -> Competencia).
 */
public class FolhaPagamento {
    private int id;
    private Competencia competencia;
    private LocalDate dataGeracao;
    private final List<Holerite> holerites = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    /**
     * Setter "simples", usado internamente por Competencia.definirFolhaPagamento
     * para manter a composição sincronizada. Para criar a folha de uma
     * competência, prefira Competencia.definirFolhaPagamento(...).
     */
    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDate dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public List<Holerite> getHolerites() {
        return Collections.unmodifiableList(holerites);
    }

    /**
     * Adiciona um holerite a esta folha, sincronizando os dois lados da
     * composição.
     */
    public void adicionarHolerite(Holerite holerite) {
        holerites.add(holerite);
        holerite.setFolhaPagamento(this);
    }
}
