# ⚽ Football Data API

API REST para ingestão, armazenamento e exposição de dados da Premier League (Inglaterra), construída com **Spring Boot (Java 21)** e **PostgreSQL**.

---

# 🚀 Visão Geral

Este projeto tem como objetivo fornecer dados estruturados de partidas de futebol, incluindo:

* Times
* Jogos
* Estatísticas detalhadas por jogo

Os dados são ingeridos automaticamente a partir de arquivos CSV públicos e disponibilizados via endpoints REST em formato JSON.

---

# 🧱 Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller → Service → Repository → Database
```

E também possui um pipeline de ingestão:

```
CSV → Parser → Ingestion Service → Banco de Dados
```

---

# 🗄️ Modelo de Dados

## Team

Representa um time.

* id
* name

---

## Match

Representa uma partida.

* id
* date
* homeTeam
* awayTeam
* homeScore
* awayScore

---

## MatchStats

Representa estatísticas detalhadas de um jogo.

* id
* match (OneToOne)
* homeShots
* awayShots
* homeShotsOnTarget
* awayShotsOnTarget
* homeCorners
* awayCorners
* homeFouls
* awayFouls

---

# 📥 Data Ingestion

Os dados são coletados de:

👉 [https://www.football-data.co.uk/](https://www.football-data.co.uk/)

Processo:

1. Download do CSV
2. Parse dos dados
3. Normalização
4. Persistência no banco

---

# 🔗 Endpoints

## Teams

### GET /api/teams

Lista todos os times

### GET /api/teams/{id}

Retorna um time específico

---

## Matches

### GET /api/matches

Lista todos os jogos

### GET /api/matches/{id}

Retorna um jogo específico

### GET /api/matches/team/{teamId}

Lista jogos de um time

---

## Match Stats

### GET /api/stats/match/{matchId}

Retorna estatísticas de um jogo

---

# 🧪 Como Rodar o Projeto

## Pré-requisitos

* Java 21
* Maven
* PostgreSQL (ou Neon)

---

## Configuração do banco

Arquivo:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<HOST>/<DB>
    username: <USER>
    password: <PASSWORD>
```

---

## Rodando a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

👉 [http://localhost:8080](http://localhost:8080)

---

# 🧪 Testando a API

Exemplo:

```bash
curl http://localhost:8080/api/teams
```

Ou via browser:

👉 [http://localhost:8080/api/matches](http://localhost:8080/api/matches)

---

# 🧠 Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* OpenCSV
* Lombok

---

# 📈 Próximos Passos

* [ ] Estatísticas médias por time
* [ ] Endpoint de analytics
* [ ] Cache (Redis)
* [ ] Autenticação (JWT)
* [ ] Deploy (AWS / Vercel / Railway)

---

# 💡 Possibilidades Futuras

* Dashboard com Next.js
* API pública monetizável
* Machine Learning (previsão de jogos)

---

# 👨‍💻 Autor

Projeto desenvolvido como estudo avançado de backend e arquitetura de APIs.

---

# ⭐ Status

🚧 Em evolução — já funcional e estruturado para escalar.
