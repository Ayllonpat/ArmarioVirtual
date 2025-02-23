-- Insertar Tipos de Prenda
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (1, 'Camiseta', NULL);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (2, 'Pantalón', NULL);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (3, 'Chaqueta', NULL);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (4, 'Zapatos', NULL);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (5, 'Sombrero', NULL);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (6, 'Camiseta de Manga Larga', 1);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (7, 'Camiseta Deportiva', 1);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (8, 'Jeans', 2);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (9, 'Chaqueta de Cuero', 3);
INSERT INTO tipo_prenda (id, nombre, padre_id) VALUES (10, 'Botines', 4);

-- Insertar Prendas
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (1, 'Camiseta Roja', 'camiseta_roja.jpg', 'Rojo', 'M', 'http://tienda.com/camiseta-roja', 'PUBLICO', NOW(), 1, (SELECT id FROM usuario WHERE email = 'juan.perez@email.com'));
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (2, 'Pantalón Azul', 'pantalon_azul.jpg', 'Azul', 'L', 'http://tienda.com/pantalon-azul', 'PUBLICO', NOW(), 2, (SELECT id FROM usuario WHERE email = 'ana.gomez@email.com'));
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (3, 'Chaqueta Negra', 'chaqueta_negra.jpg', 'Negro', 'M', 'http://tienda.com/chaqueta-negra', 'PUBLICO', NOW(), 3, (SELECT id FROM usuario WHERE email = 'carlos.lopez@email.com'));
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (4, 'Zapatos Negros', 'zapatos_negros.jpg', 'Negro', '42', 'http://tienda.com/zapatos-negros', 'PUBLICO', NOW(), 4, (SELECT id FROM usuario WHERE email = 'juan.perez@email.com'));
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (5, 'Sombrero Beige', 'sombrero_beige.jpg', 'Beige', 'Único', 'http://tienda.com/sombrero-beige', 'PUBLICO', NOW(), 5, (SELECT id FROM usuario WHERE email = 'ana.gomez@email.com'));
INSERT INTO prenda (id, nombre, imagen, color, talla, enlaceCompra, visibilidad, fecha_publicacion, tipo_prenda_id, cliente_id) VALUES (6, 'Camiseta de Manga Larga Azul', 'camiseta_manga_larga_azul.jpg', 'Azul', 'L', 'http://tienda.com/camiseta-manga-larga-azul', 'PUBLICO', NOW(), 6, (SELECT id FROM usuario WHERE email = 'carlos.lopez@email.com'));

-- Insertar Conjuntos
INSERT INTO conjunto (id, nombre, imagen, fecha_publicacion, visibilidad, cliente_id) VALUES (1, 'Conjunto Deportivo', 'conjunto_deportivo.jpg', NOW(), 'PUBLICO', (SELECT id FROM usuario WHERE email = 'juan.perez@email.com'));
INSERT INTO conjunto (id, nombre, imagen, fecha_publicacion, visibilidad, cliente_id) VALUES (2, 'Conjunto Casual', 'conjunto_casual.jpg', NOW(), 'PUBLICO', (SELECT id FROM usuario WHERE email = 'ana.gomez@email.com'));
INSERT INTO conjunto (id, nombre, imagen, fecha_publicacion, visibilidad, cliente_id) VALUES (3, 'Conjunto Elegante', 'conjunto_elegante.jpg', NOW(), 'PUBLICO', (SELECT id FROM usuario WHERE email = 'carlos.lopez@email.com'));

-- Insertar Comentarios
INSERT INTO comentario (id, contenido, fecha_publicacion, cliente_id, prenda_id, conjunto_id) VALUES (1, 'Me encanta esta camiseta roja, la compré y es de excelente calidad.', NOW(), (SELECT id FROM usuario WHERE email = 'juan.perez@email.com'), 1, NULL);
INSERT INTO comentario (id, contenido, fecha_publicacion, cliente_id, prenda_id, conjunto_id) VALUES (2, 'El pantalón azul es perfecto para cualquier ocasión, lo recomiendo mucho.', NOW(), (SELECT id FROM usuario WHERE email = 'ana.gomez@email.com'), 2, NULL);
INSERT INTO comentario (id, contenido, fecha_publicacion, cliente_id, prenda_id, conjunto_id) VALUES (3, 'Muy buena chaqueta, me la pondré para el invierno.', NOW(), (SELECT id FROM usuario WHERE email = 'carlos.lopez@email.com'), 3, NULL);
INSERT INTO comentario (id, contenido, fecha_publicacion, cliente_id, prenda_id, conjunto_id) VALUES (4, 'Los zapatos negros son muy cómodos, ideales para el día a día.', NOW(), (SELECT id FROM usuario WHERE email = 'juan.perez@email.com'), 4, NULL);
INSERT INTO comentario (id, contenido, fecha_publicacion, cliente_id, prenda_id, conjunto_id) VALUES (5, 'El sombrero beige es perfecto para el verano, me encanta.', NOW(), (SELECT id FROM usuario WHERE email = 'ana.gomez@email.com'), 5, NULL);
