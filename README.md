# Devsu - Sistema Bancario con Microservicios

Sistema de gestión bancaria basado en arquitectura de microservicios con **Spring Boot 4**, comunicación asíncrona mediante **Apache Kafka** y persistencia en **PostgreSQL**.

---

## Arquitectura General

```
┌──────────────────┐       Kafka Topics        ┌──────────────────┐
│  client-manager  │ ──────────────────────────>│ account-manager  │
│     :8021        │  cliente.creado             │     :8022        │
│                  │  cliente.actualizado        │                  │
│  - Clientes      │  cliente.eliminado          │  - Cuentas       │
│  - Personas      │                             │  - Movimientos   │
│                  │                             │  - Reportes      │
└──────┬───────────┘                             └──────┬───────────┘
       │                                                │
       ▼                                                ▼
  PostgreSQL                                       PostgreSQL
  devsu_client                                     devsu_account
```

Los microservicios se comunican de forma asíncrona a través de Kafka. Cuando un cliente se crea, actualiza o elimina en `client-manager`, se publica un evento que `account-manager` consume para mantener una referencia local sincronizada del cliente (`t_client_referencia`).

---

## Stack Tecnológico

| Tecnología              | Versión    |
|-------------------------|------------|
| Java                    | 21         |
| Spring Boot             | 4.0.3      |
| Spring Cloud            | 2025.1.0   |
| Apache Kafka            | (vía Spring Kafka) |
| PostgreSQL              | 15+        |
| Liquibase               | (vía Spring Boot)  |
| SpringDoc OpenAPI       | 3.0.2      |
| Lombok                  | (vía Spring Boot)  |
| Maven                   | 3.9+       |

---

## Microservicios

### 1. client-manager (Puerto 8021)

Gestión del ciclo de vida de clientes. Hereda de una entidad `Persona` (nombre, género, edad, identificación, dirección, teléfono) y agrega campos propios del cliente (estado, contraseña).

**Endpoints:**

| Método   | Ruta              | Descripción                      |
|----------|--------------------|----------------------------------|
| `POST`   | `/clientes`        | Crear un nuevo cliente           |
| `GET`    | `/clientes`        | Listar clientes (paginado)       |
| `PATCH`  | `/clientes/{id}`   | Actualizar un cliente            |
| `DELETE` | `/clientes/{id}`   | Eliminar un cliente              |

**Eventos Kafka publicados:**
- `cliente.creado` — al crear un cliente
- `cliente.actualizado` — al actualizar un cliente
- `cliente.eliminado` — al eliminar un cliente

**Base de datos:** `devsu_client`

**Swagger UI:** http://localhost:8021/swagger-ui/index.html

---

### 2. account-manager (Puerto 8022)

Gestión de cuentas bancarias, movimientos financieros (depósitos/retiros) y generación de reportes de estado de cuenta.

**Endpoints - Cuentas:**

| Método   | Ruta                  | Descripción                              |
|----------|-----------------------|------------------------------------------|
| `POST`   | `/cuentas`            | Crear cuenta (genera nro. UUID)          |
| `GET`    | `/cuentas`            | Listar todas las cuentas                 |
| `GET`    | `/cuentas/{id}`       | Obtener cuenta por ID                    |
| `PATCH`  | `/cuentas/{id}`       | Actualizar tipo y estado de cuenta       |
| `GET`    | `/cuentas/reportes`   | Reporte de estado de cuenta por periodo  |

**Endpoints - Movimientos:**

| Método   | Ruta                        | Descripción                          |
|----------|-----------------------------|--------------------------------------|
| `POST`   | `/movimientos`              | Registrar depósito o retiro          |
| `GET`    | `/movimientos/{nroCuenta}`  | Consultar movimientos por cuenta     |

**Eventos Kafka consumidos:**
- `cliente.creado` — sincroniza referencia local del cliente
- `cliente.actualizado` — actualiza referencia local
- `cliente.eliminado` — elimina referencia local

**Base de datos:** `devsu_account`

**Swagger UI:** http://localhost:8022/swagger-ui/index.html

---

## Modelo de Datos

### client-manager

```
t_client
├── id              BIGINT (PK, auto-increment)
├── nombres         VARCHAR(200)
├── genero          VARCHAR(1)    — M, F, O
├── edad            INTEGER
├── identificacion  VARCHAR(20)   — Único
├── direccion       TEXT
├── telefono        TEXT
├── estado          INTEGER       — 0: Activo, 1: Inactivo
└── password        VARCHAR(64)
```

### account-manager

