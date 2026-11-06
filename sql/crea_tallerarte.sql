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

INSERT INTO rol (nombre) VALUES
('ROLE_CLIENTE'),
('ROLE_INSTRUCTOR'),
('ROLE_ADMINISTRADOR');

