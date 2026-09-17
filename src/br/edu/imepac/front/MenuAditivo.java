package br.edu.imepac.front;

import br.edu.imepac.entidades.Aditivo;

import java.util.Scanner;

/**
 * Tela "Gerenciar Aditivos" (UC2 do diagrama de caso de uso).
 *
 * O Aditivo em si já é construído e validado de verdade. O que falta é
 * a persistência: {@code LancamentoService.cadastrar(...)} depende de
 * resolver o Funcionario informado (via FuncionarioDAO) e a Competencia
 * do mês/ano (via CompetenciaService, que cria uma nova se for a
 * primeira vez naquele período) — ambos pendentes de um DAO concreto.
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

        Aditivo aditivo = new Aditivo();
        aditivo.setDescricao(descricao);
        aditivo.setValor(valor);

        System.out.printf("%nDados válidos: funcionário #%d, \"%s\", R$ %.2f, competência %02d/%d%n",
                idFuncionario, aditivo.getDescricao(), aditivo.getValor(), mes, ano);
        System.out.printf("Exemplo de aplicação sobre um bruto de R$ 1.000,00: R$ %.2f%n",
                aditivo.aplicar(1000));

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Aditivo (persistência via LancamentoService — precisa localizar o "
                        + "Funcionario #" + idFuncionario + " e resolver/criar a Competencia "
                        + String.format("%02d/%d", mes, ano) + ", ambos pendentes de DAO)");
    }

    private void listarAditivos() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Aditivos (consulta via LancamentoService/LancamentoDAO)");
    }
}
