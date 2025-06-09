-- ========================================
-- Tabla Usuario (padre)
-- ========================================
CREATE TABLE Usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    tipo ENUM('cliente', 'admin') NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- ========================================
-- Tabla Cliente (subtipo de Usuario)
-- ========================================
CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT UNIQUE NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    cuil VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    sexo VARCHAR(10),
    nacionalidad VARCHAR(50),
    fecha_nacimiento DATE,
    direccion VARCHAR(100),
    localidad VARCHAR(50),
    provincia VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

-- ========================================
-- Tabla Administrador (subtipo de Usuario)
-- ========================================
CREATE TABLE Administrador (
    id_admin INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);