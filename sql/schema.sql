-- Sistema de Folha de Pagamento - IMEPAC
-- Schema MySQL, criado a partir do diagrama de classes do domínio.
-- Executado automaticamente pelo MySQL (imagem oficial) na primeira
-- inicialização do container, via /docker-entrypoint-initdb.d/.

-- Ordem das tabelas segue a dependência de chave estrangeira:
-- competencia -> funcionario -> folha_pagamento -> holerite -> lancamento

CREATE TABLE IF NOT EXISTS competencia (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    mes  INT NOT NULL,
    ano  INT NOT NULL,
    CONSTRAINT chk_competencia_mes CHECK (mes BETWEEN 1 AND 12),
    CONSTRAINT uq_competencia_mes_ano UNIQUE (mes, ano)
);

-- Funcionario, Professor, Coordenador e Bibliotecario numa única
-- tabela (herança em tabela única, discriminada por "tipo"), já que
-- não há framework de mapeamento objeto-relacional no projeto. As
-- colunas específicas de cada subtipo ficam NULL para os demais.
CREATE TABLE IF NOT EXISTS funcionario (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    tipo               VARCHAR(20)    NOT NULL,
    nome               VARCHAR(150)   NOT NULL,
    email              VARCHAR(150)   NOT NULL,
    idade              INT            NOT NULL,
    -- específico de Professor
    quantidade_aulas   INT            NULL,
    valor_aula         DECIMAL(10,2)  NULL,
    -- específico de Coordenador e Bibliotecario
    valor_base         DECIMAL(10,2)  NULL,
    -- específico de Coordenador
    percentual         DECIMAL(10,2)  NULL,
    quantidade_alunos  INT            NULL,
    CONSTRAINT chk_funcionario_tipo CHECK (tipo IN ('PROFESSOR', 'COORDENADOR', 'BIBLIOTECARIO')),
    CONSTRAINT chk_funcionario_idade CHECK (idade >= 18)
);

-- Composição: Competencia possui no máximo uma FolhaPagamento
-- (UNIQUE em competencia_id garante isso no próprio banco).
CREATE TABLE IF NOT EXISTS folha_pagamento (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    competencia_id  INT  NOT NULL,
    data_geracao    DATE NOT NULL,
    CONSTRAINT uq_folha_pagamento_competencia UNIQUE (competencia_id),
    CONSTRAINT fk_folha_pagamento_competencia
        FOREIGN KEY (competencia_id) REFERENCES competencia (id)
);

-- Composição: Holerite não existe sem a FolhaPagamento que o originou.
-- UNIQUE (folha_pagamento_id, funcionario_id) garante um único
-- holerite por funcionário em cada folha.
CREATE TABLE IF NOT EXISTS holerite (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    folha_pagamento_id  INT NOT NULL,
    funcionario_id      INT NOT NULL,
    valor_bruto         DECIMAL(10,2) NOT NULL,
    valor_liquido       DECIMAL(10,2) NOT NULL,
    CONSTRAINT uq_holerite_folha_funcionario UNIQUE (folha_pagamento_id, funcionario_id),
    CONSTRAINT fk_holerite_folha_pagamento
        FOREIGN KEY (folha_pagamento_id) REFERENCES folha_pagamento (id),
    CONSTRAINT fk_holerite_funcionario
        FOREIGN KEY (funcionario_id) REFERENCES funcionario (id)
);

-- Aditivo e Desconto numa única tabela (herança em tabela única,
-- discriminada por "tipo"). holerite_id começa NULL (lançamento
-- cadastrado antes de qualquer folha existir) e é preenchido quando a
-- folha da competência é gerada e o lançamento é aplicado a um holerite.
CREATE TABLE IF NOT EXISTS lancamento (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    tipo            VARCHAR(10)    NOT NULL,
    descricao       VARCHAR(200)   NOT NULL,
    valor           DECIMAL(10,2)  NOT NULL,
    funcionario_id  INT NOT NULL,
    competencia_id  INT NOT NULL,
    holerite_id     INT NULL,
    CONSTRAINT chk_lancamento_tipo CHECK (tipo IN ('ADITIVO', 'DESCONTO')),
    CONSTRAINT fk_lancamento_funcionario
        FOREIGN KEY (funcionario_id) REFERENCES funcionario (id),
    CONSTRAINT fk_lancamento_competencia
        FOREIGN KEY (competencia_id) REFERENCES competencia (id),
    CONSTRAINT fk_lancamento_holerite
        FOREIGN KEY (holerite_id) REFERENCES holerite (id)
);
