# Diagrama de Casos de Uso — Sistema de Folha de Pagamento

Fonte do diagrama: [`diagrama-caso-de-uso.puml`](diagrama-caso-de-uso.puml) (PlantUML).

## Ator

| Ator | Descrição |
|---|---|
| Responsável pela Folha de Pagamento | Usuário da instituição que cadastra os profissionais, lança aditivos/descontos e dispara a geração da folha. É o único ator do sistema, conforme o README. |

## Casos de uso

| # | Caso de uso | Descrição |
|---|---|---|
| UC1 | Gerenciar Funcionários | Cadastrar, consultar, alterar e excluir profissionais, com dados pessoais e dados de remuneração. |
| UC1A | Cadastrar Professor | Especialização de UC1: informa quantidade de aulas e valor da aula. |
| UC1B | Cadastrar Coordenador | Especialização de UC1: informa valor-base, percentual e quantidade de alunos. |
| UC1C | Cadastrar Bibliotecário | Especialização de UC1: informa o valor-base definido pela instituição. |
| UC1V | Validar Dados do Funcionário | `<<include>>` de UC1 — regras da camada de back (ex.: idade mínima de 18 anos já implementada em `Funcionario.setIdade`). |
| UC2 | Gerenciar Aditivos | Cadastro dos acréscimos do mês. |
| UC3 | Gerenciar Descontos | Cadastro dos descontos do mês. |
| UC4 | Associar Aditivo/Desconto ao Funcionário | `<<include>>` de UC2 e UC3 — todo lançamento pertence a um profissional. |
| UC5 | Gerar Folha de Pagamento | Processa o mês fechado, percorrendo todos os profissionais cadastrados. |
| UC6 | Calcular Remuneração Bruta | `<<include>>` de UC5 — aplica a regra de cálculo de cada tipo de profissional. |
| UC7 | Aplicar Aditivos e Descontos | `<<include>>` de UC8 — soma os acréscimos e subtrai os descontos do período. |
| UC8 | Gerar Holerite do Funcionário | `<<include>>` de UC5 — um holerite por profissional, com bruto, líquido, aditivos e descontos. |
| UC9 | Consultar Holerite | Consulta individual; `<<extend>>` de UC5, pois só existe após a folha gerada. |
| UC10 | Consultar Folha de Pagamento | Consulta consolidada do mês processado. |

## Regras de cálculo (UC6)

- **Professor:** `quantidade de aulas × valor da aula`
- **Coordenador:** `valor-base + (percentual × quantidade de alunos)`
- **Bibliotecário:** `valor-base` fixo

## Fluxo principal

1. O responsável cadastra os profissionais (UC1 / UC1A–UC1C), validados em UC1V.
2. Lança aditivos (UC2) e descontos (UC3), sempre associados a um profissional (UC4).
3. Dispara a geração da folha (UC5): para cada profissional o sistema calcula o bruto (UC6), gera o holerite (UC8) aplicando aditivos e descontos (UC7) e apura o líquido.
4. Consulta os holerites (UC9) e a folha consolidada (UC10).

## Como gerar a imagem

```bash
# com o PlantUML instalado (brew install plantuml)
plantuml -tpng docs/diagrama-caso-de-uso.puml
plantuml -tsvg docs/diagrama-caso-de-uso.puml
```

Alternativas sem instalar nada: colar o conteúdo do `.puml` em https://www.plantuml.com/plantuml ou usar o plugin **PlantUML Integration** do IntelliJ IDEA (visualiza o arquivo direto na IDE).
