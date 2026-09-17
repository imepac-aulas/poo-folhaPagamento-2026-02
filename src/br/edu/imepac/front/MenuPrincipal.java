package br.edu.imepac.front;

import java.util.Scanner;

/**
 * Tela inicial do sistema. Ponto de entrada da camada de front,
 * despachando para as telas de cada funcionalidade do diagrama de caso
 * de uso.
 */
public class MenuPrincipal {

    private final Scanner scanner;
    private final MenuFuncionario menuFuncionario;
    private final MenuAditivo menuAditivo;
    private final MenuDesconto menuDesconto;
    private final MenuFolhaPagamento menuFolhaPagamento;

    public MenuPrincipal(Scanner scanner) {
        this.scanner = scanner;
        this.menuFuncionario = new MenuFuncionario(scanner);
        this.menuAditivo = new MenuAditivo(scanner);
        this.menuDesconto = new MenuDesconto(scanner);
        this.menuFolhaPagamento = new MenuFolhaPagamento(scanner);
    }

    public void exibir() {
        boolean sair = false;
        while (!sair) {
            System.out.println();
            System.out.println("==============================================");
            System.out.println("   SISTEMA DE FOLHA DE PAGAMENTO - IMEPAC");
            System.out.println("==============================================");
            System.out.println("1 - Gerenciar Funcionários");
            System.out.println("2 - Gerenciar Aditivos");
            System.out.println("3 - Gerenciar Descontos");
            System.out.println("4 - Gerar Folha de Pagamento");
            System.out.println("5 - Consultar Holerite");
            System.out.println("6 - Consultar Folha de Pagamento");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = LeitorConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1 -> menuFuncionario.exibir();
                    case 2 -> menuAditivo.exibir();
                    case 3 -> menuDesconto.exibir();
                    case 4 -> menuFolhaPagamento.gerarFolhaPagamento();
                    case 5 -> menuFolhaPagamento.consultarHolerite();
                    case 6 -> menuFolhaPagamento.consultarFolhaPagamento();
                    case 0 -> sair = true;
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (FuncionalidadeNaoImplementadaException e) {
                System.out.println();
                System.out.println("⚠ " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("Encerrando o sistema. Até logo!");
    }
}
