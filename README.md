# Mist 🎮

Aplicativo Android para catálogo e gerenciamento de consoles de videogame, desenvolvido como trabalho final da disciplina de **Programação de Dispositivos Móveis** no curso de Técnico em Informática Integrado ao Ensino Médio — **IFSP Hortolândia (2025)**.

O projeto é composto por dois componentes integrados: um **app Android nativo em Kotlin** e um **webservice REST em Python/Flask**, com banco de dados **MySQL** como camada de persistência remota.

---

## Funcionalidades

- Listagem de consoles com nome, preço, ano de lançamento, marca e cor
- Cadastro de novo console com seleção de marca e cor via radio buttons
- Edição de console existente com pré-carregamento dos dados atuais
- Remoção de console com tratamento de erros de rede
- Atualização da lista via gesto de pull-to-refresh
- Validação de campos no formulário (nome obrigatório, preço numérico, ano entre 1901 e 2155)
- Sincronização automática com a API remota na abertura da listagem
- Cache local com Room para funcionamento offline após primeira sincronização
- Feedback ao usuário via Snackbar em caso de erros de validação

---

## Arquitetura

O projeto segue uma arquitetura em camadas (**MVVM**) com separação clara entre UI, lógica de negócio e acesso a dados, tanto locais quanto remotos.

```
┌─────────────────────────────────────────┐
│              App Android (Kotlin)        │
│                                         │
│  UI (Jetpack Compose + Material 3)      │
│       ↕ eventos / estado                │
│  ViewModel (StateFlow + Channel)        │
│       ↕ repositório                     │
│  Repository (interface)                 │
│    ├── Room (SQLite local)              │
│    └── Retrofit → API REST              │
└─────────────────┬───────────────────────┘
                  │ HTTP (porta 5000)
┌─────────────────▼───────────────────────┐
│         Webservice (Python / Flask)      │
│                                         │
│  Rotas REST: consoles, jogos, cores,    │
│  marcas — CRUD completo                 │
│       ↕ SQL                             │
│  Banco de dados MySQL (banco: mist)     │
└─────────────────────────────────────────┘
```

---

## Tecnologias

### App Android

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose (rotas type-safe com `@Serializable`) |
| Estado / ViewModel | ViewModel + StateFlow + Channel |
| Banco de dados local | Room (SQLite) com chaves estrangeiras e `CASCADE` |
| HTTP Client | Retrofit 2 + Moshi (serialização JSON) |
| Logging de rede | OkHttp Logging Interceptor |
| Build | Gradle KTS + KSP |
| SDK mínimo | Android 7.0 (API 24) |
| SDK alvo | Android 15 (API 36) |

### Webservice

| Componente | Tecnologia |
|---|---|
| Linguagem | Python |
| Framework | Flask |
| Banco de dados | MySQL (MariaDB 10.4) |
| Conector | mysql-connector-python |

---

## Estrutura do Projeto

```
Mist/
├── app/                          # App Android
│   └── src/main/java/com/example/mist/
│       ├── data/
│       │   ├── local/            # Room: DAOs, Entities, Repositories, DatabaseProvider
│       │   └── remote/           # Retrofit: ApiClient, MistService
│       ├── domain/               # Modelos: Console, Jogo, Cor, Marca, ConsoleJogo
│       ├── navigation/           # MistNavHost — rotas type-safe
│       ├── screen/
│       │   ├── list/             # ListScreen + ListViewModel + ListEvent
│       │   └── addEdit/          # AddEditScreen + AddEditViewModel + AddEditEvent
│       └── ui/theme/             # Tema Material 3
│
webservice/
├── app.py                        # API REST Flask com todos os endpoints
└── schema/
    └── schema.sql                # Script de criação do banco MySQL
```

---

## Banco de Dados Relacional

O banco `mist` é composto por 5 tabelas com relacionamentos por chave estrangeira:

```sql
marca        (id, marca)
cor          (id, cor)
console      (id, nome, preco, ano, marca_id → marca, cor_id → cor)
jogo         (id, nome, preco, ano, capa)
console_jogo (console_id → console, jogo_id → jogo)  -- associação N:N
```

