# 🔗 URL Shortener

A lightweight **URL Shortener** REST API built with **Spring Boot**, **MySQL**, and **Docker**. It converts long URLs into short, shareable codes using a **Base62 encoding** algorithm and tracks how many times each short link has been visited.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [How It Works](#how-it-works)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Configuration](#configuration)

---

## ✨ Features

- Shorten any long URL into a compact Base62 code (e.g., `http://localhost:8080/aB3`)
- Redirect to the original URL by visiting the short code
- Tracks **click count** for every short URL
- Records the **creation timestamp** of each short URL
- Persistent storage using **MySQL**
- Easy local setup with **Docker Compose**

---

## 🛠️ Tech Stack

| Technology         | Purpose                          |
|--------------------|----------------------------------|
| Java 17            | Programming language             |
| Spring Boot 3.x    | Application framework            |
| Spring Data JPA    | Database ORM                     |
| MySQL 8            | Relational database              |
| Docker & Compose   | Containerized database setup     |
| Lombok             | Boilerplate code reduction       |
| Maven              | Build tool                       |

---

## 📁 Project Structure

```
url-shortener/
├── docker-compose.yml                  # MySQL container configuration
├── pom.xml                             # Maven dependencies
└── src/
    └── main/
        ├── java/com/shekhar/url_shortener/
        │   ├── UrlShortenerApplication.java     # Entry point
        │   ├── controller/
        │   │   └── UrlController.java           # REST endpoints
        │   ├── entity/
        │   │   └── ShortUrl.java                # JPA entity
        │   ├── repository/
        │   │   └── UrlRepository.java           # Data access layer
        │   ├── service/
        │   │   └── UrlService.java              # Business logic
        │   └── util/
        │       └── Base62.java                  # Encoding utility
        └── resources/
            └── application.properties           # App configuration
```

---

## ⚙️ How It Works

1. **Shorten a URL** — A `POST /shorten` request saves the original URL to the database and generates a unique short code.
2. **Encoding** — The auto-generated database `id` (Long) is encoded into a short alphanumeric string using **Base62** (`a-z`, `A-Z`, `0-9`), producing codes like `aB3`, `xYz1`.
3. **Redirect** — A `GET /{code}` request looks up the code, increments the click count, and returns an HTTP `302 Found` redirect to the original URL.

```
POST /shorten  {"url": "https://example.com/very/long/url"}
   → saves to DB → encodes ID → returns "aB3"

GET /aB3
   → looks up "aB3" → increments click count → 302 redirect to https://example.com/very/long/url
```

---

## ✅ Prerequisites

Make sure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven 3.6+](https://maven.apache.org/) (or use the included `mvnw` wrapper)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/url-shortener.git
cd url-shortener
```

### 2. Start MySQL with Docker Compose

```bash
docker-compose up -d
```

This starts a MySQL 8 container with:
- **Host port:** `3307`
- **Database:** `urlshortener`
- **Username:** `root`
- **Password:** `root`

> ⏳ Wait **15–20 seconds** for MySQL to fully initialize before starting the app.

Verify the container is running:

```bash
docker ps
```

You should see a container with `0.0.0.0:3307->3306/tcp`.

### 3. Build and Run the Application

Using the Maven wrapper:

```bash
# Windows
mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

Or build a JAR and run it:

```bash
mvnw.cmd clean package
java -jar target/url-shortener-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**.

---

## 📡 API Endpoints

### ➕ Shorten a URL

**`POST /shorten`**

Request Body:
```json
{
  "url": "https://www.example.com/some/very/long/url"
}
```

Response (plain text):
```
aB3
```

Example with `curl`:
```bash
curl -X POST http://localhost:8080/shorten \
  -H "Content-Type: application/json" \
  -d "{\"url\": \"https://www.example.com/some/very/long/url\"}"
```

---

### 🔀 Redirect to Original URL

**`GET /{code}`**

- Returns HTTP `302 Found` with a `Location` header pointing to the original URL.
- Increments the click count for that short URL.

Example:
```bash
curl -L http://localhost:8080/aB3
```

| Response Code | Description                          |
|---------------|--------------------------------------|
| `302 Found`   | Redirects to the original URL        |
| `500`         | Short code not found in the database |

---

## 🗄️ Database Schema

Table: **`short_url`**

| Column        | Type          | Description                         |
|---------------|---------------|-------------------------------------|
| `id`          | BIGINT (PK)   | Auto-incremented primary key        |
| `original_url`| VARCHAR       | The full original URL               |
| `short_url`   | VARCHAR       | The Base62-encoded short code       |
| `created_at`  | DATETIME      | Timestamp when the URL was created  |
| `click_count` | BIGINT        | Number of times the link was visited|

> The schema is auto-managed by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🔧 Configuration

Located in `src/main/resources/application.properties`:

```properties
spring.application.name=url-shortener

spring.datasource.url=jdbc:mysql://localhost:3307/urlshortener
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

To use a different database or port, update these values and adjust the `docker-compose.yml` accordingly.

---

## 🛑 Stopping the Application

To stop the Docker MySQL container:

```bash
docker-compose down
```

To stop and remove volumes (wipes all data):

```bash
docker-compose down -v
```

