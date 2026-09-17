package br.edu.imepac;

import br.edu.imepac.entidades.Funcionario;

public class Main {

    public static void main(String[] args) {
        Funcionario func = new Funcionario();

        func.setId(1);
        func.setNome("joao");
        func.setEmail("joao@email.com");
        func.setIdade(13);

        System.out.println("Id: " + func.getId());
        System.out.println("Nome: " + func.getNome());
        System.out.println("Email: " + func.getEmail());
        System.out.println("Idade: " + func.getIdade());
    }

}
