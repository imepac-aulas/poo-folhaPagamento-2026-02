package br.edu.imepac;

import br.edu.imepac.front.MenuPrincipal;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new MenuPrincipal(scanner).exibir();
        }
    }

}
