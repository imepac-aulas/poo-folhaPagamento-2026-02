package br.edu.imepac.front;

/**
 * Sinaliza que uma opção de menu foi escolhida corretamente, mas a
 * funcionalidade por trás dela ainda depende da camada de serviço e/ou
 * DAO, que ainda não foram implementadas.
 *
 * É lançada de propósito (não é um erro inesperado) para que a camada de
 * front tenha um comportamento único e previsível diante de qualquer
 * funcionalidade pendente: mostra uma mensagem clara e volta pro menu,
 * em vez de travar ou fingir que a operação foi concluída.
 */
public class FuncionalidadeNaoImplementadaException extends RuntimeException {

    public FuncionalidadeNaoImplementadaException(String funcionalidade) {
        super("Funcionalidade \"" + funcionalidade + "\" ainda não implementada "
                + "(depende da camada de serviço/DAO, que ainda não existe).");
    }
}
