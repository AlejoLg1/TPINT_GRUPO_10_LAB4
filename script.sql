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
    numero VARCHAR(5) NOT NULL,
    localidad VARCHAR(50) NOT NULL,
    provincia VARCHAR(50) NOT NULL
);

-- Tabla de clientes (solo si el usuario es cliente)
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL,
    id_direccion INT NOT NULL,
    dni VARCHAR(15) UNIQUE NOT NULL,
    cuil VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    sexo ENUM('M', 'F', 'X') NOT NULL,
    nacionalidad VARCHAR(50),
    fecha_nacimiento DATE NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    estado BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_direccion) REFERENCES Direccion(id_direccion)
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
    fecha_pago DATE NOT NULL,
    estado ENUM('PENDIENTE', 'PAGADO', 'ATRASADO') NOT NULL DEFAULT 'PENDIENTE',
    
    FOREIGN KEY (nro_cuenta) REFERENCES Cuenta(nro_cuenta),
    FOREIGN KEY (id_prestamo) REFERENCES Prestamo(id_prestamo)
);


