# Diagrama de Classes — Domínio do Sistema de Folha de Pagamento

Fonte do diagrama: [`diagrama-classes.puml`](diagrama-classes.puml) (PlantUML).

## Convenções adotadas

- **`id: int`** em toda classe — identificador único de cada entidade.
- **`get()` / `set()` genéricos** — representam, para cada classe, o conjunto de getters e setters de seus atributos (não foram listados um a um).
- **`Funcionario` é abstrata** — nunca é instanciada diretamente; só existe como `Professor`, `Coordenador` ou `Bibliotecario`. `calcularRemuneracao(): double` é abstrato nela e sobrescrito em cada subtipo (polimorfismo), atendendo ao requisito do README de que *"cada tipo de funcionário possui uma forma específica de cálculo de sua remuneração"*.
- **`Lancamento` é abstrata** — superclasse comum de `Aditivo` e `Desconto`. Concentra os dados que os dois compartilham (`descricao`, `valor`, `competencia`, `funcionario`) e declara `aplicar(valor: double): double` como método abstrato, sobrescrito em cada subtipo: `Aditivo` soma seu valor, `Desconto` subtrai. É assim que o `Holerite` apura o valor líquido sem precisar de `if/instanceof` — outra aplicação de polimorfismo, no mesmo padrão usado em `Funcionario`.
- **`Competencia`** representa o período (mês/ano) e é a **raiz de composição** do modelo: ela possui a `FolhaPagamento` daquele mês (se já tiver sido gerada) e todos os `Lancamento` lançados naquele período. `Holerite` não referencia `Competencia` diretamente — o período dele é obtido navegando por `FolhaPagamento` (que o possui).

## Classes e atributos

| Classe | Atributos próprios | Observação |
|---|---|---|
| `Competencia` | `id`, `mes`, `ano` | Identifica um período (ex.: 09/2026). |
| `Funcionario` (abstrata) | `id`, `nome`, `email`, `idade` | Superclasse comum aos três tipos de profissional. |
| `Professor` | `quantidadeAulas`, `valorAula` | Remuneração = aulas × valor da aula. |
| `Coordenador` | `valorBase`, `percentual`, `quantidadeAlunos` | Remuneração = base + (percentual × alunos). |
| `Bibliotecario` | `valorBase` | Remuneração = valor-base fixo. |
| `Lancamento` (abstrata) | `id`, `descricao`, `valor`, `competencia`, `funcionario` | Superclasse comum a `Aditivo` e `Desconto`. |
| `Aditivo` | — (herda de `Lancamento`) | `aplicar(valor)` retorna `valor + this.valor`. |
| `Desconto` | — (herda de `Lancamento`) | `aplicar(valor)` retorna `valor - this.valor`. |
| `Holerite` | `id`, `folhaPagamento`, `valorBruto`, `valorLiquido` | Gerado um por funcionário a cada folha; obtém a competência navegando por `folhaPagamento`. |
| `FolhaPagamento` | `id`, `competencia`, `dataGeracao` | Processo mensal que agrupa os holerites de uma competência. |

## Relacionamentos, multiplicidade e navegação

| Relação | Tipo UML | Multiplicidade | Navegação |
|---|---|---|---|
| `Funcionario` ⟵ `Professor`/`Coordenador`/`Bibliotecario` | Generalização (herança) | — | — |
| `Lancamento` ⟵ `Aditivo`/`Desconto` | Generalização (herança) | — | — |
| `Competencia` *→ `FolhaPagamento` | Composição | 1 → 0..1 | Unidirecional (a folha daquela competência, se já tiver sido gerada) |
| `Competencia` *→ `Lancamento` | Composição | 1 → 0..* | Unidirecional (todos os lançamentos daquele período, de todos os funcionários) |
| `FolhaPagamento` *↔ `Holerite` | Composição | 1 → 1..* | Bidirecional (holerite não existe sem a folha que o gerou; guarda referência de volta para poder navegar até a `Competencia`) |
| `Holerite` → `Funcionario` | Associação | 0..* → 1 | Unidirecional (cada holerite se refere a exatamente um funcionário) |
| `Lancamento` → `Funcionario` | Associação | 0..* → 1 | Unidirecional (aditivo/desconto sabe a quem pertence) |
| `Holerite` o→ `Lancamento` | Agregação | 1 → 0..* | Unidirecional (o holerite reúne os lançamentos aplicados naquele mês; a coleção pode conter tanto `Aditivo` quanto `Desconto`, graças ao polimorfismo) |

