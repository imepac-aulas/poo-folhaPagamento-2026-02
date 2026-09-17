package br.edu.imepac.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregação: reúne os Lancamento (Aditivo/Desconto) aplicados ao
 * Funcionario naquela competência, junto com os valores bruto e líquido
 * apurados.
 */
public class Holerite {
    private int id;
    private FolhaPagamento folhaPagamento;
    private Funcionario funcionario;
    private double valorBruto;
    private double valorLiquido;
    private final List<Lancamento> lancamentos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FolhaPagamento getFolhaPagamento() {
        return folhaPagamento;
    }

    /**
     * Setter "simples", usado internamente por FolhaPagamento.adicionarHolerite
     * para manter a composição sincronizada.
     */
    public void setFolhaPagamento(FolhaPagamento folhaPagamento) {
        this.folhaPagamento = folhaPagamento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public double getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(double valorBruto) {
        this.valorBruto = valorBruto;
    }

    public double getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(double valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public List<Lancamento> getLancamentos() {
        return Collections.unmodifiableList(lancamentos);
    }

    /**
     * Adiciona um lançamento a este holerite, sincronizando os dois
     * lados da agregação (o lançamento passa a saber a qual holerite
     * foi aplicado).
     */
    public void adicionarLancamento(Lancamento lancamento) {
        lancamentos.add(lancamento);
        lancamento.setHolerite(this);
    }
}
