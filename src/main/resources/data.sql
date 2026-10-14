INSERT INTO clientes (nombre, apellido, cedula, telefono, email) VALUES
('Juan', 'Pérez', '1234567', '0981123456', 'juan.perez@correo.com'),
('María', 'González', '2345678', '0982234567', 'maria.gonzalez@correo.com'),
('Carlos', 'Benítez', '3456789', '0983345678', 'carlos.benitez@correo.com'),
('Lucía', 'Martínez', '4567890', '0984456789', 'lucia.martinez@correo.com');

INSERT INTO canchas (nombre, tipo, precio_hora, activa) VALUES
('Cancha 1 - Fútbol 5', 'FUTBOL_5', 120000, TRUE),
('Cancha 2 - Fútbol 7', 'FUTBOL_7', 180000, TRUE),
('Cancha 3 - Pádel', 'PADEL', 90000, TRUE),
('Cancha 4 - Tenis', 'TENIS', 70000, TRUE),
('Cancha 5 - Básquet', 'BASQUET', 80000, FALSE);

INSERT INTO reservas
    (cliente_id, cancha_id, fecha, hora_inicio, hora_fin, estado, total, observacion)
VALUES
(1, 1, '2026-10-20', '18:00:00', '19:00:00', 'CONFIRMADA', 120000, 'Partido con amigos'),
(2, 3, '2026-10-20', '19:00:00', '20:30:00', 'PENDIENTE', 135000, 'Torneo interno'),
(3, 2, '2026-10-21', '20:00:00', '22:00:00', 'CONFIRMADA', 360000, 'Cumpleaños'),
(4, 4, '2026-10-22', '17:00:00', '18:00:00', 'CANCELADA', 70000, 'Cancelada por lluvia');