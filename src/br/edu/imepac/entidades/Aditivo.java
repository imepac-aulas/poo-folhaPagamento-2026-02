package br.edu.imepac.entidades;

public class Aditivo extends Lancamento {

    @Override
    public double aplicar(double valor) {
        return valor + getValor();
    }
}
