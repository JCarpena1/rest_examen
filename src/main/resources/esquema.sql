-- crear database
DROP SCHEMA IF EXISTS restful;
CREATE SCHEMA restful;
USE restful;

-- crear tabla
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

-- insertar datos
TRUNCATE TABLE clientes;
INSERT INTO clientes (nombre, edad, direccion, fecha_nacimiento) values ('Hana', 18, 'Su Casa, 1', '2000-01-01 12:00:00');
INSERT INTO clientes (nombre, edad, direccion) values ('Belén', 19, 'Su Casa, 2');
INSERT INTO clientes (nombre, edad) values ('Carmen', 20);
INSERT INTO clientes (nombre) values ('Diana');
UPDATE clientes SET nombre = 'Ana' WHERE id = 1;

-- crear procedimiento
DROP PROCEDURE IF EXISTS ContarClientesP;
CREATE PROCEDURE ContarClientesP(IN search CHAR(45), OUT total INT)
BEGIN
	SELECT COUNT(*) INTO total FROM clientes
	WHERE nombre like CONCAT('%', search, '%');
END;

-- crear función
DROP FUNCTION IF EXISTS ContarClientesF;
CREATE FUNCTION ContarClientesF(search CHAR(45)) RETURNS INT DETERMINISTIC READS SQL DATA
BEGIN
	SELECT COUNT(*) INTO @total FROM clientes
        WHERE nombre like CONCAT('%', search, '%');
    
	RETURN @total;
END;