As chaves estrangeiras garantem integridade referencial. O campo `nome` em `console` e `jogo` possui constraint `UNIQUE`, impedindo cadastros duplicados.

No app Android, as mesmas entidades são espelhadas localmente via **Room**, com `ForeignKey` e `CASCADE` configurados nas entidades Kotlin, e acesso ao banco via padrão Singleton thread-safe (`@Volatile`).

---

## API REST — Endpoints

### Consoles (Plataformas)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/plataformas` | Lista todos os consoles com marca e cor aninhados |
| GET | `/plataforma/<id>` | Busca console por ID |
| POST | `/plataforma` | Cadastra novo console |
| PUT | `/plataforma/<id>` | Atualiza console existente |
| DELETE | `/plataforma/<id>` | Remove console |

### Jogos

| Método | Rota | Descrição |
|---|---|---|
| GET | `/jogos` | Lista todos os jogos |
| GET | `/jogo/<id>` | Busca jogo por ID |
| POST | `/jogo` | Cadastra novo jogo |
| PUT | `/jogo/<id>` | Atualiza jogo existente |
| DELETE | `/jogo/<id>` | Remove jogo |

### Cores e Marcas

| Método | Rota | Descrição |
|---|---|---|
| GET | `/cores` | Lista todas as cores |
| GET | `/cor/<id>` | Busca cor por ID |
| POST | `/cor` | Cadastra nova cor |
| GET | `/marcas` | Lista todas as marcas |
| GET | `/marca/<id>` | Busca marca por ID |
| POST | `/marca` | Cadastra nova marca |

---

## Como Executar

### Pré-requisitos

- Python 3.10+
- MySQL / MariaDB
- Android Studio Hedgehog ou superior
- JDK 11+
- Dispositivo ou emulador Android 7.0+ (API 24)

### 1. Configurar o banco de dados

```bash
# No MySQL, crie o banco e importe o schema
mysql -u root -p -e "CREATE DATABASE mist;"
mysql -u root -p mist < webservice/schema/schema.sql
```

### 2. Subir o webservice

```bash
cd webservice

# Instalar dependências
pip install flask mysql-connector-python

# Configurar credenciais em app.py (host, user, password)
# Por padrão: host=localhost, user=root, password="", database=mist

python app.py
# API disponível em http://localhost:5000
```

### 3. Executar o app Android

```bash
# Clone o repositório
git clone https://github.com/yuriRLombardi/Mist.git

# Abra a pasta Mist/ no Android Studio e sincronize o Gradle
# Execute num emulador (a URL base já está configurada para 10.0.2.2:5000,
# que é o localhost do host mapeado para o emulador Android)
```

> **Atenção:** para rodar em dispositivo físico, altere a `baseUrl` em `ApiClient.kt` para o IP da máquina na rede local (ex: `http://192.168.x.x:5000`).

---

## Estratégia de Sincronização

O app adota uma abordagem **remote-first com cache local**:

1. Ao abrir a lista, o app busca cores e marcas da API e atualiza o cache Room
2. Em seguida, busca todos os consoles remotos, limpa o cache local e reinsere os dados atualizados
3. A UI observa o banco local via `Flow`, reagindo automaticamente a qualquer mudança
4. Em operações de escrita (criar/editar/deletar), o app tenta sincronizar com a API primeiro; em caso de erro HTTP, aplica rollback local baseado no código de status retornado

---

## Minha Contribuição

Atuei no **design e implementação do banco de dados relacional**, incluindo a modelagem das 5 tabelas com relacionamentos, chaves estrangeiras e constraints de integridade (`schema.sql`). No app Android, fui responsável pela camada de persistência local com Room (entidades `PlataformEntity`, `CorEntity`, `MarcaEntity`, DAOs e `DatabaseProvider`) e contribuí na **lógica de back-end**, incluindo a implementação do `PlataformaRepositoryImpl` com a estratégia de sincronização remota e tratamento de erros HTTP, e no `AddEditViewModel` com validação de formulário e gerenciamento de estado via `StateFlow`.

---

## Equipe

Projeto desenvolvido em grupo por 5 integrantes como trabalho final da disciplina de Programação de Dispositivos Móveis — IFSP Hortolândia (2025).
