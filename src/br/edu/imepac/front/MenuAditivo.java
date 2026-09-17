package br.edu.imepac.front;

import java.util.Scanner;

/**
 * Tela "Gerenciar Aditivos" (UC2 do diagrama de caso de uso).
 *
 * A classe Aditivo ainda não existe no código (só no diagrama de
 * classes), então os dados são coletados normalmente e a persistência é
 * sinalizada como pendente da camada de serviço/DAO.
 */
public class MenuAditivo {

    private final Scanner scanner;

    public MenuAditivo(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("--- Gerenciar Aditivos ---");
            System.out.println("1 - Cadastrar Aditivo");
            System.out.println("2 - Listar Aditivos");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = LeitorConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1 -> cadastrarAditivo();
                    case 2 -> listarAditivos();
                    case 0 -> voltar = true;
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (FuncionalidadeNaoImplementadaException e) {
                System.out.println();
                System.out.println("⚠ " + e.getMessage());
            }
        }
    }

    private void cadastrarAditivo() {
        System.out.println();
        System.out.println("--- Cadastrar Aditivo ---");

        int idFuncionario = LeitorConsole.lerInteiro(scanner, "Id do funcionário");
        String descricao = LeitorConsole.lerTexto(scanner, "Descrição");
        double valor = LeitorConsole.lerDecimal(scanner, "Valor (R$)");
        int mes = LeitorConsole.lerInteiro(scanner, "Mês da competência (1-12)");
        int ano = LeitorConsole.lerInteiro(scanner, "Ano da competência");

        System.out.printf("%nDados coletados: funcionário #%d, \"%s\", R$ %.2f, competência %02d/%d%n",
                idFuncionario, descricao, valor, mes, ano);

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Aditivo (persistência via LancamentoService/DAO)");
    }

    private void listarAditivos() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Aditivos (consulta via LancamentoService/DAO)");
    }
}
