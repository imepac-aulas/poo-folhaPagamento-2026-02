package br.edu.imepac.entidades;

/**
 * Classe abstrata comum a Aditivo e Desconto. Aponta para o Funcionario
 * ao qual pertence (evita associação bidirecional em Funcionario) e para
 * a Competencia à qual se refere.
 */
public abstract class Lancamento {
    private int id;
    private String descricao;
    private double valor;
    private Competencia competencia;
    private Funcionario funcionario;
    private Holerite holerite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Holerite getHolerite() {
        return holerite;
    }

    /**
     * Setter "simples", usado internamente por Holerite.adicionarLancamento
     * para manter a agregação sincronizada. Começa nulo — só é
     * preenchido quando a folha da competência é gerada e este
     * lançamento é aplicado a um Holerite específico.
     */
    public void setHolerite(Holerite holerite) {
        this.holerite = holerite;
    }

    /**
     * Aditivo soma o próprio valor, Desconto subtrai (polimorfismo) —
     * é assim que o Holerite apura o valor líquido sem testar o tipo.
     */
    public abstract double aplicar(double valor);
}
