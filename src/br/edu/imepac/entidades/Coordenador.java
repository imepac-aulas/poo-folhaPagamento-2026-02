package br.edu.imepac.entidades;

public class Coordenador extends Funcionario {
    private double valorBase;
    private double percentual;
    private int quantidadeAlunos;

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }

    public int getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(int quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    @Override
    public double calcularRemuneracao() {
        return valorBase + (percentual * quantidadeAlunos);
    }
}
