DROP DATABASE IF EXISTS BuyMeProyect;
CREATE DATABASE BuyMeProyect;
USE BuyMeProyect;

CREATE TABLE usuario(
id_usuario INT PRIMARY KEY AUTO_INCREMENT, -- HE PUESTO ID_USUARIO POR SI ALGUNA PERSONA NO TIENE DNI 
nickname VARCHAR(25), -- USUARIO Y CONTRASEÑA NOS SIRVEN PARA IDENTIFICAR A CADA PERSONA, A LA HORA DE INICIAR SESION SOLO SERAN REQUERIDOS ESTOS DOS PARAMETROS
contraseña VARCHAR(64),
nombre VARCHAR(20), -- DATOS DE REGISTRO, ESTOS DATOS NOS SIRVEN PARA TENER UN CONTROL SOBRE LAS PERSONAS QUE SE ALOJAN EN NUESTRA APP
apellidos VARCHAR(35),
telefono VARCHAR(15), -- ESTO NOS FACILITARA A LA HORA DE HACER LOS "RECADOS" EN CONTACTAR CON LA PERSONA CORRESPONDIENTE
correo VARCHAR(30) -- PODEMOS ACTUALIZAR A LOS USUARIOS DE LAS ACTUALIZACIONES, Y NOVEDADES DE NUESTRA APLICACION

);

CREATE TABLE supermercado(
id_supermercado INT PRIMARY KEY AUTO_INCREMENT, -- CADA SUPERMERCADO TENDRA SUS PROPIOS PRODUCTOS
nombre VARCHAR (30)

);

CREATE TABLE grupo(
id_grupo INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR (30)

);

CREATE TABLE usuario_grupo ( -- ESTA TABLA NOS PERMITE CONTROLAR LA RELACION ENTRE USUARIO Y GRUPO
  id_usuario INT,
  id_grupo INT,
  PRIMARY KEY (id_usuario, id_grupo),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo)
);

CREATE TABLE grupo_supermercado ( -- ESTA TABLA NOS PERMITE CONTROLAR LA RELACION ENTRE SUPERMERCADO Y GRUPO
  id_grupo INT,
  id_supermercado INT,
  PRIMARY KEY (id_grupo, id_supermercado),
  FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo),
  FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)
);


CREATE TABLE producto(
id_producto INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR (30),
precio FLOAT -- PONEMOS EL PRECIO PARA QUE APAREZCA AL SELECCIONAR EL PRODUCTO



);

CREATE TABLE producto_supermercado (
id_supermercado INT, 
id_producto INT,
FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
FOREIGN KEY (id_supermercado) REFERENCES supermercado(id_supermercado)

);

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





SELECT * FROM usuario;
SELECT * FROM producto;
SELECT * FROM supermercado;



