package br.edu.imepac.entidades;

/**
 * Classe abstrata: nunca é instanciada diretamente, só através de
 * Professor, Coordenador ou Bibliotecario (ver diagrama de classes).
 */
public abstract class Funcionario {
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
        if(idade < 0){
            throw new IllegalArgumentException("A idade não pode ser negativa");
        }
        if(idade < 18){
            throw new IllegalArgumentException("O profissional deve ter no mínimo 18 anos");
        }
        this.idade = idade;
    }

    /**
     * Cada tipo de funcionário calcula sua remuneração de um jeito
     * diferente (polimorfismo) — ver Professor, Coordenador e
     * Bibliotecario.
     */
    public abstract double calcularRemuneracao();
}