### Por que `Lancamento` como superclasse, e não uma única classe com um campo "tipo"?

Consideramos as duas opções e escolhemos a herança porque:

- Mantém `Aditivo` e `Desconto` como classes de fato — exatamente como o README as lista (`ADITIVO`, `DESCONTO`) — em vez de colapsá-las numa única classe genérica com um enum.
- Reaproveita o mesmo padrão já usado em `Funcionario` (abstrata + subtipos polimórficos), dando consistência ao modelo.
- Permite que o `Holerite` calcule o valor líquido chamando `aplicar(valor)` em cada `Lancamento` da sua coleção, sem testar o tipo — o polimorfismo decide se soma ou subtrai.

### Por que o "filho" aponta para o "dono" (`Lancamento`/`Holerite` → `Funcionario`), e não o contrário?

> **Registros transacionais apontam para a entidade a que pertencem; entidades "mestre" não guardam listas de seus registros transacionais.**

- Reflete como a FK ficaria no banco (`lancamento.funcionario_id`, `holerite.funcionario_id`).
- Evita o risco de uma associação bidirecional ficar dessincronizada (esquecer de atualizar os dois lados ao criar/remover um registro).
- Quem precisa listar "os lançamentos do funcionário X" faz isso via a camada de serviço/DAO, não navegando um atributo em memória.
- **Exceção deliberada:** `FolhaPagamento` → `Holerite` (composição) e `Holerite` → `Lancamento` (agregação) continuam com o agregador guardando a coleção, porque reunir esses itens **é a própria razão de existir** dessas classes.

### Por que `Competencia` é a raiz de composição do modelo?

Consideramos três desenhos possíveis para `Competencia` (ver histórico de discussão): objeto de valor duplicado em cada classe, entidade compartilhada por associação, e raiz de composição. Ficamos com a terceira opção:

- **Resolve "quais lançamentos são desta competência?" por construção.** Os lançamentos de um período não precisam ser filtrados/comparados (por valor ou por referência) — eles **são** a coleção `competencia.getLancamentos()`. O mesmo vale para a folha: `competencia.getFolhaPagamento()`.
- **Preserva a independência do `Lancamento` frente à `FolhaPagamento`.** Como a multiplicidade `Competencia → FolhaPagamento` é `0..1`, uma competência pode ter lançamentos cadastrados **antes** de qualquer folha existir — exatamente o fluxo descrito no README (aditivos/descontos lançados durante o mês, a folha gerada depois).
- **`Holerite` não precisa de referência própria a `Competencia`.** Como ele só existe dentro de uma `FolhaPagamento` (que por sua vez só existe dentro de uma `Competencia`), o período de um holerite é obtido por navegação (`Holerite → FolhaPagamento → Competencia`, transitiva), sem precisar duplicar o dado nem manter mais uma referência no diagrama.

**Efeito de exclusão em cascata:** como `Competencia` compõe `FolhaPagamento` e `Lancamento`, apagar uma `Competencia` apaga em cascata a folha daquele mês (e, por consequência, todos os seus holerites, já que `FolhaPagamento` compõe `Holerite`) e todos os lançamentos do período. Isso é semanticamente correto para o conceito ("apagar o período apaga tudo que pertence a ele"), mas é uma operação destrutiva demais para liberar sem controle num sistema de folha de pagamento real. Por isso, fica como **regra de negócio na camada de serviço** (fora do escopo deste diagrama de classes): *não permitir excluir uma `Competencia` que já possua uma `FolhaPagamento` gerada.*

## Como gerar a imagem

```bash
plantuml -tpng docs/diagrama-classes.puml
plantuml -tsvg docs/diagrama-classes.puml
```
