# Implementação de DAO com MySQL

Implementação concreta das interfaces de `br.edu.imepac.dao` usando JDBC puro (sem framework/ORM), em `br.edu.imepac.dao.mysql`.

## 1. Subir o banco

```bash
docker compose up -d
```

O `docker-compose.yml` sobe um MySQL 8 e, **na primeira inicialização** (quando o volume de dados ainda está vazio), executa automaticamente todo `.sql` dentro de `sql/` — é assim que as tabelas (`competencia`, `funcionario`, `folha_pagamento`, `holerite`, `lancamento`) já sobem criadas junto com o banco, sem nenhum passo manual extra. Esse é o mecanismo padrão da imagem oficial do MySQL (pasta `/docker-entrypoint-initdb.d/`), não é nada customizado.

Credenciais padrão (ajustáveis no `docker-compose.yml`): usuário `root`, senha `root`, banco `folha_pagamento`, porta `3306`.

Pra conferir que as tabelas subiram:

```bash
docker exec folha-pagamento-mysql mysql -uroot -proot folha_pagamento -e "SHOW TABLES;"
```

Se precisar recriar o schema do zero (ex.: mudou o `schema.sql`), como o script só roda na *primeira* inicialização, é preciso derrubar o volume:

```bash
docker compose down -v
docker compose up -d
```

## 2. Driver JDBC

O projeto não usa Maven/Gradle (é um módulo IntelliJ simples, com `src/` compilado direto), então o driver do MySQL (`mysql-connector-j`) precisa ser baixado manualmente e adicionado ao classpath — ele **não é um framework**, é só o driver JDBC, sem o qual nenhuma aplicação Java consegue abrir uma conexão com MySQL.

```bash
mkdir -p lib
curl -L -o lib/mysql-connector-j.jar \
  https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar
```

No IntelliJ: `File > Project Structure > Libraries > +` e aponte para `lib/mysql-connector-j.jar`.

Para compilar/rodar via linha de comando:

```bash
javac -cp lib/mysql-connector-j.jar -d out $(find src -name "*.java")
java -cp out:lib/mysql-connector-j.jar br.edu.imepac.Main
```

## 3. Configuração de conexão

`br.edu.imepac.dao.mysql.ConexaoMySQL` já vem com os valores padrão do `docker-compose.yml` (`jdbc:mysql://localhost:3306/folha_pagamento`, usuário `root`, senha `root`). Para apontar pra outro banco, sem recompilar, defina as variáveis de ambiente:

```bash
export DB_URL="jdbc:mysql://localhost:3306/folha_pagamento"
export DB_USUARIO="root"
export DB_SENHA="root"
```

## 4. Como os DAOs foram montados

Cada implementação segue a interface já definida em `br.edu.imepac.dao` (nenhum método novo, nenhuma dependência de framework):

- `CompetenciaDAOMySQL`, `FuncionarioDAOMySQL` — não dependem de outro DAO.
- `FolhaPagamentoDAOMySQL(CompetenciaDAO)` — resolve a FK `competencia_id`.
- `HoleriteDAOMySQL(FuncionarioDAO, FolhaPagamentoDAO)` — resolve `funcionario_id` e `folha_pagamento_id`.
- `LancamentoDAOMySQL(FuncionarioDAO, CompetenciaDAO, HoleriteDAO)` — resolve `funcionario_id`, `competencia_id` e `holerite_id` (nula até o lançamento ser aplicado a um holerite).

`Professor`/`Coordenador`/`Bibliotecario` são gravados numa única tabela `funcionario`, discriminada pela coluna `tipo` (as colunas específicas de cada subtipo ficam `NULL` para os demais) — o mesmo padrão é usado em `lancamento` pra `Aditivo`/`Desconto`. É o DAO que decide, na leitura, qual subclasse reconstruir; não há mapeamento automático porque não há ORM.

## 5. Validação

Testado manualmente de ponta a ponta contra o container real (cadastro de Professor e Bibliotecário, lançamento de aditivo/desconto, geração de folha, consulta de holerite, bloqueio de folha duplicada) — bruto/líquido calculados corretamente e conferidos direto nas tabelas via `SELECT`. O container de teste foi derrubado (`docker compose down -v`) depois da validação; o banco começa vazio.
