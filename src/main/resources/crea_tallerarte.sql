DROP DATABASE IF EXISTS tallerarte;
CREATE DATABASE tallerarte CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

DROP USER IF EXISTS 'usuario_tallerarte'@'%';
CREATE USER 'usuario_tallerarte'@'%' IDENTIFIED BY 'CambiarEstaClave';
GRANT ALL PRIVILEGES ON tallerarte.* TO 'usuario_tallerarte'@'%';
FLUSH PRIVILEGES;

USE tallerarte;

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(100) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
) ENGINE=InnoDB;

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE taller (
    id_taller INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(10,2) NOT NULL,
    cupos INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    lugar VARCHAR(100),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_categoria INT NOT NULL,
    id_instructor INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_instructor) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- Modulo de reservas
CREATE TABLE reserva (
    id_reserva    INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    INT NOT NULL,
    id_taller     INT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    estado        VARCHAR(20) NOT NULL DEFAULT 'Confirmada',
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    CONSTRAINT fk_reserva_taller  FOREIGN KEY (id_taller)  REFERENCES taller (id_taller),
    INDEX idx_reserva_estado (estado)
) ENGINE=InnoDB;

INSERT INTO rol (nombre) VALUES
('ROLE_CLIENTE'),
('ROLE_INSTRUCTOR'),
('ROLE_ADMINISTRADOR');

-- Usuarios de prueba (uno por rol)
-- Admin: admin@tallerarte.cr / Admin2026
-- Instructor: instructor@tallerarte.cr / Instructor2026
-- Cliente: cliente@tallerarte.cr / Cliente2026
INSERT INTO usuario (nombre, correo, telefono, contrasena, activo, id_rol) VALUES
('Andrea Castro', 'admin@tallerarte.cr', '88881111',
 '$2a$10$0RqoPj9V2QYvD3KjpAgqvek.Vhxu.GTT.sZJ8kOPxxz8RGclgc40.', 1,
 (SELECT id_rol FROM rol WHERE nombre = 'ROLE_ADMINISTRADOR')),
('Lucía Solano', 'instructor@tallerarte.cr', '88882222',
 '$2a$10$njZFTwiDvhc00C3aPuv5huLy5KNl80m2PkuNG0.52g./B3ij/.Nxi', 1,
 (SELECT id_rol FROM rol WHERE nombre = 'ROLE_INSTRUCTOR')),
('Valeria Mora', 'cliente@tallerarte.cr', '88883333',
 '$2a$10$0tbaX4aMdrxIpI1xLeU22.NgpNvWP4CV.rhGiykx.qgaQVXTRqZT2', 1,
 (SELECT id_rol FROM rol WHERE nombre = 'ROLE_CLIENTE'));

-- Datos de ejemplo para probar (opcionales; borra este bloque si ya tienes los tuyos)
INSERT INTO categoria (nombre, descripcion) VALUES
('Cerámica', 'Modelado y torno'),
('Fotografía', 'Composición y luz');

INSERT INTO taller (nombre, descripcion, precio, cupos, fecha_hora, lugar, activo, id_categoria, id_instructor) VALUES
('Cerámica para principiantes', 'Introducción al modelado en arcilla', 25000.00, 5,
 DATE_ADD(NOW(), INTERVAL 7 DAY), 'Sede Heredia', 1,
 (SELECT id_categoria FROM categoria WHERE nombre = 'Cerámica'),
 (SELECT id_usuario FROM usuario WHERE correo = 'instructor@tallerarte.cr')),
('Fotografía con celular', 'Composición y luz natural', 18000.00, 1,
 DATE_ADD(NOW(), INTERVAL 10 DAY), 'Sede Heredia', 1,
 (SELECT id_categoria FROM categoria WHERE nombre = 'Fotografía'),
 (SELECT id_usuario FROM usuario WHERE correo = 'instructor@tallerarte.cr'));