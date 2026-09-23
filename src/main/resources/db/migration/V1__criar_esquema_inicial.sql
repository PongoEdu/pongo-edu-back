-- PongoEdu - esquema inicial do modulo de gestao de laboratorio
-- Gerado a partir do modelo de dominio (RF01-RF19) do documento do projeto.

-- =========================================================
-- Usuarios e acesso (RF01, RF02, RF19)
-- =========================================================
CREATE TABLE usuario (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    perfil          VARCHAR(30)  NOT NULL CHECK (perfil IN ('PROFESSOR', 'AUXILIAR_LABORATORIO')),
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP    NOT NULL DEFAULT now(),
    atualizado_em   TIMESTAMP
);

-- =========================================================
-- Estoque: categorias, lotes, produtos (RF03, RF04, RF05)
-- =========================================================
CREATE TABLE categoria (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(100) NOT NULL UNIQUE,
    descricao           VARCHAR(255),
    permite_emprestimo  BOOLEAN      NOT NULL DEFAULT FALSE,
    ativo               BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em           TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE lote (
    id                BIGSERIAL PRIMARY KEY,
    codigo            VARCHAR(60) NOT NULL UNIQUE,
    fornecedor        VARCHAR(150),
    data_fabricacao   DATE,
    data_validade     DATE,
    observacao        VARCHAR(255),
    criado_em         TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE TABLE laboratorio (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(120) NOT NULL UNIQUE,
    descricao    VARCHAR(255),
    capacidade   INTEGER,
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE produto (
    id                  BIGSERIAL PRIMARY KEY,
    codigo              VARCHAR(60)  NOT NULL UNIQUE,
    nome                VARCHAR(150) NOT NULL,
    descricao           VARCHAR(255),
    categoria_id        BIGINT       NOT NULL REFERENCES categoria (id),
    lote_id             BIGINT       NOT NULL REFERENCES lote (id),
    unidade_medida      VARCHAR(20)  NOT NULL CHECK (unidade_medida IN
                         ('UNIDADE', 'GRAMA', 'QUILOGRAMA', 'MILILITRO', 'LITRO', 'CAIXA', 'PACOTE', 'FRASCO')),
    quantidade_estoque  INTEGER      NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    estoque_minimo      INTEGER      NOT NULL DEFAULT 0 CHECK (estoque_minimo >= 0),
    localizacao         VARCHAR(120),
    ativo               BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em           TIMESTAMP    NOT NULL DEFAULT now(),
    atualizado_em       TIMESTAMP
);

CREATE INDEX idx_produto_categoria ON produto (categoria_id);
CREATE INDEX idx_produto_lote ON produto (lote_id);

-- =========================================================
-- Movimentacoes de estoque (RF06, RF07, RF08)
-- =========================================================
CREATE TABLE movimentacao_estoque (
    id                      BIGSERIAL PRIMARY KEY,
    produto_id              BIGINT      NOT NULL REFERENCES produto (id),
    lote_id                 BIGINT      NOT NULL REFERENCES lote (id),
    usuario_id              BIGINT      REFERENCES usuario (id),
    tipo                    VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    origem                  VARCHAR(40) NOT NULL CHECK (origem IN
                             ('RECEBIMENTO', 'PREPARACAO_PRATICA', 'EMPRESTIMO', 'DEVOLUCAO_EMPRESTIMO',
                              'AJUSTE_MANUAL', 'DESCARTE')),
    quantidade              INTEGER     NOT NULL,
    quantidade_anterior     INTEGER     NOT NULL,
    quantidade_resultante   INTEGER     NOT NULL,
    referencia_id           BIGINT,
    observacao              VARCHAR(255),
    data_movimentacao       TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE INDEX idx_movimentacao_produto ON movimentacao_estoque (produto_id);
CREATE INDEX idx_movimentacao_usuario ON movimentacao_estoque (usuario_id);
CREATE INDEX idx_movimentacao_data ON movimentacao_estoque (data_movimentacao);

CREATE TABLE recebimento (
    id                  BIGSERIAL PRIMARY KEY,
    tipo_entrada        VARCHAR(30) NOT NULL CHECK (tipo_entrada IN ('CODIGO_SISTEMA', 'NOTA_FISCAL')),
    numero_nota_fiscal  VARCHAR(60),
    fornecedor          VARCHAR(150),
    data_recebimento    DATE        NOT NULL,
    usuario_id          BIGINT      NOT NULL REFERENCES usuario (id),
    observacao          VARCHAR(255),
    criado_em           TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE TABLE recebimento_item (
    id                  BIGSERIAL PRIMARY KEY,
    recebimento_id      BIGINT        NOT NULL REFERENCES recebimento (id) ON DELETE CASCADE,
    produto_id          BIGINT        NOT NULL REFERENCES produto (id),
    lote_id             BIGINT        NOT NULL REFERENCES lote (id),
    quantidade          INTEGER       NOT NULL CHECK (quantidade > 0),
    valor_unitario      NUMERIC(12,2),
    vencido_confirmado  BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_recebimento_item_recebimento ON recebimento_item (recebimento_id);

CREATE TABLE solicitacao_compra (
    id                      BIGSERIAL PRIMARY KEY,
    produto_id              BIGINT      NOT NULL REFERENCES produto (id),
    quantidade_sugerida     INTEGER     NOT NULL,
    situacao                VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                             CHECK (situacao IN ('PENDENTE', 'ATENDIDA', 'RECUSADA', 'CANCELADA')),
    gerada_automaticamente  BOOLEAN     NOT NULL DEFAULT TRUE,
    data_geracao            TIMESTAMP   NOT NULL DEFAULT now(),
    data_atendimento        TIMESTAMP,
    usuario_atendimento_id  BIGINT      REFERENCES usuario (id),
    observacao              VARCHAR(255)
);

CREATE INDEX idx_solicitacao_compra_produto ON solicitacao_compra (produto_id);
CREATE INDEX idx_solicitacao_compra_situacao ON solicitacao_compra (situacao);

-- =========================================================
-- Emprestimos (RF11, RF12)
-- =========================================================
CREATE TABLE emprestimo (
    id                      BIGSERIAL PRIMARY KEY,
    produto_id              BIGINT      NOT NULL REFERENCES produto (id),
    professor_id            BIGINT      NOT NULL REFERENCES usuario (id),
    usuario_atendimento_id  BIGINT      REFERENCES usuario (id),
    quantidade              INTEGER     NOT NULL DEFAULT 1 CHECK (quantidade > 0),
    data_inicio             DATE        NOT NULL,
    data_fim                DATE        NOT NULL,
    finalidade              VARCHAR(255),
    situacao                VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                             CHECK (situacao IN ('PENDENTE', 'APROVADO', 'RECUSADO', 'RETIRADO', 'DEVOLVIDO', 'CANCELADO')),
    data_solicitacao        TIMESTAMP   NOT NULL DEFAULT now(),
    data_retirada           TIMESTAMP,
    data_devolucao          TIMESTAMP,
    ocorrencia              VARCHAR(255),
    observacao              VARCHAR(255),
    CHECK (data_fim >= data_inicio)
);

CREATE INDEX idx_emprestimo_produto ON emprestimo (produto_id);
CREATE INDEX idx_emprestimo_professor ON emprestimo (professor_id);
CREATE INDEX idx_emprestimo_situacao ON emprestimo (situacao);

-- =========================================================
-- Roteiros de pratica e apoio de IA (RF13, RF17)
-- =========================================================
CREATE TABLE roteiro (
    id                          BIGSERIAL PRIMARY KEY,
    titulo                      VARCHAR(150) NOT NULL,
    objetivo                    TEXT,
    procedimento                TEXT,
    tema                        VARCHAR(150),
    disciplina                  VARCHAR(80),
    nivel_ensino                VARCHAR(30) CHECK (nivel_ensino IN ('FUNDAMENTAL_I', 'FUNDAMENTAL_II', 'MEDIO')),
    nivel_seguranca             VARCHAR(20) NOT NULL DEFAULT 'BAIXO'
                                 CHECK (nivel_seguranca IN ('BAIXO', 'MEDIO', 'ALTO')),
    tempo_estimado_minutos      INTEGER,
    professor_id                BIGINT       NOT NULL REFERENCES usuario (id),
    gerado_por_ia               BOOLEAN      NOT NULL DEFAULT FALSE,
    publicado                   BOOLEAN      NOT NULL DEFAULT FALSE,
    possui_pendencia_material   BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em                   TIMESTAMP    NOT NULL DEFAULT now(),
    atualizado_em               TIMESTAMP
);

CREATE INDEX idx_roteiro_professor ON roteiro (professor_id);

CREATE TABLE roteiro_material (
    id                      BIGSERIAL PRIMARY KEY,
    roteiro_id              BIGINT  NOT NULL REFERENCES roteiro (id) ON DELETE CASCADE,
    produto_id              BIGINT  NOT NULL REFERENCES produto (id),
    quantidade_necessaria   INTEGER NOT NULL DEFAULT 1 CHECK (quantidade_necessaria > 0),
    observacao              VARCHAR(255),
    CONSTRAINT uk_roteiro_material UNIQUE (roteiro_id, produto_id)
);

CREATE INDEX idx_roteiro_material_produto ON roteiro_material (produto_id);

-- =========================================================
-- Agendamento e preparacao de praticas (RF14, RF15, RF16)
-- =========================================================
CREATE TABLE agendamento (
    id              BIGSERIAL PRIMARY KEY,
    professor_id    BIGINT      NOT NULL REFERENCES usuario (id),
    laboratorio_id  BIGINT      NOT NULL REFERENCES laboratorio (id),
    roteiro_id      BIGINT      REFERENCES roteiro (id),
    turma           VARCHAR(60),
    data            DATE        NOT NULL,
    hora_inicio     TIME        NOT NULL,
    hora_fim        TIME        NOT NULL,
    situacao        VARCHAR(20) NOT NULL DEFAULT 'AGENDADO'
                     CHECK (situacao IN ('AGENDADO', 'PREPARADO', 'REALIZADO', 'CANCELADO')),
    observacao      VARCHAR(255),
    criado_em       TIMESTAMP   NOT NULL DEFAULT now(),
    atualizado_em   TIMESTAMP,
    CHECK (hora_fim > hora_inicio)
);

CREATE INDEX idx_agendamento_professor ON agendamento (professor_id);
CREATE INDEX idx_agendamento_laboratorio_data ON agendamento (laboratorio_id, data);

CREATE TABLE preparacao_pratica (
    id              BIGSERIAL PRIMARY KEY,
    agendamento_id  BIGINT      NOT NULL UNIQUE REFERENCES agendamento (id),
    auxiliar_id     BIGINT      NOT NULL REFERENCES usuario (id),
    situacao        VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                     CHECK (situacao IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'COM_PENDENCIA')),
    data_inicio     TIMESTAMP   NOT NULL DEFAULT now(),
    data_conclusao  TIMESTAMP,
    observacao      VARCHAR(255)
);

CREATE TABLE preparacao_item (
    id                      BIGSERIAL PRIMARY KEY,
    preparacao_id           BIGINT  NOT NULL REFERENCES preparacao_pratica (id) ON DELETE CASCADE,
    produto_id              BIGINT  NOT NULL REFERENCES produto (id),
    quantidade_necessaria   INTEGER NOT NULL,
    quantidade_separada     INTEGER NOT NULL DEFAULT 0,
    separado                BOOLEAN NOT NULL DEFAULT FALSE,
    pendente                BOOLEAN NOT NULL DEFAULT FALSE,
    observacao              VARCHAR(255)
);

CREATE INDEX idx_preparacao_item_preparacao ON preparacao_item (preparacao_id);
CREATE INDEX idx_preparacao_item_produto ON preparacao_item (produto_id);

-- =========================================================
-- Notificacoes (RF18)
-- =========================================================
CREATE TABLE notificacao (
    id              BIGSERIAL PRIMARY KEY,
    usuario_id      BIGINT      NOT NULL REFERENCES usuario (id),
    tipo            VARCHAR(40) NOT NULL CHECK (tipo IN
                     ('NOVIDADES_SEMANAIS', 'LEMBRETE_PRATICA', 'SOLICITACAO_COMPRA',
                      'SOLICITACAO_EMPRESTIMO', 'AGENDAMENTO', 'PREPARACAO_PRATICA')),
    assunto         VARCHAR(150) NOT NULL,
    mensagem        TEXT,
    situacao        VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                     CHECK (situacao IN ('PENDENTE', 'ENVIADA', 'FALHA')),
    tentativas      INTEGER     NOT NULL DEFAULT 0,
    erro            VARCHAR(255),
    data_geracao    TIMESTAMP   NOT NULL DEFAULT now(),
    data_envio      TIMESTAMP
);

CREATE INDEX idx_notificacao_usuario ON notificacao (usuario_id);
CREATE INDEX idx_notificacao_situacao ON notificacao (situacao);