```
t_client_referencia
├── client_id       BIGINT (PK)
├── identificacion  VARCHAR(15)
├── nombres         VARCHAR(250)
└── estado          INTEGER       — 0: Activo, 1: Inactivo

t_cuenta
├── id              BIGINT (PK, auto-increment)
├── client_id       BIGINT (FK → t_client_referencia)
├── nro_cuenta      VARCHAR(100)  — UUID generado
├── tipo_cuenta     INTEGER       — 0: Ahorro, 1: Corriente
├── saldo_inicial   NUMERIC(19,4)
├── saldo_disponible NUMERIC(19,4)
└── estado          INTEGER       — 0: Activo, 1: Inactivo

t_movimiento
├── id              BIGINT (PK, auto-increment)
├── cuenta_id       BIGINT (FK → t_cuenta)
├── fecha_mov       TIMESTAMP
├── tipo_mov        INTEGER       — 0: Depósito, 1: Retiro
├── valor           NUMERIC(19,4)
└── saldo           NUMERIC(19,4)
```

---

## Requisitos Previos

### Opción A — Docker (recomendado)

- **Docker** y **Docker Compose** instalados

### Opción B — Ejecución local

- **Java 21** (JDK)
- **Apache Kafka** ejecutándose en `localhost:9092`
- **PostgreSQL** ejecutándose en `localhost:5432`
- **Maven 3.9+**

---

## Ejecución con Docker Compose (recomendado)

Esta es la forma más sencilla de levantar todo el sistema. Docker Compose se encarga de crear las bases de datos, iniciar Kafka y levantar ambos microservicios automáticamente.

> **Nota:** No es necesario ejecutar `BaseDatos.sql` ni crear tablas manualmente. **Liquibase** se ejecuta automáticamente al iniciar cada microservicio y crea todas las tablas necesarias. El archivo `BaseDatos.sql` se incluye únicamente como referencia del esquema.

### Construir y levantar todos los servicios

```bash
docker compose up --build
```

### Levantar en segundo plano (modo detached)

```bash
docker compose up --build -d
```

### Ver los logs en modo detached

```bash
docker compose logs -f
```

### Ver logs de un servicio específico

```bash
docker compose logs -f client-manager
docker compose logs -f account-manager
```

### Detener todos los servicios

```bash
docker compose down
```

### Detener y eliminar volúmenes (limpia la base de datos)

```bash
docker compose down -v
```

### Reconstruir un servicio específico

```bash
docker compose up --build client-manager
docker compose up --build account-manager
```

Al ejecutar `docker compose up --build`, se levantan automáticamente:

| Servicio           | Puerto | Descripción                              |
|--------------------|--------|------------------------------------------|
| **postgres**       | 5432   | PostgreSQL 16 — crea `devsu_client` y `devsu_account` al inicio |
| **kafka**          | 9092   | Apache Kafka (modo KRaft, sin Zookeeper) |
| **client-manager** | 8021   | Microservicio de gestión de clientes     |
| **account-manager**| 8022   | Microservicio de cuentas y movimientos   |

---

## Ejecución Local (sin Docker)

### 1. Crear las bases de datos en PostgreSQL

```sql
CREATE DATABASE devsu_client;
CREATE DATABASE devsu_account;
```

### 2. Iniciar Kafka

```bash
docker run -d --name kafka \
  -p 9092:9092 \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  apache/kafka:latest
```

### 3. Iniciar los microservicios

```bash
# Terminal 1 - Client Manager
cd client-manager
mvn spring-boot:run

# Terminal 2 - Account Manager
cd account-manager
mvn spring-boot:run
```

### Variables de entorno (opcionales)

Ambos proyectos usan valores por defecto que se pueden sobrescribir:

