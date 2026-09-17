package br.edu.imepac.servico;

import br.edu.imepac.dao.CompetenciaDAO;
import br.edu.imepac.dao.FolhaPagamentoDAO;
import br.edu.imepac.dao.FuncionarioDAO;
import br.edu.imepac.dao.HoleriteDAO;
import br.edu.imepac.dao.LancamentoDAO;
import br.edu.imepac.entidades.Competencia;
import br.edu.imepac.entidades.FolhaPagamento;
import br.edu.imepac.entidades.Funcionario;
import br.edu.imepac.entidades.Holerite;
import br.edu.imepac.entidades.Lancamento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Orquestra a geração da folha de pagamento (UC5): para cada
 * funcionário cadastrado, calcula a remuneração bruta (polimorfismo em
 * Funcionario.calcularRemuneracao()), aplica os lançamentos da
 * competência (polimorfismo em Lancamento.aplicar()) e monta o
 * respectivo Holerite.
 */
public class FolhaPagamentoService {

    private final FolhaPagamentoDAO folhaPagamentoDAO;
    private final CompetenciaDAO competenciaDAO;
    private final FuncionarioDAO funcionarioDAO;
    private final LancamentoDAO lancamentoDAO;
    private final HoleriteDAO holeriteDAO;

    public FolhaPagamentoService(FolhaPagamentoDAO folhaPagamentoDAO,
                                  CompetenciaDAO competenciaDAO,
                                  FuncionarioDAO funcionarioDAO,
                                  LancamentoDAO lancamentoDAO,
                                  HoleriteDAO holeriteDAO) {
        this.folhaPagamentoDAO = folhaPagamentoDAO;
        this.competenciaDAO = competenciaDAO;
        this.funcionarioDAO = funcionarioDAO;
        this.lancamentoDAO = lancamentoDAO;
        this.holeriteDAO = holeriteDAO;
    }

    public FolhaPagamento gerar(int mes, int ano) {
        Competencia competencia = competenciaDAO.buscarPorMesEAno(mes, ano)
                .orElseThrow(() -> new IllegalStateException(
                        "Não há competência cadastrada para " + mes + "/" + ano
                                + " (cadastre ao menos um aditivo/desconto antes)."));

        if (folhaPagamentoDAO.buscarPorCompetencia(competencia).isPresent()) {
            throw new IllegalStateException(
                    "Já existe uma folha de pagamento gerada para esta competência.");
        }

        FolhaPagamento folha = new FolhaPagamento();
        folha.setDataGeracao(LocalDate.now());
        competencia.definirFolhaPagamento(folha);

        List<Lancamento> lancamentosDoPeriodo = lancamentoDAO.buscarPorCompetencia(competencia);

        for (Funcionario funcionario : funcionarioDAO.listarTodos()) {
            Holerite holerite = montarHolerite(funcionario, lancamentosDoPeriodo);
            folha.adicionarHolerite(holerite);
        }

        FolhaPagamento folhaSalva = folhaPagamentoDAO.salvar(folha);
        folha.getHolerites().forEach(holeriteDAO::salvar);
        return folhaSalva;
    }

    private Holerite montarHolerite(Funcionario funcionario, List<Lancamento> lancamentosDoPeriodo) {
        double valorBruto = funcionario.calcularRemuneracao();
        double valorLiquido = valorBruto;

        Holerite holerite = new Holerite();
        holerite.setFuncionario(funcionario);

        for (Lancamento lancamento : lancamentosDoPeriodo) {
            if (lancamento.getFuncionario().getId() == funcionario.getId()) {
                valorLiquido = lancamento.aplicar(valorLiquido);
                holerite.adicionarLancamento(lancamento);
            }
        }

        holerite.setValorBruto(valorBruto);
        holerite.setValorLiquido(valorLiquido);
        return holerite;
    }

    public Optional<FolhaPagamento> consultarPorCompetencia(int mes, int ano) {
        return competenciaDAO.buscarPorMesEAno(mes, ano)
                .flatMap(folhaPagamentoDAO::buscarPorCompetencia);
    }
}
