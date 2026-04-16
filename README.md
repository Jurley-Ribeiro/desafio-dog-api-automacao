# 🐶 Dog API — Testes Automatizados

> Suíte de testes de integração para a [Dog API](https://dog.ceo/dog-api/documentation), desenvolvida em Java com REST Assured, JUnit 5 e Allure Report.

---

## 📑 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias](#-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Executando os Testes](#-executando-os-testes)
- [Relatório Allure](#-relatório-allure)
- [Endpoints Testados](#-endpoints-testados)
- [Cenários de Teste](#-cenários-de-teste)
- [Pipeline CI/CD](#-pipeline-cicd)
- [Boas Práticas Aplicadas](#-boas-práticas-aplicadas)

---

## 🔍 Visão Geral

Este projeto valida o comportamento dos principais endpoints da **Dog API**, garantindo:

- ✅ Respostas HTTP corretas (status codes)
- ✅ Formato e estrutura dos payloads JSON
- ✅ Integridade das URLs de imagens retornadas
- ✅ Comportamento em cenários de erro (raças inexistentes)
- ✅ Randomicidade do endpoint de imagem aleatória
- ✅ Tempo de resposta dentro do limite aceitável

---

## 🛠 Tecnologias

| Tecnologia        | Versão  | Finalidade                              |
|-------------------|---------|-----------------------------------------|
| Java              | 11+     | Linguagem principal                     |
| Maven             | 3.8+    | Gerenciamento de dependências e build   |
| JUnit 5           | 5.10.2  | Framework de testes                     |
| REST Assured      | 5.4.0   | Cliente HTTP para testes de API         |
| Allure Report     | 2.26.0  | Relatório visual de execução dos testes |
| AssertJ           | 3.25.3  | Asserções fluentes e legíveis           |
| Owner             | 1.0.12  | Gerenciamento tipado de configurações   |
| Jackson           | 2.17.0  | Desserialização de JSON                 |
| Logback           | 1.5.3   | Logging durante a execução              |

---

## 📁 Estrutura do Projeto

```
dog-api-tests/
├── .github/
│   └── workflows/
│       └── ci.yml                        # Pipeline GitHub Actions
├── src/
│   └── test/
│       ├── java/com/dogapi/
│       │   ├── client/
│       │   │   └── DogApiClient.java     # Encapsula todas as chamadas HTTP
│       │   ├── config/
│       │   │   ├── ApiConfig.java        # Interface de configuração (Owner)
│       │   │   └── ConfigProvider.java   # Singleton de configuração
│       │   ├── model/
│       │   │   ├── BreedsListResponse.java  # DTO para listagem de raças
│       │   │   ├── ImageResponse.java       # DTO para respostas de imagens
│       │   │   └── ErrorResponse.java       # DTO para respostas de erro
│       │   ├── tests/
│       │   │   ├── BaseTest.java         # Setup/teardown compartilhado
│       │   │   ├── BreedsListTest.java   # Testes: GET /breeds/list/all
│       │   │   ├── BreedImagesTest.java  # Testes: GET /breed/{breed}/images
│       │   │   └── RandomImageTest.java  # Testes: GET /breeds/image/random
│       │   └── util/
│       │       ├── ApiAssertions.java         # Asserções reutilizáveis
│       │       └── AllureRestAssuredFilter.java  # Filtro Allure p/ REST Assured
│       └── resources/
│           ├── api.properties            # Configurações da API
│           ├── allure.properties         # Configurações do Allure
│           └── logback-test.xml          # Configuração de logging
└── pom.xml                               # Dependências e plugins Maven
```

---

## ✅ Pré-requisitos

Antes de executar os testes, certifique-se de ter instalado:

| Ferramenta | Versão mínima | Verificação                  |
|------------|---------------|------------------------------|
| Java (JDK) | 11            | `java -version`              |
| Maven      | 3.8           | `mvn -version`               |
| Git        | qualquer      | `git --version`              |

> **Allure CLI** (opcional — apenas para visualizar o relatório localmente):
> ```bash
> # macOS
> brew install allure
>
> # Windows (via Scoop)
> scoop install allure
>
> # Linux
> curl -o allure.tgz -L https://github.com/allure-framework/allure2/releases/download/2.26.0/allure-2.26.0.tgz
> tar -xzf allure.tgz -C /opt/
> export PATH=$PATH:/opt/allure-2.26.0/bin
> ```

---

## ⚙️ Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/SEU_USUARIO/dog-api-tests.git
cd dog-api-tests
```

### 2. Instale as dependências

```bash
mvn clean install -DskipTests
```

### 3. (Opcional) Ajuste as configurações

As configurações ficam em `src/test/resources/api.properties`:

```properties
api.base.url=https://dog.ceo/api
api.timeout.connect=10000
api.timeout.read=15000
api.status.success=success
```

Você também pode sobrescrever via **variável de ambiente** ou **system property** do Maven:

```bash
mvn test -Dapi.base.url=https://dog.ceo/api -Dapi.timeout.read=20000
```

---

## ▶️ Executando os Testes

### Executar todos os testes

```bash
mvn clean test
```

### Executar apenas testes de smoke

```bash
mvn clean test -Dgroups="smoke"
```

### Executar testes de uma classe específica

```bash
# Apenas testes do endpoint /breeds/list/all
mvn clean test -Dtest=BreedsListTest

# Apenas testes do endpoint /breed/{breed}/images
mvn clean test -Dtest=BreedImagesTest

# Apenas testes do endpoint /breeds/image/random
mvn clean test -Dtest=RandomImageTest
```

### Executar um teste específico

```bash
mvn clean test -Dtest=BreedsListTest#shouldContainKnownBreeds
```

---

## 📊 Relatório Allure

### Gerar e abrir o relatório após a execução

```bash
# Executa os testes e gera o relatório em uma etapa
mvn clean test allure:report

# Abre o relatório no browser automaticamente
mvn allure:serve
```

O relatório HTML estático fica disponível em:
```
target/allure-report/index.html
```

### Exemplo de relatório gerado

O relatório Allure exibe:

- 📋 **Dashboard** com totais de testes: passados, falhos, quebrados, ignorados
- 🗂️ **Suítes** organizadas por `@Epic`, `@Feature` e `@Story`
- 🔍 **Detalhes de cada teste**: request HTTP, response, asserções e logs
- 📈 **Histórico de execuções** (quando configurado com GitHub Pages)
- ⏱️ **Duração** de cada teste e da suíte completa

---

## 🔗 Endpoints Testados

| Endpoint                      | Método | Descrição                                   |
|-------------------------------|--------|---------------------------------------------|
| `/breeds/list/all`            | GET    | Lista todas as raças e sub-raças            |
| `/breed/{breed}/images`       | GET    | Retorna todas as imagens de uma raça        |
| `/breeds/image/random`        | GET    | Retorna uma imagem aleatória de qualquer raça |

---

## 🧪 Cenários de Teste

### `BreedsListTest` — GET /breeds/list/all

| # | Cenário                                                   | Severidade |
|---|-----------------------------------------------------------|------------|
| 1 | Deve retornar HTTP 200 e status `success`                 | BLOCKER    |
| 2 | Deve retornar Content-Type `application/json`             | CRITICAL   |
| 3 | Deve retornar lista de raças não vazia                    | CRITICAL   |
| 4 | Deve conter raças conhecidas (`hound`, `bulldog`, etc.)   | NORMAL     |
| 5 | Deve retornar sub-raças corretas para `hound`             | NORMAL     |
| 6 | Deve retornar lista vazia para raças sem sub-raças        | MINOR      |
| 7 | Todos os nomes de raças devem ser minúsculos              | MINOR      |

### `BreedImagesTest` — GET /breed/{breed}/images

| #  | Cenário                                                         | Severidade |
|----|-----------------------------------------------------------------|------------|
| 1  | Deve retornar HTTP 200 para raça válida (`hound`)               | BLOCKER    |
| 2  | Deve retornar lista de imagens não vazia                        | CRITICAL   |
| 3  | Todas as URLs de imagem devem ser válidas                       | CRITICAL   |
| 4  | Deve retornar imagens para múltiplas raças (parametrizado × 7)  | NORMAL     |
| 5  | Deve retornar 404 para raça inexistente                         | CRITICAL   |
| 6  | Deve retornar imagens para sub-raça válida (`hound/afghan`)     | NORMAL     |
| 7  | Deve retornar Content-Type `application/json`                   | MINOR      |
| 8  | URLs das imagens devem conter o nome da raça solicitada         | NORMAL     |

### `RandomImageTest` — GET /breeds/image/random

| # | Cenário                                                         | Severidade |
|---|-----------------------------------------------------------------|------------|
| 1 | Deve retornar HTTP 200 e status `success`                       | BLOCKER    |
| 2 | Deve retornar URL de imagem válida no campo `message`           | CRITICAL   |
| 3 | Deve retornar Content-Type `application/json`                   | CRITICAL   |
| 4 | Deve retornar URLs diferentes em chamadas sucessivas            | NORMAL     |
| 5 | Deve responder em menos de 3 segundos                           | MINOR      |
| 6 | Deve retornar resposta consistente em execuções repetidas (×3)  | MINOR      |
| 7 | URL deve apontar para o domínio oficial `images.dog.ceo`        | MINOR      |

**Total: ~23 cenários de teste**

---

## 🚀 Pipeline CI/CD

O projeto inclui uma pipeline no **GitHub Actions** (`.github/workflows/ci.yml`) com os seguintes gatilhos:

| Gatilho             | Descrição                                           |
|---------------------|-----------------------------------------------------|
| `push` para `main`  | Executa ao enviar código para a branch principal    |
| `push` para `develop` | Executa ao enviar código para a branch de desenvolvimento |
| `pull_request`      | Executa em todo PR aberto para `main`               |
| `schedule` (cron)   | Executa diariamente às 06:00 UTC                    |
| `workflow_dispatch` | Permite execução manual pela interface do GitHub    |

### Artefatos gerados pela pipeline

- 📦 `allure-results` — resultados brutos (30 dias de retenção)
- 📊 `allure-report` — relatório HTML completo (30 dias de retenção)
- 📝 `test-logs` — logs de execução (7 dias de retenção)

### Publicação automática do relatório

Quando os testes passam na branch `main`, o relatório Allure é publicado automaticamente no **GitHub Pages** em:
```
https://SEU_USUARIO.github.io/dog-api-tests/allure-report
```

> Para habilitar o GitHub Pages: vá em **Settings → Pages → Source → Deploy from branch → gh-pages**.

---

## 🏗️ Boas Práticas Aplicadas

### Clean Code
- Nomes descritivos para classes, métodos e variáveis
- Métodos pequenos com responsabilidade única
- Sem magic numbers (constantes nomeadas)
- Comentários apenas onde agregam valor

### Princípios SOLID
- **SRP** — cada classe tem uma única responsabilidade (`DogApiClient` só faz requisições, `ApiAssertions` só faz asserções, etc.)
- **OCP** — novos endpoints podem ser adicionados ao `DogApiClient` sem modificar os existentes
- **ISP** — `ApiConfig` expõe apenas o contrato necessário para a configuração da API
- **DIP** — testes dependem de abstrações (`DogApiClient`, `ApiAssertions`) e não de implementações HTTP diretamente

### Padrões de teste
- **AAA** (Arrange, Act, Assert) em todos os testes
- **Parametrização** com `@ParameterizedTest` para testar múltiplas raças
- **Hierarquia** com `BaseTest` para evitar duplicação de setup
- **Rastreabilidade** com `@Epic`, `@Feature`, `@Story` e `@Severity` do Allure
- **Tags** para seleção granular (`@Tag("smoke")`, `@Tag("breeds")`, etc.)

---

## 👤 Autor

Desenvolvido como desafio técnico de QA — automação de testes para a Dog API.

---

*Feito com ☕ e muito amor por cachorros 🐕*
