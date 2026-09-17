package br.edu.imepac.front;

import java.util.Scanner;

/**
 * Ações de folha de pagamento (UC5, UC9 e UC10 do diagrama de caso de
 * uso). São ações diretas — não têm submenu próprio, por isso são
 * chamadas direto pelo MenuPrincipal.
 */
public class MenuFolhaPagamento {

    private final Scanner scanner;

    public MenuFolhaPagamento(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gerarFolhaPagamento() {
        System.out.println();
        System.out.println("--- Gerar Folha de Pagamento ---");

        int mes = LeitorConsole.lerInteiro(scanner, "Mês da competência (1-12)");
        int ano = LeitorConsole.lerInteiro(scanner, "Ano da competência");

        System.out.printf("%nCompetência informada: %02d/%d%n", mes, ano);

        throw new FuncionalidadeNaoImplementadaException(
                "Gerar Folha de Pagamento (processamento via FolhaPagamentoService/DAO)");
    }

    public void consultarHolerite() {
        System.out.println();
        System.out.println("--- Consultar Holerite ---");

        int idFuncionario = LeitorConsole.lerInteiro(scanner, "Id do funcionário");
        int mes = LeitorConsole.lerInteiro(scanner, "Mês da competência (1-12)");
        int ano = LeitorConsole.lerInteiro(scanner, "Ano da competência");

        System.out.printf("%nConsulta: funcionário #%d, competência %02d/%d%n", idFuncionario, mes, ano);

        throw new FuncionalidadeNaoImplementadaException(
                "Consultar Holerite (consulta via HoleriteService/DAO)");
    }

    public void consultarFolhaPagamento() {
        System.out.println();
        System.out.println("--- Consultar Folha de Pagamento ---");

        int mes = LeitorConsole.lerInteiro(scanner, "Mês da competência (1-12)");
        int ano = LeitorConsole.lerInteiro(scanner, "Ano da competência");

        System.out.printf("%nConsulta: competência %02d/%d%n", mes, ano);

        throw new FuncionalidadeNaoImplementadaException(
                "Consultar Folha de Pagamento (consulta via FolhaPagamentoService/DAO)");
    }
}
