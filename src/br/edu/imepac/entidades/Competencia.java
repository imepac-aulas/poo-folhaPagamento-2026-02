package br.edu.imepac.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Raiz de composição do domínio: identifica um período (mês/ano) e
 * possui a FolhaPagamento daquele mês (se já tiver sido gerada) e todos
 * os Lancamento lançados no período. Ver diagrama de classes para a
 * justificativa dessa modelagem.
 */
public class Competencia {
    private int id;
    private int mes;
    private int ano;
    private FolhaPagamento folhaPagamento;
    private final List<Lancamento> lancamentos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public FolhaPagamento getFolhaPagamento() {
        return folhaPagamento;
    }

    /**
     * Define a folha de pagamento desta competência, sincronizando os
     * dois lados da composição (evita a associação ficar dessincronizada).
     */
    public void definirFolhaPagamento(FolhaPagamento folhaPagamento) {
        this.folhaPagamento = folhaPagamento;
        if (folhaPagamento != null) {
            folhaPagamento.setCompetencia(this);
        }
    }

    public List<Lancamento> getLancamentos() {
        return Collections.unmodifiableList(lancamentos);
    }

    /**
     * Adiciona um lançamento a esta competência, sincronizando os dois
     * lados da composição.
     */
    public void adicionarLancamento(Lancamento lancamento) {
        lancamentos.add(lancamento);
        lancamento.setCompetencia(this);
    }
}
