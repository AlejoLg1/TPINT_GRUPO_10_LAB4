CREATE DATABASE banco;
USE banco;

-- Tabla de usuarios (tanto clientes como personal del banco)
CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    clave VARCHAR(100) NOT NULL,
    tipo ENUM('BANCARIO', 'CLIENTE') NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    estado BOOLEAN DEFAULT TRUE
);

-- Tabla de direcciones
CREATE TABLE Direccion (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL
);

CREATE TABLE Provincia(
	id_provincia INT AUTO_INCREMENT PRIMARY KEY,
	nombre_provincia varchar(50) not null
);

CREATE TABLE Localidad(
	id_localidad INT AUTO_INCREMENT PRIMARY KEY,
    id_provincia INT not null,
	nombre_localidad varchar(50) not null,
    
	FOREIGN KEY (id_provincia) REFERENCES Provincia(id_provincia)
);

-- Tabla de clientes (solo si el usuario es cliente)
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL,
    id_direccion INT NOT NULL,
    id_provincia int not null,
    id_localidad int not null,
    dni VARCHAR(15) UNIQUE NOT NULL,
    cuil VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    sexo ENUM('M', 'F', 'X') NOT NULL,
    nacionalidad VARCHAR(50),
    fecha_nacimiento DATE NOT NULL,
    fecha_alta DATE NOT NULL DEFAULT (CURDATE()),
    fecha_baja DATE DEFAULT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    estado BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_direccion) REFERENCES Direccion(id_direccion),
    FOREIGN KEY (id_provincia) REFERENCES Provincia(id_provincia),
    FOREIGN KEY (id_localidad) REFERENCES Localidad(id_localidad)
);

-- Tabla de tipos de cuenta 
CREATE TABLE Tipo_cuenta (
    id_tipo_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

-- Tabla de cuentas
CREATE TABLE Cuenta (
    nro_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_tipo_cuenta INT NOT NULL,
    cbu VARCHAR(22) UNIQUE NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    saldo DECIMAL(12,2) DEFAULT 10000.00,
    estado BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_tipo_cuenta) REFERENCES Tipo_cuenta(id_tipo_cuenta)
);

-- Tabla de tipos de movimiento
CREATE TABLE Tipo_movimiento (
    id_tipo_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

-- Tabla de movimientos
CREATE TABLE Movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    nro_cuenta INT NOT NULL,
    id_tipo_movimiento INT NOT NULL,
    fecha DATETIME NOT NULL,
    detalle VARCHAR(100),
    importe DECIMAL(12,2) NOT NULL,
    
    FOREIGN KEY (nro_cuenta) REFERENCES Cuenta(nro_cuenta),
    FOREIGN KEY (id_tipo_movimiento) REFERENCES Tipo_movimiento(id_tipo_movimiento)
);

-- Tabla de transferencias
CREATE TABLE Transferencia (
    id_transferencia INT AUTO_INCREMENT PRIMARY KEY,
    id_movimiento_salida INT NOT NULL UNIQUE,
    id_movimiento_entrada INT NOT NULL UNIQUE,
    importe DECIMAL(12,2) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
	FOREIGN KEY (id_movimiento_salida) REFERENCES Movimiento(id_movimiento),
    FOREIGN KEY (id_movimiento_entrada) REFERENCES Movimiento(id_movimiento)
);


-- Tabla de préstamos
CREATE TABLE Prestamo (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    nro_cuenta INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    importe_solicitado DECIMAL(12,2) NOT NULL,
    cantidad_cuotas INT NOT NULL,
    monto_cuota DECIMAL(12,2) NOT NULL,
    autorizacion BOOLEAN DEFAULT FALSE,
    estado VARCHAR(50) NOT NULL,
    cuotas_pagadas INT NOT NULL DEFAULT 0,
    
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (nro_cuenta) REFERENCES Cuenta(nro_cuenta)
);


-- Tabla de cuotas de préstamos
CREATE TABLE Cuota (
    id_cuota INT AUTO_INCREMENT PRIMARY KEY,
    id_prestamo INT NOT NULL,
    nro_cuenta INT NOT NULL,
    numero_cuota INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_pago DATE NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('PENDIENTE', 'PAGADO', 'ATRASADO') NOT NULL DEFAULT 'PENDIENTE',
    
    FOREIGN KEY (nro_cuenta) REFERENCES Cuenta(nro_cuenta),
    FOREIGN KEY (id_prestamo) REFERENCES Prestamo(id_prestamo)
);



-- Usuario administrador bancario
INSERT INTO Usuario (nombre_usuario, clave, tipo, is_admin, estado)
VALUES ('admin', '1234', 'BANCARIO', TRUE, TRUE);

INSERT INTO Provincia (nombre_provincia) VALUES
('Buenos Aires'),
('Catamarca'),
( 'Chaco'),
( 'Chubut'),
( 'Córdoba'),
( 'Corrientes'),
( 'Entre Ríos'),
( 'Formosa'),
( 'Jujuy'),
('La Pampa'),
('La Rioja'),
( 'Mendoza'),
( 'Misiones'),
( 'Neuquén'),
( 'Río Negro'),
( 'Salta'),
( 'San Juan'),
( 'San Luis'),
( 'Santa Cruz'),
( 'Santa Fe'),
( 'Santiago del Estero'),
( 'Tierra del Fuego'),
( 'Tucumán'),
( 'Ciudad Autónoma de Buenos Aires');


