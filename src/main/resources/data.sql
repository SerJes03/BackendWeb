INSERT INTO usuarios
(username, nombre, apellido,
password, red_social, fecha_nacimiento, enabled, image)
VALUES
('jessica.serna@est.iudigital.edu.co', 'Jessica', 'Serna',
'$2a$10$BzVSn9catDbbukqZ3a4CfexQGTskzN2FXyX/1D78.xt7mfGuV.aBO', 0, '2000-12-03', 1, '');

INSERT INTO roles(nombre, descripcion)
VALUES ('ROLE_ADMIN', 'administrador');

INSERT INTO roles(nombre, descripcion)
VALUES ('ROLE_USER', 'usuario normal');

INSERT INTO roles_usuarios(usuarios_id, roles_id)
VALUES (1, 1);

INSERT INTO roles_usuarios(usuarios_id, roles_id)
VALUES (1, 2);

INSERT INTO delitos (nombre, descripcion, usuarios_id)
VALUES ('hurto', 'Cuando lo roban a uno', 1);

INSERT INTO delitos (nombre, descripcion, usuarios_id)
VALUES ('homicidio', 'Cuando lo matan a uno', 1);

