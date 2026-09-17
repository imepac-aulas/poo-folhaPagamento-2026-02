package br.edu.imepac.entidades;

public class Professor extends Funcionario {
    private int quantidadeAulas;
    private double valorAula;

    public int getQuantidadeAulas() {
        return quantidadeAulas;
    }

    public void setQuantidadeAulas(int quantidadeAulas) {
        this.quantidadeAulas = quantidadeAulas;
    }

    public double getValorAula() {
        return valorAula;
    }

    public void setValorAula(double valorAula) {
        this.valorAula = valorAula;
    }

    @Override
    public double calcularRemuneracao() {
        return quantidadeAulas * valorAula;
    }
}
