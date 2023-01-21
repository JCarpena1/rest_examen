SELECT CURRENT_TIMESTAMP(), NOW(), UTC_TIMESTAMP();
SELECT @@global.time_zone, @@session.time_zone;
SET GLOBAL time_zone = '+00:00'; -- 'SYSTEM'
SET SESSION time_zone = '+00:00'; -- 'SYSTEM'
SELECT CURRENT_TIMESTAMP(), NOW(), UTC_TIMESTAMP();

DROP SCHEMA IF EXISTS restful;
CREATE SCHEMA restful;
USE restful;

DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
  id SERIAL,
  nombre VARCHAR(45) NOT NULL,
  edad TINYINT NULL,
  direccion VARCHAR(45) NULL,
  fecha_nacimiento DATETIME NUll,
  created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
  updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`)
);

SHOW CREATE TABLE clientes;

TRUNCATE TABLE clientes;
INSERT INTO clientes (nombre, edad, direccion, fecha_nacimiento) values ('Hana', 18, 'Su Casa, 1', '2000-01-01 12:00:00');
INSERT INTO clientes (nombre, edad, direccion) values ('Belén', 19, 'Su Casa, 2');
INSERT INTO clientes (nombre, edad) values ('Carmen', 20);
INSERT INTO clientes (nombre) values ('Diana');

UPDATE clientes SET nombre = 'Ana' WHERE id = 1;

SELECT * FROM clientes;
