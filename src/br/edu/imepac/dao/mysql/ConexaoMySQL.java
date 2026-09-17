package br.edu.imepac.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC com o MySQL (sem pool de conexões — cada
 * operação de DAO abre e fecha a sua própria, o que é suficiente para
 * o escopo do projeto). As configurações batem com os valores padrão
 * do docker-compose.yml e podem ser sobrescritas por variáveis de
 * ambiente, sem precisar recompilar o código.
 */
public final class ConexaoMySQL {

    private static final String URL =
            valorOuPadrao("DB_URL", "jdbc:mysql://localhost:3306/folha_pagamento");
    private static final String USUARIO = valorOuPadrao("DB_USUARIO", "root");
    private static final String SENHA = valorOuPadrao("DB_SENHA", "root");

    private ConexaoMySQL() {
    }

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    private static String valorOuPadrao(String variavelDeAmbiente, String padrao) {
        String valor = System.getenv(variavelDeAmbiente);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }
}
