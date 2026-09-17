package br.edu.imepac.front;

import br.edu.imepac.entidades.Funcionario;

import java.util.Scanner;

/**
 * Tela "Gerenciar Funcionários" (UC1 do diagrama de caso de uso).
 *
 * Os dados comuns (nome, email, idade) já são validados de verdade,
 * usando {@link Funcionario#setIdade(int)}. Os dados específicos de cada
 * tipo são coletados normalmente, mas o cadastro em si (persistência)
 * ainda depende da camada de serviço/DAO, então é sinalizado como
 * pendente no momento em que seria salvo.
 */
public class MenuFuncionario {

    private final Scanner scanner;

    public MenuFuncionario(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("--- Gerenciar Funcionários ---");
            System.out.println("1 - Cadastrar Professor");
            System.out.println("2 - Cadastrar Coordenador");
            System.out.println("3 - Cadastrar Bibliotecário");
            System.out.println("4 - Listar Funcionários");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = LeitorConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1 -> cadastrarProfessor();
                    case 2 -> cadastrarCoordenador();
                    case 3 -> cadastrarBibliotecario();
                    case 4 -> listarFuncionarios();
                    case 0 -> voltar = true;
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (FuncionalidadeNaoImplementadaException e) {
                System.out.println();
                System.out.println("⚠ " + e.getMessage());
            }
        }
    }

    private void cadastrarProfessor() {
        System.out.println();
        System.out.println("--- Cadastrar Professor ---");

        Funcionario funcionario = lerDadosComuns();
        int quantidadeAulas = LeitorConsole.lerInteiro(scanner, "Quantidade de aulas");
        double valorAula = LeitorConsole.lerDecimal(scanner, "Valor da aula (R$)");

        System.out.printf("%nDados válidos: %s, %d aula(s) x R$ %.2f%n",
                funcionario.getNome(), quantidadeAulas, valorAula);

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Professor (persistência via FuncionarioService/DAO)");
    }

    private void cadastrarCoordenador() {
        System.out.println();
        System.out.println("--- Cadastrar Coordenador ---");

        Funcionario funcionario = lerDadosComuns();
        double valorBase = LeitorConsole.lerDecimal(scanner, "Valor-base (R$)");
        double percentual = LeitorConsole.lerDecimal(scanner, "Percentual (ex.: 2 para 2%)");
        int quantidadeAlunos = LeitorConsole.lerInteiro(scanner, "Quantidade de alunos");

        System.out.printf("%nDados válidos: %s, base R$ %.2f + %.2f%% x %d aluno(s)%n",
                funcionario.getNome(), valorBase, percentual, quantidadeAlunos);

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Coordenador (persistência via FuncionarioService/DAO)");
    }

    private void cadastrarBibliotecario() {
        System.out.println();
        System.out.println("--- Cadastrar Bibliotecário ---");

        Funcionario funcionario = lerDadosComuns();
        double valorBase = LeitorConsole.lerDecimal(scanner, "Valor-base (R$)");

        System.out.printf("%nDados válidos: %s, valor-base R$ %.2f%n",
                funcionario.getNome(), valorBase);

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Bibliotecário (persistência via FuncionarioService/DAO)");
    }

    private void listarFuncionarios() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Funcionários (consulta via FuncionarioService/DAO)");
    }

    /**
     * Coleta e valida os dados comuns a qualquer tipo de funcionário.
     * A idade só é aceita quando passa pela validação real de
     * {@link Funcionario#setIdade(int)} (idade negativa ou menor de 18).
     */
    private Funcionario lerDadosComuns() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(LeitorConsole.lerTexto(scanner, "Nome"));
        funcionario.setEmail(LeitorConsole.lerTexto(scanner, "Email"));

        boolean idadeValida = false;
        while (!idadeValida) {
            int idade = LeitorConsole.lerInteiro(scanner, "Idade");
            try {
                funcionario.setIdade(idade);
                idadeValida = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return funcionario;
    }
}