| Variable                         | Valor por defecto              | Descripción              |
|----------------------------------|--------------------------------|--------------------------|
| `DB_HOST`                        | `localhost`                    | Host de PostgreSQL       |
| `DB_PORT`                        | `5432`                         | Puerto de PostgreSQL     |
| `DB_NAME`                        | `devsu_client` / `devsu_account` | Nombre de la BD        |
| `DB_USERNAME`                    | `postgres`                     | Usuario de BD            |
| `DB_PASSWORD`                    | `h4ckm3`                       | Contraseña de BD         |
| `PORT`                           | `8021` / `8022`                | Puerto del servicio      |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092`               | Dirección de Kafka       |

---

## Pruebas

### Pruebas unitarias (client-manager)

```bash
cd client-manager
mvn test
```

### Pruebas de integración (account-manager)

```bash
cd account-manager
mvn test -Dtest="com.devsu.integration.*"
```

Las pruebas de integración usan **H2 en memoria** (modo PostgreSQL), no requieren base de datos externa ni Kafka.

---

## Documentación API (Swagger)

Con los microservicios en ejecución, accede a la interfaz interactiva de Swagger UI:

| Servicio         | URL                                          |
|------------------|----------------------------------------------|
| client-manager   | http://localhost:8021/swagger-ui/index.html  |
| account-manager  | http://localhost:8022/swagger-ui/index.html  |

---

## Códigos de Error

### client-manager

| Código | HTTP Status | Descripción                                  |
|--------|-------------|----------------------------------------------|
| ER001  | 500         | Error interno del servidor                   |
| ER002  | 400         | No es posible leer el mensaje                |
| ER003  | 400         | Argumento del método no válido               |
| ER004  | 400         | Falta encabezado de solicitud                |
| ER005  | 404         | Recurso no encontrado                        |
| ER006  | 500         | Error de acceso a datos                      |
| ER007  | 400         | No existe el cliente                         |
| ER008  | 400         | No se puede eliminar (registros asociados)   |
| ER009  | 400         | La identificación ya existe                  |

### account-manager

| Código | HTTP Status | Descripción                                  |
|--------|-------------|----------------------------------------------|
| ER001  | 500         | Error interno del servidor                   |
| ER002  | 400         | No es posible leer el mensaje                |
| ER003  | 400         | Argumento del método no válido               |
| ER004  | 400         | Falta encabezado de solicitud                |
| ER005  | 404         | Recurso no encontrado                        |
| ER006  | 500         | Error de acceso a datos                      |
| ER007  | 400         | No existe el cliente                         |
| ER008  | 400         | No se puede eliminar (registros asociados)   |
| ER009  | 400         | No existe la cuenta                          |
| ER010  | 400         | Saldo insuficiente                           |
| ER011  | 400         | Tipo de movimiento inválido                  |
| ER012  | 400         | Valor del movimiento inválido                |
| ER013  | 400         | Fecha desde mayor que fecha hasta            |

---

## Entregables

El proyecto incluye los siguientes entregables según los requerimientos:

| Archivo / Recurso | Descripción |
|---|---|
| `BaseDatos.sql` | Script de referencia con la estructura de bases de datos, tablas y esquema completo. **No es necesario ejecutarlo manualmente**: Liquibase crea las tablas automáticamente al iniciar cada microservicio |
| `Devsu.postman_collection.json` | Colección de Postman con todos los endpoints para validación |
| `docker-compose.yml` | Orquestación completa: PostgreSQL + Kafka + ambos microservicios |
| `client-manager/Dockerfile` | Dockerfile multi-stage para el servicio de clientes |
| `account-manager/Dockerfile` | Dockerfile multi-stage para el servicio de cuentas |
| `account-manager/src/test/` | Pruebas de integración con H2 en memoria |
| `client-manager/src/test/` | Pruebas unitarias del servicio de clientes |
| Swagger UI | Documentación interactiva en cada microservicio |

### Importar colección de Postman

1. Abrir Postman
2. Click en **Import**
3. Seleccionar el archivo `Devsu.postman_collection.json`
4. Las variables `base_url_client` y `base_url_account` apuntan a `localhost:8021` y `localhost:8022` respectivamente
5. Para los endpoints de movimientos, reemplazar `{{nro_cuenta}}` con el UUID real que se obtiene al crear una cuenta

### Flujo de prueba recomendado en Postman

1. **Crear Cliente** → `POST /clientes` (client-manager)
2. **Crear Cuenta** → `POST /cuentas` con el `clientId` del paso 1 (account-manager)
3. **Registrar Depósito** → `POST /movimientos` con el `nroCuenta` del paso 2
4. **Registrar Retiro** → `POST /movimientos` con tipoMovimiento = 1
5. **Consultar Movimientos** → `GET /movimientos/{nroCuenta}`
6. **Reporte Estado de Cuenta** → `GET /cuentas/reportes?clientId=1&desde=2025-01-01&hasta=2026-12-31`

---

## Estructura del Proyecto

```
prueba-practica/
├── README.md
├── BaseDatos.sql
├── Devsu.postman_collection.json
├── docker-compose.yml
├── docker/
│   └── init-databases.sh
├── client-manager/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/devsu/
│       │   ├── app/rest/controller/       → ClientController (Swagger)
│       │   ├── domain/entity/             → Persona, Cliente
│       │   ├── domain/service/            → ClienteService
│       │   └── domain/provider/           → Kafka Producer
│       └── test/java/com/devsu/
│           └── domain/service/impl/       → Pruebas unitarias
└── account-manager/
    ├── Dockerfile
    ├── pom.xml
    └── src/
        ├── main/java/com/devsu/
        │   ├── app/rest/controller/        → CuentaController, MovimientoController (Swagger)
        │   ├── domain/entity/              → Cuenta, Movimiento, ClienteRef
        │   ├── domain/service/             → CuentaService, MovimientoService, ReporteService
        │   └── app/messaging/              → Kafka Consumer
        └── test/java/com/devsu/
            └── integration/                → Pruebas de integración
```

---

## Autor

Ramiro Ochoa
