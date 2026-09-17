package br.edu.imepac.front;

import br.edu.imepac.entidades.Bibliotecario;
import br.edu.imepac.entidades.Coordenador;
import br.edu.imepac.entidades.Funcionario;
import br.edu.imepac.entidades.Professor;

import java.util.Scanner;

/**
 * Tela "Gerenciar Funcionários" (UC1 do diagrama de caso de uso).
 *
 * Os dados de cada funcionário já são validados de verdade (via os
 * setters das entidades reais — nome/email/idade em
 * {@link Funcionario}, e os campos específicos de cada subtipo). O que
 * ainda não existe é a persistência: a camada de serviço já tem
 * {@code FuncionarioService.cadastrar(Funcionario)} pronta, mas ela
 * depende de um {@code FuncionarioDAO} concreto, que ainda não foi
 * implementado — por isso o cadastro é sinalizado como pendente no
 * momento em que seria salvo.
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

        Professor professor = new Professor();
        lerDadosComuns(professor);
        professor.setQuantidadeAulas(LeitorConsole.lerInteiro(scanner, "Quantidade de aulas"));
        professor.setValorAula(LeitorConsole.lerDecimal(scanner, "Valor da aula (R$)"));

        System.out.printf("%nDados válidos: %s, %d aula(s) x R$ %.2f (remuneração bruta: R$ %.2f)%n",
                professor.getNome(), professor.getQuantidadeAulas(), professor.getValorAula(),
                professor.calcularRemuneracao());

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Professor (persistência via FuncionarioService/FuncionarioDAO)");
    }

    private void cadastrarCoordenador() {
        System.out.println();
        System.out.println("--- Cadastrar Coordenador ---");

        Coordenador coordenador = new Coordenador();
        lerDadosComuns(coordenador);
        coordenador.setValorBase(LeitorConsole.lerDecimal(scanner, "Valor-base (R$)"));
        coordenador.setPercentual(LeitorConsole.lerDecimal(scanner, "Percentual (ex.: 2 para 2%)"));
        coordenador.setQuantidadeAlunos(LeitorConsole.lerInteiro(scanner, "Quantidade de alunos"));

        System.out.printf("%nDados válidos: %s, base R$ %.2f + %.2f x %d aluno(s) (remuneração bruta: R$ %.2f)%n",
                coordenador.getNome(), coordenador.getValorBase(), coordenador.getPercentual(),
                coordenador.getQuantidadeAlunos(), coordenador.calcularRemuneracao());

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Coordenador (persistência via FuncionarioService/FuncionarioDAO)");
    }

    private void cadastrarBibliotecario() {
        System.out.println();
        System.out.println("--- Cadastrar Bibliotecário ---");

        Bibliotecario bibliotecario = new Bibliotecario();
        lerDadosComuns(bibliotecario);
        bibliotecario.setValorBase(LeitorConsole.lerDecimal(scanner, "Valor-base (R$)"));

        System.out.printf("%nDados válidos: %s, valor-base R$ %.2f (remuneração bruta: R$ %.2f)%n",
                bibliotecario.getNome(), bibliotecario.getValorBase(), bibliotecario.calcularRemuneracao());

        throw new FuncionalidadeNaoImplementadaException(
                "Cadastrar Bibliotecário (persistência via FuncionarioService/FuncionarioDAO)");
    }

    private void listarFuncionarios() {
        throw new FuncionalidadeNaoImplementadaException(
                "Listar Funcionários (consulta via FuncionarioService/FuncionarioDAO)");
    }

    /**
     * Coleta e valida os dados comuns a qualquer tipo de funcionário,
     * preenchendo o objeto já construído pelo chamador (não dá mais pra
     * instanciar Funcionario diretamente, agora que ele é abstrato). A
     * idade só é aceita quando passa pela validação real de
     * {@link Funcionario#setIdade(int)} (idade negativa ou menor de 18).
     */
    private void lerDadosComuns(Funcionario funcionario) {
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
    }
}