INSERT INTO Localidad ( nombre_localidad, id_provincia) VALUES
('La Plata', 1),
( 'San Fernando del Valle de Catamarca', 2),
( 'Resistencia', 3),
( 'Rawson', 4),
( 'Córdoba', 5),
( 'Corrientes', 6),
( 'Paraná', 7),
( 'Formosa', 8),
( 'San Salvador de Jujuy', 9),
( 'Santa Rosa', 10),
( 'La Rioja', 11),
( 'Mendoza', 12),
( 'Posadas', 13),
( 'Neuquén', 14),
( 'Viedma', 15),
( 'Salta', 16),
( 'San Juan', 17),
( 'San Luis', 18),
( 'Río Gallegos', 19),
( 'Santa Fe', 20),
( 'Santiago del Estero', 21),
( 'Ushuaia', 22),
( 'San Miguel de Tucumán', 23),
( 'Ciudad Autónoma de Buenos Aires', 24),
('Avellaneda', 1),
('Bahía Blanca', 1),
('Banfield', 1),
('Berazategui', 1),
('Berisso', 1),
('Campana', 1),
('Chivilcoy', 1),
('Florencio Varela', 1),
('General Rodríguez', 1),
('Hurlingham', 1),
('Ituzaingó', 1),
('José C. Paz', 1),
('Junín', 1),
('La Matanza', 1),
('Lanús', 1),
('Lomas de Zamora', 1),
('Mar del Plata', 1),
('Merlo', 1),
('Moreno', 1),
('Morón', 1),
('Olavarría', 1),
('Pergamino', 1),
('Pilar', 1),
('Quilmes', 1),
('San Fernando', 1),
('San Isidro', 1),
('San Justo', 1),
('San Martín', 1),
('San Miguel', 1),
('Tandil', 1),
('Tigre', 1),
('Tres de Febrero', 1),
('Vicente López', 1),
('Zárate', 1);





-- Insert Tipos de Cuentas
INSERT INTO Tipo_cuenta (descripcion) VALUES ('Caja de ahorro'), ('Cuenta corriente');
-- Insert Tipos de Movimiento

INSERT Tipo_movimiento (descripcion) VALUES ('ALTA CUENTA'), ('ALTA PRESTAMO'), ('PAGO PRESTAMO'), ('TRANSFERENCIA');

-- Procedure Insertar Cliente

