package br.edu.imepac.front;

import java.util.Scanner;

/**
 * Utilitário de leitura de dados via console, compartilhado pelas telas
 * da camada de front. Concentra a repetição de parse/validação de
 * entrada (número inválido, texto vazio) num único lugar.
 */
public final class LeitorConsole {

    private LeitorConsole() {
    }

    public static int lerOpcao(Scanner scanner) {
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String lerTexto(Scanner scanner, String rotulo) {
        System.out.print(rotulo + ": ");
        return scanner.nextLine().trim();
    }

    public static int lerInteiro(Scanner scanner, String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    public static double lerDecimal(Scanner scanner, String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String entrada = scanner.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número (ex.: 1500.00).");
            }
        }
    }
}
