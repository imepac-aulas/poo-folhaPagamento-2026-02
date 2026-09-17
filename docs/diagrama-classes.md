# Diagrama de Classes — Domínio do Sistema de Folha de Pagamento

Fonte do diagrama: [`diagrama-classes.puml`](diagrama-classes.puml) (PlantUML).

## Convenções adotadas

- **`id: int`** em toda classe — identificador único de cada entidade.
- **`get()` / `set()` genéricos** — representam, para cada classe, o conjunto de getters e setters de seus atributos (não foram listados um a um).
- **`Funcionario` é abstrata** — nunca é instanciada diretamente; só existe como `Professor`, `Coordenador` ou `Bibliotecario`. `calcularRemuneracao(): double` é abstrato nela e sobrescrito em cada subtipo (polimorfismo), atendendo ao requisito do README de que *"cada tipo de funcionário possui uma forma específica de cálculo de sua remuneração"*.
- **`Lancamento` é abstrata** — superclasse comum de `Aditivo` e `Desconto`. Concentra os dados que os dois compartilham (`descricao`, `valor`, `competencia`, `funcionario`) e declara `aplicar(valor: double): double` como método abstrato, sobrescrito em cada subtipo: `Aditivo` soma seu valor, `Desconto` subtrai. É assim que o `Holerite` apura o valor líquido sem precisar de `if/instanceof` — outra aplicação de polimorfismo, no mesmo padrão usado em `Funcionario`.
- **`Competencia`** é um objeto de valor que representa o período (mês/ano) ao qual `FolhaPagamento`, `Holerite` e `Lancamento` pertencem.

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
| `Holerite` | `id`, `competencia`, `valorBruto`, `valorLiquido` | Gerado um por funcionário a cada folha. |
| `FolhaPagamento` | `id`, `competencia`, `dataGeracao` | Processo mensal que agrupa os holerites de uma competência. |

## Relacionamentos, multiplicidade e navegação

| Relação | Tipo UML | Multiplicidade | Navegação |
|---|---|---|---|
| `Funcionario` ⟵ `Professor`/`Coordenador`/`Bibliotecario` | Generalização (herança) | — | — |
| `Lancamento` ⟵ `Aditivo`/`Desconto` | Generalização (herança) | — | — |
| `FolhaPagamento` *→ `Holerite` | Composição | 1 → 1..* | Unidirecional (holerite não existe sem a folha que o gerou) |
| `Holerite` → `Funcionario` | Associação | 0..* → 1 | Unidirecional (cada holerite se refere a exatamente um funcionário) |
| `Lancamento` → `Funcionario` | Associação | 0..* → 1 | Unidirecional (aditivo/desconto sabe a quem pertence) |
| `Holerite` o→ `Lancamento` | Agregação | 1 → 0..* | Unidirecional (o holerite reúne os lançamentos aplicados naquele mês; a coleção pode conter tanto `Aditivo` quanto `Desconto`, graças ao polimorfismo) |
| `FolhaPagamento` *→ `Competencia` | Composição | 1 → 1 | Unidirecional |
| `Holerite` *→ `Competencia` | Composição | 1 → 1 | Unidirecional |
| `Lancamento` *→ `Competencia` | Composição | 1 → 1 | Unidirecional |

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

### Por que `Competencia` é composição (e não uma entidade compartilhada)?

`Competencia` foi modelada como objeto de valor: cada `FolhaPagamento`, `Holerite` e `Lancamento` possui a sua própria instância (por isso o losango cheio, multiplicidade 1→1), em vez de todos apontarem para uma única linha compartilhada numa tabela de competências. Isso simplifica o modelo — se no futuro for necessário evitar duplicidade (ex.: impedir duas folhas para a mesma competência), essa regra fica a cargo da camada de serviço, não da modelagem de classes.

### Por que um `Lancamento` sabe sua própria competência?

O README descreve que aditivos e descontos são cadastrados **durante o mês**, informando a competência à qual pertencem — antes de qualquer folha existir. É essa informação que permite ao sistema, na hora de gerar a folha de uma competência, filtrar exatamente os lançamentos daquele período e não misturar lançamentos de meses diferentes.

## Como gerar a imagem

```bash
plantuml -tpng docs/diagrama-classes.puml
plantuml -tsvg docs/diagrama-classes.puml
```