DELIMITER //

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarCliente`(
    IN p_nombre_usuario VARCHAR(50),
    IN p_clave VARCHAR(100),
    IN p_calle VARCHAR(100),
    IN p_numero VARCHAR(5),
    IN p_dni VARCHAR(15),
    IN p_cuil VARCHAR(20),
    IN p_nombre VARCHAR(50),
    IN p_apellido VARCHAR(50),
    IN p_sexo ENUM('M', 'F', 'X'),
    IN p_nacionalidad VARCHAR(50),
    IN p_fecha_nacimiento DATE,
    IN p_correo VARCHAR(100),
    IN p_telefono VARCHAR(20),
     IN p_id_provincia INT,
    IN p_id_localidad INT
)
BEGIN
    DECLARE v_id_usuario INT;
    DECLARE v_id_direccion INT;
    DECLARE existe_usuario INT DEFAULT 0;
    DECLARE existe_dni INT DEFAULT 0;
    DECLARE existe_cuil INT DEFAULT 0;

    -- Validar existencia de usuario, DNI y CUIL
    SELECT COUNT(*) INTO existe_usuario
    FROM Usuario
    WHERE nombre_usuario = p_nombre_usuario;

    SELECT COUNT(*) INTO existe_dni
    FROM Cliente
    WHERE dni = p_dni;

    SELECT COUNT(*) INTO existe_cuil
    FROM Cliente
    WHERE cuil = p_cuil;

    IF existe_usuario > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El nombre de usuario ya existe.';
    END IF;

    IF existe_dni > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El DNI ya está registrado.';
    END IF;

    IF existe_cuil > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El CUIL ya está registrado.';
    END IF;

    -- Insertar en Usuario
    INSERT INTO Usuario (nombre_usuario, clave, tipo, is_admin, estado)
    VALUES (p_nombre_usuario, p_clave, 'CLIENTE', FALSE, TRUE);
    SET v_id_usuario = LAST_INSERT_ID();

    -- Insertar en Direccion
    INSERT INTO Direccion (calle, numero)
    VALUES (p_calle, p_numero);
    SET v_id_direccion = LAST_INSERT_ID();

    -- Insertar en Cliente
    INSERT INTO Cliente (
        dni, cuil, nombre, apellido, sexo, nacionalidad, fecha_nacimiento, correo, telefono,
        id_usuario, id_direccion, id_provincia, id_localidad, estado
    )
    VALUES (
        p_dni, p_cuil, p_nombre, p_apellido, p_sexo, p_nacionalidad, p_fecha_nacimiento, p_correo, p_telefono,
        v_id_usuario, v_id_direccion, p_id_provincia, p_id_localidad, TRUE
    );
END 

// DELIMITER ;


-- Procedure ListarCuentasPorCliente

DELIMITER //

CREATE PROCEDURE ListarCuentasPorCliente (IN p_id_cliente INT)
BEGIN
    SELECT 
        c.nro_cuenta,
        c.cbu,
        t.descripcion AS tipo_cuenta,
        c.fecha_creacion,
        c.saldo
    FROM 
        Cuenta c
        JOIN Tipo_cuenta t ON c.id_tipo_cuenta = t.id_tipo_cuenta
    WHERE 
        c.id_cliente = p_id_cliente AND c.estado = TRUE;
END //

DELIMITER ;

-- Procedure InsertarCuenta

DELIMITER //

CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertarCuenta`(
    IN p_id_cliente INT,
    IN p_id_tipo_cuenta INT,
    IN p_saldo DECIMAL(15,2),
    OUT p_nro_cuenta INT
)
BEGIN
    DECLARE nuevo_nro_cuenta INT;
    DECLARE nuevo_cbu VARCHAR(22);

    -- Insertar nueva cuenta con el id del tipo de cuenta
    INSERT INTO Cuenta (id_cliente, id_tipo_cuenta, saldo, fecha_creacion, estado, cbu)
    VALUES (p_id_cliente, p_id_tipo_cuenta, p_saldo, NOW(), TRUE, 'TEMP');

    -- Obtener el número de cuenta generado (auto_increment)
    SET nuevo_nro_cuenta = LAST_INSERT_ID();
    
    -- Registrar Movimiento
	INSERT INTO Movimiento (nro_cuenta, id_tipo_movimiento, fecha, detalle, importe)
    VALUES (nuevo_nro_cuenta, 1, NOW(), 'Alta de cuenta', p_saldo);

    -- Generar CBU ficticio de 22 dígitos
    SET nuevo_cbu = LPAD(CONCAT('310000', p_id_cliente, nuevo_nro_cuenta), 22, '0');

    -- Actualizar CBU generado
    UPDATE Cuenta
    SET cbu = nuevo_cbu
    WHERE nro_cuenta = nuevo_nro_cuenta;

    -- Devolver nro_cuenta generado
    SET p_nro_cuenta = nuevo_nro_cuenta;
END //

DELIMITER ;

CREATE VIEW DatosCliente AS
SELECT 
  C.id_cliente,
  C.id_usuario,
  C.dni,
  C.cuil,
  C.nombre,
  C.apellido,
  C.sexo,
  C.nacionalidad,
  C.fecha_nacimiento,
  D.id_direccion,
  D.calle,
  D.numero,
  l.id_localidad,
  l.nombre_localidad,
  p.id_provincia,
  p.nombre_provincia,
  C.correo,
  C.telefono,
  C.estado
FROM Cliente C
inner join Provincia p on p.id_provincia = c.id_provincia
inner join Localidad l on l.id_localidad = c.id_localidad
INNER JOIN Direccion D ON C.id_direccion = D.id_direccion

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_ejecutar_movimiento(
    IN p_nro_cuenta INT,
    IN p_id_tipo_movimiento INT,
    IN p_fecha DATETIME,
    IN p_detalle VARCHAR(100),
    IN p_importe DECIMAL(12,2),
    OUT p_id_movimiento INT
)
BEGIN
    DECLARE v_saldo_actual DECIMAL(12,2);

    -- Verificar saldo actual de la cuenta
    SELECT saldo INTO v_saldo_actual FROM Cuenta WHERE nro_cuenta = p_nro_cuenta;

    -- Validar saldo insuficiente si el movimiento es negativo
    IF p_importe < 0 AND ABS(p_importe) > v_saldo_actual THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo insuficiente.';
    END IF;

    -- Insertar movimiento
    INSERT INTO Movimiento (nro_cuenta, id_tipo_movimiento, fecha, detalle, importe)
    VALUES (p_nro_cuenta, p_id_tipo_movimiento, p_fecha, p_detalle, p_importe);

    -- Obtener el ID generado
    SET p_id_movimiento = LAST_INSERT_ID();

    -- Actualizar el saldo de la cuenta
    UPDATE Cuenta SET saldo = saldo + p_importe WHERE nro_cuenta = p_nro_cuenta;
END$$
DELIMITER ;


DELIMITER //

CREATE TRIGGER actualizar_fecha_baja_cliente
BEFORE UPDATE ON Cliente
FOR EACH ROW
BEGIN
    -- Si se cambia de activo a inactivo y no tiene fecha_baja
    IF NEW.estado = FALSE AND OLD.estado = TRUE AND NEW.fecha_baja IS NULL THEN
        SET NEW.fecha_baja = CURDATE();
    END IF;
END;
//

DELIMITER ;
