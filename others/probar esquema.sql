-- timezone
SELECT CURRENT_TIMESTAMP(), NOW(), UTC_TIMESTAMP();
SELECT @@global.time_zone, @@session.time_zone;
SET GLOBAL time_zone = '+00:00'; -- 'SYSTEM'
SET SESSION time_zone = '+00:00'; -- 'SYSTEM'
SELECT CURRENT_TIMESTAMP(), NOW(), UTC_TIMESTAMP();

-- ver tabla
SHOW CREATE TABLE clientes;

-- ver datos
SELECT * FROM clientes;

-- probar procedimiento
CALL ContarClientesP('a', @total);
SELECT @total as totalP;

-- probar función
SELECT ContarClientesF('a') as totalF;
