package br.edu.imepac.entidades;

public class Desconto extends Lancamento {

    @Override
    public double aplicar(double valor) {
        return valor - getValor();
    }
}
