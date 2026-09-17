package br.edu.imepac.front;

import java.util.Scanner;

/**
 * Tela "Gerenciar Descontos" (UC3 do diagrama de caso de uso).
 *
 * A classe Desconto ainda não existe no código (só no diagrama de
 * classes), então os dados são coletados normalmente e a persistência é
 * sinalizada como pendente da camada de serviço/DAO.
 */
public class MenuDesconto {

    private final Scanner scanner;

    public MenuDesconto(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("--- Gerenciar Descontos ---");
            System.out.println("1 - Cadastrar Desconto");
            System.out.println("2 - Listar Descontos");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = LeitorConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1 -> cadastrarDesconto();
                    case 2 -> listarDescontos();
                    case 0 -> voltar = true;
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (FuncionalidadeNaoImplementadaException e) {
                System.out.println();
                System.out.println("⚠ " + e.getMessage());
            }
        }
    }

    private void cadastrarDesconto() {
        System.out.println();
        System.out.println("--- Cadastrar Desconto ---");

        int idFuncionario = LeitorConsole.lerInteiro(scanner, "Id do funcionário");
        String descricao = LeitorConsole.lerTexto(scanner, "Descrição");
        double valor = LeitorConsole.lerDecimal(scanner, "Valor (R$)");
        int mes = LeitorConsole.lerInteiro(scanner, "Mês da competência (1-12)");
        int ano = LeitorConsole.lerInteiro(scanner, "Ano da competência");

        System.out.printf("%nDados coletados: funcionário #%d, \"%s\", R$ %.2f, competência %02d/%d%n",
                idFuncionario, descricao, valor, mes, ano);

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Desconto (persistência via LancamentoService/DAO)");
    }

    private void listarDescontos() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Descontos (consulta via LancamentoService/DAO)");
    }
}
