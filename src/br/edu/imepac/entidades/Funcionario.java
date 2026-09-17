package br.edu.imepac.entidades;

public class Funcionario {
    private int id;
    private String nome;
    private String email;
    private int idade;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if(idade < 18){
            throw new IllegalArgumentException("A idade deve ser maior que 18");
        }
        this.idade = idade;
    }
}
