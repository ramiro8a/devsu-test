-- ============================================================
-- BaseDatos.sql
-- Script de creación de bases de datos, esquema y entidades
-- Proyecto: Devsu - Sistema Bancario con Microservicios
-- Autor: Ramiro Ochoa
-- ============================================================

-- ============================================================
-- 1. CREACIÓN DE BASES DE DATOS
-- ============================================================

CREATE DATABASE devsu_client;
CREATE DATABASE devsu_account;

-- ============================================================
-- 2. ESQUEMA: devsu_client
-- ============================================================

\c devsu_client;

-- Tabla: t_client
-- Almacena los datos de los clientes (hereda conceptualmente de Persona)
CREATE TABLE t_client (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombres         VARCHAR(250),
    genero          VARCHAR(5),
    edad            INTEGER DEFAULT 0,
    identificacion  VARCHAR(15),
    direccion       TEXT,
    telefono        VARCHAR(250),
    estado          INTEGER DEFAULT 0,       -- 0: Activo, 1: Inactivo
    password        VARCHAR(200)
);

COMMENT ON COLUMN t_client.estado IS '0: Activo, 1: Inactivo';

-- ============================================================
-- 3. ESQUEMA: devsu_account
-- ============================================================

\c devsu_account;

-- Tabla: t_client_referencia
-- Referencia local de clientes sincronizada vía Kafka desde client-manager
CREATE TABLE t_client_referencia (
    client_id       BIGINT PRIMARY KEY,
    identificacion  VARCHAR(15),
    nombres         VARCHAR(250),
    estado          INTEGER DEFAULT 0        -- 0: Activo, 1: Inactivo
);

COMMENT ON COLUMN t_client_referencia.estado IS '0: Activo, 1: Inactivo';

-- Tabla: t_cuenta
-- Cuentas bancarias asociadas a un cliente
CREATE TABLE t_cuenta (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id           BIGINT NOT NULL,
    nro_cuenta          VARCHAR(100),
    tipo_cuenta         INTEGER,              -- 0: Ahorro, 1: Corriente
    saldo_inicial       NUMERIC(19, 4),
    saldo_disponible    NUMERIC(19, 4),
    estado              INTEGER DEFAULT 0     -- 0: Activo, 1: Inactivo
);

COMMENT ON COLUMN t_cuenta.tipo_cuenta IS '0: Ahorro, 1: Corriente';
COMMENT ON COLUMN t_cuenta.estado IS '0: Activo, 1: Inactivo';

-- Tabla: t_movimiento
-- Movimientos financieros (depósitos y retiros) de cada cuenta
CREATE TABLE t_movimiento (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cuenta_id       BIGINT NOT NULL,
    fecha_mov       TIMESTAMP,
    tipo_mov        INTEGER,                  -- 0: Depósito, 1: Retiro
    valor           NUMERIC(19, 4),
    saldo           NUMERIC(19, 4),
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES t_cuenta(id)
);

COMMENT ON COLUMN t_movimiento.tipo_mov IS '0: Depósito, 1: Retiro';
