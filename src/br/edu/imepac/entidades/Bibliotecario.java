package br.edu.imepac.entidades;

public class Bibliotecario extends Funcionario {
    private double valorBase;

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    @Override
    public double calcularRemuneracao() {
        return valorBase;
    }
}
