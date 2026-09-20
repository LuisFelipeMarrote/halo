# API de projetos e tarefas

Especificação de um projeto de estudo de Java e Spring. O objetivo é praticar a linguagem, REST, injeção de dependências, validação e persistência, com as decisões de produto já definidas.

## Índice

- [Escopo](#1-escopo)
- [Modelo de dados](#2-modelo-de-dados)
- [Regras de negócio](#3-regras-de-negócio)
- [Operações HTTP](#4-operações-http)
- [Contratos JSON](#5-contratos-json)
- [Plano de classes](#6-plano-de-classes)
- [Tratamento de erros](#7-tratamento-de-erros)
- [Ordem de implementação](#8-ordem-de-implementação)
- [Checklist de conclusão](#9-checklist-de-conclusão)

## 1 Escopo

Construir uma API para cadastrar projetos e acompanhar suas tarefas. Permitir criar, consultar, editar e excluir ambos, alterar o status de tarefas e filtrar tarefas por status.

Tecnologias: Java, Spring Boot, Maven, Spring Web, Spring Data JPA, Bean Validation e H2. Escolha uma versão LTS de Java compatível com a versão de Spring Boot utilizada.

Fora do escopo: autenticação, usuários, interface web, paginação, notificações, deploy e Docker. Encerre a primeira versão quando o checklist final estiver completo.

## 2 Modelo de dados

Relação: Projeto (1) → Tarefa (N). Um projeto pode não ter tarefas; toda tarefa pertence a exatamente um projeto. Mapeie somente Tarefa → Projeto no JPA. Não é necessário manter uma coleção de tarefas em Projeto.

### Projeto

| Campo | Tipo Java | Regra |
|---|---|---|
| id | Long | Gerado automaticamente |
| nome | String | Obrigatório; 3 a 80 caracteres |
| descricao | String | Opcional; até 500 caracteres |
| criadoEm | LocalDateTime | Definido na criação; imutável |

### Tarefa

| Campo | Tipo Java | Regra |
|---|---|---|
| id | Long | Gerado automaticamente |
| titulo | String | Obrigatório; 3 a 120 caracteres |
| descricao | String | Opcional; até 1.000 caracteres |
| status | StatusTarefa | Inicialmente PENDENTE |
| projeto | Projeto | Associação obrigatória |
| criadaEm | LocalDateTime | Definido na criação; imutável |

### StatusTarefa

Enum com PENDENTE, EM_ANDAMENTO e CONCLUIDA. Qualquer transição é permitida, inclusive reabrir uma tarefa concluída.

## 3 Regras de negócio

1. Remova espaços nas extremidades de nomes, títulos e descrições antes de salvar. Respeite os limites de tamanho após essa normalização.
2. Nomes e títulos compostos apenas por espaços são inválidos.
3. Nomes de projetos e títulos de tarefas podem se repetir.
4. Uma tarefa só pode ser criada em um projeto existente.
5. Uma tarefa não pode ser transferida para outro projeto.
6. Um projeto com tarefas não pode ser excluído, mesmo se todas estiverem concluídas. Retorne 409.
7. Identificadores, datas e status inicial são definidos pelo servidor.
8. Nas rotas de tarefa, ela precisa pertencer ao projeto informado. Caso contrário, retorne 404.

## 4 Operações HTTP

Todas as rotas abaixo recebem o prefixo /api.

### Projetos

| Método | Rota | Operação | Sucesso |
|---|---|---|---|
| POST | /projetos | Criar | 201 |
| GET | /projetos | Listar | 200 |
| GET | /projetos/{id} | Consultar | 200 |
| PUT | /projetos/{id} | Editar nome e descrição | 200 |
| DELETE | /projetos/{id} | Excluir se não houver tarefas | 204 |

### Tarefas

Base das rotas desta tabela: /api/projetos/{projetoId}/tarefas.

| Método | Sufixo da base | Operação | Sucesso |
|---|---|---|---|
| POST | Nenhum | Criar | 201 |
| GET | Nenhum | Listar e filtrar | 200 |
| GET | /{id} | Consultar | 200 |
| PUT | /{id} | Editar título e descrição | 200 |
| PATCH | /{id}/status | Alterar status | 200 |
| DELETE | /{id} | Excluir | 204 |

Filtro opcional: GET /api/projetos/1/tarefas?status=PENDENTE.

- Listagens retornam arrays em ordem crescente de id; sem resultados, retornam [].
- Listar tarefas de um projeto inexistente retorna 404.
- PUT recebe os campos editáveis; descrição ausente ou null limpa a descrição.
- PUT de tarefa não altera status, projeto ou data de criação.
- Respostas 201 incluem o recurso criado e o cabeçalho Location.
- Respostas 204 não possuem corpo.

## 5 Contratos JSON

### Criar ou atualizar projeto

```json
{
  "nome": "Estudos de Spring",
  "descricao": "Exercícios para aprender o framework"
}
```

### Resposta de projeto

```json
{
  "id": 1,
  "nome": "Estudos de Spring",
  "descricao": "Exercícios para aprender o framework",
  "criadoEm": "2026-09-16T14:30:00"
}
```

### Criar ou atualizar tarefa

```json
{
  "titulo": "Implementar o cadastro de projetos",
  "descricao": "Criar controller, service e repository"
}
```

### Alterar status

```json
{
  "status": "EM_ANDAMENTO"
}
```

### Resposta de tarefa

```json
{
  "id": 10,
  "titulo": "Implementar o cadastro de projetos",
  "descricao": "Criar controller, service e repository",
  "status": "EM_ANDAMENTO",
  "projetoId": 1,
  "criadaEm": "2026-09-16T14:35:00"
}
```

Não retorne entidades JPA diretamente. Use DTOs para expor apenas os campos definidos nos contratos.

## 6 Plano de classes

Pacote raiz: com.exemplo.gestortarefas. Classe de entrada: GestorTarefasApplication.

| Pacote | Classes |
|---|---|
| projeto | Projeto, ProjetoRepository, ProjetoService, ProjetoController, ProjetoRequest, ProjetoResponse |
| tarefa | Tarefa, StatusTarefa, TarefaRepository, TarefaService, TarefaController, TarefaRequest, AlterarStatusRequest, TarefaResponse |
| erro | RecursoNaoEncontradoException, ConflitoException, ErroResponse, ApiExceptionHandler |

### Responsabilidades

- Entidades: representar os dados persistidos e suas associações.
- Controllers: receber HTTP, validar entradas e devolver respostas.
- Services: aplicar regras de negócio e coordenar operações.
- Repositories: consultar e persistir entidades; estender JpaRepository.
- DTOs Request: definir os campos aceitos pela API.
- DTOs Response: definir os campos devolvidos pela API.
- ApiExceptionHandler: converter exceções em respostas HTTP consistentes.

Use injeção pelo construtor, serviços concretos sem interfaces adicionais e conversão manual entre entidades e DTOs dentro dos serviços.

### ProjetoService

```text
criar(request)
listar()
buscarPorId(id)
atualizar(id, request)
excluir(id)
```

### TarefaService

```text
criar(projetoId, request)
listar(projetoId, statusOpcional)
buscarPorId(projetoId, tarefaId)
atualizar(projetoId, tarefaId, request)
alterarStatus(projetoId, tarefaId, request)
excluir(projetoId, tarefaId)
```

Além das operações herdadas de JpaRepository, o repositório de tarefas deve permitir listar por projeto, filtrar por projeto e status, buscar por id e projeto e verificar se um projeto possui tarefas.

### Ponte com Node e TypeScript

Controllers correspondem, aproximadamente, aos handlers de rotas. DTOs representam contratos de entrada e saída, como os tipos usados em TypeScript. Services concentram regras e repositories fazem o acesso aos dados. A validação das requisições ocorre em tempo de execução.

## 7 Tratamento de erros

| Situação | HTTP |
|---|---|
| Entrada inválida, JSON malformado ou status desconhecido | 400 |
| Projeto ou tarefa não encontrado | 404 |
| Exclusão de projeto com tarefas | 409 |

Formato único de resposta:

```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "campos": {
    "titulo": "Deve conter entre 3 e 120 caracteres"
  }
}
```

Quando não houver erros associados a campos, devolva "campos": {}.

## 8 Ordem de implementação

1. Estrutura inicial: criar a aplicação, configurar H2 e fazer o projeto iniciar.
2. Projetos: implementar entidade, repositório, DTOs, serviço e controller.
3. Validação e erros: implementar restrições e tratamento centralizado.
4. Tarefas: implementar associação ao projeto e operações básicas.
5. Regras restantes: filtro, alteração de status e bloqueio da exclusão de projetos com tarefas.
6. Testes: verificar os critérios de conclusão abaixo.

## 9 Checklist de conclusão

- [ ] Criar um projeto e recuperá-lo pela API.
- [ ] Rejeitar projeto com nome em branco.
- [ ] Criar tarefa com status inicial PENDENTE.
- [ ] Rejeitar tarefa vinculada a projeto inexistente.
- [ ] Atualizar título e descrição sem alterar status ou data de criação.
- [ ] Alterar o status e filtrar tarefas corretamente.
- [ ] Retornar 404 ao acessar tarefa usando o projeto errado.
- [ ] Impedir a exclusão de projeto com tarefas.
- [ ] Excluir as tarefas e depois excluir o projeto.

Automatize pelo menos os testes de criação, validação, associação ao projeto e conflito na exclusão. Ao concluir estes critérios, encerre esta versão.
