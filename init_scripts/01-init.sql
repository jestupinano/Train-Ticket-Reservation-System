-- 01-init.sql

-- Crear rol/usuario y BD (si vas a usar la BD por defecto que crea el entrypoint, omite CREATE DATABASE)
-- El entrypoint de la imagen oficial ya crea la BD y el superuser con las env vars,
-- así que aquí solo creamos el esquema y damos permisos.

CREATE SCHEMA IF NOT EXISTS reservation AUTHORIZATION reservation;

-- (Si NO creaste el usuario con variables de entorno, descomenta esto)
-- CREATE ROLE reservation WITH LOGIN PASSWORD 'manager';
-- CREATE DATABASE reservation OWNER reservation;

-- Asegúrate de estar en el esquema y la BD correcta
SET search_path TO reservation, public;

-- Tablas
CREATE TABLE customer (
    mailid VARCHAR(40) PRIMARY KEY,
    pword  VARCHAR(20) NOT NULL,
    fname  VARCHAR(20) NOT NULL,
    lname  VARCHAR(20),
    addr   VARCHAR(100),
    phno   NUMERIC(12,0) NOT NULL
);

CREATE TABLE admin (
    mailid VARCHAR(40) PRIMARY KEY,
    pword  VARCHAR(20) NOT NULL,
    fname  VARCHAR(20) NOT NULL,
    lname  VARCHAR(20),
    addr   VARCHAR(100),
    phno   NUMERIC(12,0) NOT NULL
);

CREATE TABLE train (
    tr_no    NUMERIC(10,0) PRIMARY KEY,
    tr_name  VARCHAR(70)   NOT NULL,
    from_stn VARCHAR(20)   NOT NULL,
    to_stn   VARCHAR(20)   NOT NULL,
    seats    NUMERIC(4,0)  NOT NULL,
    fare     NUMERIC(6,2)  NOT NULL
);

CREATE TABLE history (
    transid  VARCHAR(36) PRIMARY KEY,
    mailid   VARCHAR(40) REFERENCES customer(mailid),
    tr_no    NUMERIC(10,0),
    date     DATE,
    from_stn VARCHAR(20) NOT NULL,
    to_stn   VARCHAR(20) NOT NULL,
    seats    NUMERIC(3,0) NOT NULL,
    amount   NUMERIC(8,2) NOT NULL
);

-- Datos
INSERT INTO admin VALUES
('admin@demo.com','admin','System','Admin','Demo Address 123 colony','9874561230');

INSERT INTO customer VALUES
('shashi@demo.com','shashi','Shashi','Raj','Kolkata, West Bengal',954745222);

INSERT INTO train VALUES
(10001,'JODHPUR EXP','HOWRAH','JODHPUR', 152, 490.50),
(10002,'YAMUNA EXP','GAYA','DELHI', 52, 550.50),
(10003,'NILANCHAL EXP','GAYA','HOWRAH', 92, 451),
(10004,'JAN SATABDI EXP','RANCHI','PATNA', 182, 550),
(10005,'GANGE EXP','MUMBAI','KERALA', 12, 945),
(10006,'GARIB RATH EXP','PATNA','DELHI', 1, 1450.75);

INSERT INTO history VALUES
('BBC374-NSDF-4673','shashi@demo.com',10001,DATE '2024-02-02','HOWRAH','JODHPUR',2, 981),
('BBC375-NSDF-4675','shashi@demo.com',10004,DATE '2024-01-12','RANCHI','PATNA',1, 550),
('BBC373-NSDF-4674','shashi@demo.com',10006,DATE '2024-07-22','PATNA','DELHI',3, 4352.25);
