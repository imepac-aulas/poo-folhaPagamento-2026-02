package br.edu.imepac.front;

import br.edu.imepac.entidades.Desconto;

import java.util.Scanner;

/**
 * Tela "Gerenciar Descontos" (UC3 do diagrama de caso de uso).
 *
 * O Desconto em si já é construído e validado de verdade. O que falta é
 * a persistência: {@code LancamentoService.cadastrar(...)} depende de
 * resolver o Funcionario informado (via FuncionarioDAO) e a Competencia
 * do mês/ano (via CompetenciaService, que cria uma nova se for a
 * primeira vez naquele período) — ambos pendentes de um DAO concreto.
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

        Desconto desconto = new Desconto();
        desconto.setDescricao(descricao);
        desconto.setValor(valor);

        System.out.printf("%nDados válidos: funcionário #%d, \"%s\", R$ %.2f, competência %02d/%d%n",
                idFuncionario, desconto.getDescricao(), desconto.getValor(), mes, ano);
        System.out.printf("Exemplo de aplicação sobre um bruto de R$ 1.000,00: R$ %.2f%n",
                desconto.aplicar(1000));

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Desconto (persistência via LancamentoService — precisa localizar o "
                        + "Funcionario #" + idFuncionario + " e resolver/criar a Competencia "
                        + String.format("%02d/%d", mes, ano) + ", ambos pendentes de DAO)");
    }

    private void listarDescontos() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Descontos (consulta via LancamentoService/LancamentoDAO)");
    }
}
