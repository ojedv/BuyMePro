DROP DATABASE IF EXISTS BuyMeProyect;
CREATE DATABASE BuyMeProyect;
USE BuyMeProyect;

CREATE TABLE usuario(
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(25),
    contraseña VARCHAR(64),
    nombre VARCHAR(20),
    apellidos VARCHAR(35),
    telefono VARCHAR(15),
    correo VARCHAR(30)
);

CREATE TABLE supermercado(
    id_supermercado INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30)
);

CREATE TABLE grupo(
    id_grupo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30)
);

CREATE TABLE usuario_grupo (
    id_usuario INT,
    id_grupo INT,
    PRIMARY KEY (id_usuario, id_grupo),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo)
);

CREATE TABLE grupo_supermercado (
    id_grupo INT,
    id_supermercado INT,
    PRIMARY KEY (id_grupo, id_supermercado),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo),
    FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)
);

CREATE TABLE producto(
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30),
    precio FLOAT,
    id_supermercado INT,
    FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)
);

CREATE TABLE producto_supermercado (
id_supermercado INT, 
id_producto INT,
FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)

);

CREATE TABLE pedido(
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario_solicitante INT NOT NULL,
    id_usuario_comprador INT NULL, -- NULL cuando no hay comprador asignado
    id_supermercado INT NOT NULL,
    estado ENUM('PENDIENTE', 'ASIGNADO', 'COMPLETADO', 'CANCELADO') DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_asignacion TIMESTAMP NULL,
    fecha_completado TIMESTAMP NULL,
    total DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_usuario_comprador) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)
);

CREATE TABLE pedido_producto(
    id_pedido_producto INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT DEFAULT 1,
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- Insertar supermercados
INSERT INTO supermercado (nombre) 
VALUES 
('Mercadona'),
('Lidl'),
('Más'),
('Dia');

-- Insertar productos para Mercadona (id_supermercado = 1)
INSERT INTO producto (nombre, precio, id_supermercado) VALUES 
('Leche Entera 1L', 1.20, 1),
('Pan de Molde', 1.50, 1),
('Huevos Docena', 2.30, 1),
('Arroz 1kg', 1.80, 1),
('Aceite de Oliva 1L', 4.50, 1),
('Pollo Entero', 3.20, 1),
('Tomates 1kg', 2.10, 1),
('Pasta Espaguetis', 1.40, 1);

-- Insertar productos para Lidl (id_supermercado = 2)
INSERT INTO producto (nombre, precio, id_supermercado) VALUES 
('Leche Semidesnatada 1L', 1.15, 2),
('Pan Integral', 1.60, 2),
('Yogur Natural Pack 4', 2.00, 2),
('Cereales 500g', 3.20, 2),
('Jamón Cocido 200g', 2.80, 2),
('Queso Manchego 250g', 4.20, 2),
('Plátanos 1kg', 1.90, 2),
('Atún Lata Pack 3', 3.50, 2);

-- Insertar productos para Dia (id_supermercado = 3)
INSERT INTO producto (nombre, precio, id_supermercado) VALUES 
('Leche Desnatada 1L', 1.10, 3),
('Galletas María', 1.20, 3),
('Café Molido 250g', 2.80, 3),
('Azúcar 1kg', 1.50, 3),
('Mantequilla 250g', 2.40, 3),
('Salmón Ahumado 100g', 5.20, 3),
('Naranjas 2kg', 2.60, 3),
('Detergente Líquido 1L', 3.80, 3);

-- Insertar productos para Mas (id_supermercado = 4)
INSERT INTO producto (nombre, precio, id_supermercado) VALUES 
('Agua Mineral 6x1.5L', 2.40, 4),
('Cerveza Pack 6', 4.80, 4),
('Pizza Congelada', 3.20, 4),
('Helado 1L', 3.80, 4),
('Chocolate Negro 100g', 2.20, 4),
('Patatas Fritas 150g', 1.80, 4),
('Manzanas 1.5kg', 2.90, 4),
('Papel Higiénico Pack 6', 4.50, 4);

-- Consultas de comprobación
SELECT * FROM usuario;
SELECT * FROM producto;
SELECT * FROM supermercado;
SELECT * FROM pedido_producto;
