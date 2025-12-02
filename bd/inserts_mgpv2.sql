-- ===========================================================
-- SCRIPT DE DATOS DE PRUEBA - mpgv2
-- Deshuace García - Sistema de gestión de inventario de piezas
-- Fecha: Diciembre 2024
-- ===========================================================

USE mpgv2;

-- ===========================================================
-- 1. ROLES
-- ===========================================================
INSERT INTO roles (id, role) VALUES
(1, 'USER'),
(2, 'ADMIN'),
(3, 'SELLER'),
(4, 'INVENTORY_MANAGER'),
(5, 'ACCOUNTANT');

-- ===========================================================
-- 2. USUARIOS
-- Contraseña para todos: "password123" (deberás hashearla en producción)
-- ===========================================================
INSERT INTO users (name, role_id, username, password) VALUES
('Juan Pérez', 2, 'admin', '$2a$10$example.hash.admin'),
('María García', 3, 'maria.seller', '$2a$10$example.hash.seller1'),
('Carlos López', 3, 'carlos.seller', '$2a$10$example.hash.seller2'),
('Ana Martínez', 4, 'ana.inventory', '$2a$10$example.hash.inventory'),
('Roberto Sánchez', 5, 'roberto.accountant', '$2a$10$example.hash.accountant'),
('Laura Torres', 1, 'laura.user', '$2a$10$example.hash.user');

-- ===========================================================
-- 3. MARCAS DE VEHÍCULOS
-- ===========================================================
INSERT INTO brands (name) VALUES
('TOYOTA'),
('HONDA'),
('FORD'),
('CHEVROLET'),
('NISSAN'),
('VOLKSWAGEN'),
('MAZDA'),
('HYUNDAI'),
('KIA'),
('BMW');

-- ===========================================================
-- 4. MODELOS DE VEHÍCULOS
-- ===========================================================
INSERT INTO models (brand_id, serial_number, name, year, transmission, engine, vehicle_class) VALUES
-- Toyota
(1, 'JTDKB20U487500102', 'Corolla', 2020, 'AUTOMATIC', '4 cilindros 1.8L', 'Sedan'),
(1, 'JTDKT924561234567', 'Camry', 2019, 'AUTOMATIC', '4 cilindros 2.5L', 'Sedan'),
(1, 'JTMZK31V395012345', 'RAV4', 2021, 'AUTOMATIC', '4 cilindros 2.5L', 'SUV'),
(1, 'JTNKARJE3GJ012345', 'Tacoma', 2018, 'STANDARD', 'V6 3.5L', 'Pickup'),

-- Honda
(2, '1HGBH41JXMN109186', 'Civic', 2021, 'AUTOMATIC', '4 cilindros 2.0L', 'Sedan'),
(2, '5J6RM4H75BL012345', 'CR-V', 2020, 'AUTOMATIC', '4 cilindros 1.5L Turbo', 'SUV'),
(2, '1HGCP2F87CA012345', 'Accord', 2019, 'AUTOMATIC', '4 cilindros 1.5L Turbo', 'Sedan'),

-- Ford
(3, '1FAHP3F20CL123456', 'Focus', 2018, 'STANDARD', '4 cilindros 2.0L', 'Sedan'),
(3, '1FM5K8D84FGB12345', 'Explorer', 2020, 'AUTOMATIC', 'V6 3.5L', 'SUV'),
(3, '1FTEW1E59KFC12345', 'F-150', 2019, 'AUTOMATIC', 'V8 5.0L', 'Pickup'),

-- Chevrolet
(4, '3GNKBERS0LS123456', 'Equinox', 2021, 'AUTOMATIC', '4 cilindros 1.5L Turbo', 'SUV'),
(4, '1G1BE5SM2G7123456', 'Cruze', 2018, 'AUTOMATIC', '4 cilindros 1.4L Turbo', 'Sedan'),
(4, '1GCGTCEN8E1123456', 'Silverado', 2020, 'AUTOMATIC', 'V8 5.3L', 'Pickup'),

-- Nissan
(5, '1N4AL3AP8JC123456', 'Altima', 2019, 'AUTOMATIC', '4 cilindros 2.5L', 'Sedan'),
(5, '5N1DR2MM4LC123456', 'Rogue', 2020, 'AUTOMATIC', '4 cilindros 2.5L', 'SUV');

-- ===========================================================
-- 5. VEHÍCULOS COMPRADOS PARA DESHUESE
-- ===========================================================
INSERT INTO purchase_vehicles (model_id, purchase_date, purchase_cost) VALUES
(1, '2024-01-15', 45000.00),
(2, '2024-02-20', 52000.00),
(3, '2024-03-10', 68000.00),
(5, '2024-04-05', 48000.00),
(7, '2024-05-12', 42000.00),
(9, '2024-06-18', 71000.00),
(11, '2024-07-22', 38000.00),
(13, '2024-08-30', 55000.00);

-- ===========================================================
-- 6. IMÁGENES DE MODELOS
-- ===========================================================
INSERT INTO model_images (model_id, image_url) VALUES
(1, 'https://example.com/images/corolla_2020_front.jpg'),
(1, 'https://example.com/images/corolla_2020_side.jpg'),
(2, 'https://example.com/images/camry_2019_front.jpg'),
(3, 'https://example.com/images/rav4_2021_front.jpg'),
(5, 'https://example.com/images/civic_2021_front.jpg'),
(7, 'https://example.com/images/accord_2019_front.jpg');

-- ===========================================================
-- 7. PIEZAS DE INVENTARIO
-- ===========================================================
INSERT INTO parts (code, name, side, category_type, color, price, quantity, model_id) VALUES
-- Piezas de Corolla 2020
('COR-ALT-001', 'Alternador', 'UNIDIRECTIONAL', 'ELECTRICAL', NULL, 2500.00, 3, 1),
('COR-PARA-L-001', 'Parachoques Delantero', 'FORWARD', 'COLLISION', 'Negro', 3500.00, 2, 1),
('COR-FARO-L-001', 'Faro Delantero Izquierdo', 'LEFT', 'ELECTRICAL', 'Transparente', 1800.00, 1, 1),
('COR-FARO-R-001', 'Faro Delantero Derecho', 'RIGHT', 'ELECTRICAL', 'Transparente', 1800.00, 1, 1),
('COR-PUERTA-L-001', 'Puerta Delantera Izquierda', 'LEFT', 'COLLISION', 'Gris Plata', 4200.00, 1, 1),
('COR-MOTOR-001', 'Motor Completo 1.8L', 'UNIDIRECTIONAL', 'ENGINE', NULL, 25000.00, 1, 1),

-- Piezas de Camry 2019
('CAM-TRANS-001', 'Transmisión Automática', 'UNIDIRECTIONAL', 'TRANSMISSION', NULL, 18000.00, 1, 2),
('CAM-COFRE-001', 'Cofre', 'FORWARD', 'COLLISION', 'Blanco', 3800.00, 1, 2),
('CAM-ESPEJO-L-001', 'Espejo Lateral Izquierdo', 'LEFT', 'COLLISION', 'Negro', 850.00, 2, 2),
('CAM-ESPEJO-R-001', 'Espejo Lateral Derecho', 'RIGHT', 'COLLISION', 'Negro', 850.00, 2, 2),

-- Piezas de RAV4 2021
('RAV-SUSPENSION-001', 'Amortiguador Delantero', 'FORWARD', 'SUSPENSION', NULL, 2200.00, 4, 3),
('RAV-FRENO-001', 'Disco de Freno Delantero', 'FORWARD', 'BRAKES', NULL, 950.00, 4, 3),
('RAV-LLANTA-001', 'Llanta de Aleación 17"', 'UNIDIRECTIONAL', 'CHASSIS', 'Plata', 3500.00, 4, 3),
('RAV-PUERTA-R-001', 'Puerta Trasera Derecha', 'RIGHT-BACK', 'COLLISION', 'Rojo', 4500.00, 1, 3),

-- Piezas de Civic 2021
('CIV-VOLANTE-001', 'Volante Multifunción', 'UNIDIRECTIONAL', 'INSIDE', 'Negro', 2800.00, 1, 5),
('CIV-TABLERO-001', 'Tablero de Instrumentos', 'UNIDIRECTIONAL', 'INSIDE', 'Negro', 3200.00, 1, 5),
('CIV-RADIADOR-001', 'Radiador', 'FORWARD', 'ENGINE', NULL, 2400.00, 2, 5),
('CIV-BATERIA-001', 'Batería 12V', 'UNIDIRECTIONAL', 'ELECTRICAL', NULL, 1200.00, 3, 5),

-- Piezas de Accord 2019
('ACC-CATALIZ-001', 'Convertidor Catalítico', 'UNIDIRECTIONAL', 'ENGINE', NULL, 8500.00, 1, 7),
('ACC-ESCAPE-001', 'Sistema de Escape Completo', 'BACK', 'ENGINE', NULL, 4200.00, 1, 7),
('ACC-COMPRESOR-001', 'Compresor de A/C', 'UNIDIRECTIONAL', 'ELECTRICAL', NULL, 4800.00, 1, 7),

-- Piezas genéricas sin modelo específico
('GEN-FILTRO-001', 'Filtro de Aceite Universal', 'UNIDIRECTIONAL', 'ENGINE', NULL, 150.00, 25, NULL),
('GEN-FILTRO-002', 'Filtro de Aire Universal', 'UNIDIRECTIONAL', 'ENGINE', NULL, 200.00, 20, NULL),
('GEN-BUJIA-001', 'Juego de Bujías', 'UNIDIRECTIONAL', 'ENGINE', NULL, 450.00, 15, NULL),
('GEN-LIMPIA-001', 'Plumillas Limpiaparabrisas', 'UNIDIRECTIONAL', 'OTHER', 'Negro', 280.00, 30, NULL);

-- ===========================================================
-- 8. IMÁGENES DE PIEZAS
-- ===========================================================
INSERT INTO parts_images (part_id, image_url) VALUES
(1, 'https://example.com/parts/alternador_corolla.jpg'),
(2, 'https://example.com/parts/parachoques_corolla_front.jpg'),
(2, 'https://example.com/parts/parachoques_corolla_detail.jpg'),
(3, 'https://example.com/parts/faro_left_corolla.jpg'),
(6, 'https://example.com/parts/motor_corolla_complete.jpg'),
(11, 'https://example.com/parts/amortiguador_rav4.jpg'),
(15, 'https://example.com/parts/volante_civic.jpg');

-- ===========================================================
-- 9. CLIENTES
-- ===========================================================
INSERT INTO customer (name, phone, rfc) VALUES
('Pedro Ramírez González', '3312345678', 'RAGP850615HJ2'),
('Sofía Hernández Díaz', '3398765432', 'HEDS920420MG5'),
('Miguel Ángel Torres', '3387654321', NULL),
('Lucía Méndez Pérez', '3345678901', 'MEPL880305HJ8'),
('Jorge Alberto Ruiz', '3323456789', NULL),
('Carmen Flores Sánchez', '3356789012', 'FOSC910712MG3'),
('Ricardo Morales López', '3334567890', NULL),
('Daniela Castro Jiménez', '3389012345', 'CAJD950128MG7'),
('Fernando Vargas Cruz', '3312349876', NULL),
('Patricia Gómez Rivera', '3398761234', 'GORP870920MG1');

-- ===========================================================
-- 10. DATOS FISCALES DE CLIENTES
-- ===========================================================
INSERT INTO customer_invoice_data (customer_id, rfc, business_name, address, postal_code, tax_regime, invoice_use, email) VALUES
(1, 'RAGP850615HJ2', 'Pedro Ramírez González', 'Av. Patria 1234, Zapopan, Jalisco', '45030', '612', 'G03', 'pedro.ramirez@email.com'),
(2, 'HEDS920420MG5', 'Sofía Hernández Díaz', 'Calle Juárez 567, Guadalajara, Jalisco', '44100', '605', 'G03', 'sofia.hernandez@email.com'),
(4, 'MEPL880305HJ8', 'Refaccionaria Méndez SA de CV', 'Calzada Independencia 890, Guadalajara, Jalisco', '44200', '601', 'G03', 'facturacion@refaccionariamendez.com'),
(6, 'FOSC910712MG3', 'Carmen Flores Sánchez', 'Av. Américas 2345, Guadalajara, Jalisco', '44630', '612', 'G03', 'carmen.flores@email.com'),
(8, 'CAJD950128MG7', 'Daniela Castro Jiménez', 'Calle Morelos 456, Tlaquepaque, Jalisco', '45500', '612', 'G01', 'daniela.castro@email.com'),
(10, 'GORP870920MG1', 'Patricia Gómez Rivera', 'Av. López Mateos 789, Zapopan, Jalisco', '45040', '605', 'G03', 'patricia.gomez@email.com');

-- ===========================================================
-- 11. TICKETS DE VENTA
-- ===========================================================
INSERT INTO tickets (folio, user_id, total, payment_method, items, date) VALUES
(UUID(), 2, 5300.00, 'CASH', 2, '2024-09-15 10:30:00'),
(UUID(), 3, 3600.00, 'CREDIT_CARD', 2, '2024-09-16 14:20:00'),
(UUID(), 2, 25000.00, 'DEBIT_CARD', 1, '2024-09-18 11:00:00'),
(UUID(), 3, 7800.00, 'CASH', 3, '2024-09-20 16:45:00'),
(UUID(), 2, 18000.00, 'CREDIT_CARD', 1, '2024-09-22 09:15:00'),
(UUID(), 3, 8450.00, 'CASH', 4, '2024-09-25 13:30:00'),
(UUID(), 2, 4200.00, 'DEBIT_CARD', 1, '2024-10-01 10:00:00'),
(UUID(), 3, 12500.00, 'CASH_ON_DELIVERY', 3, '2024-10-05 15:20:00');

-- ===========================================================
-- 12. VENTAS (SALES)
-- ===========================================================
-- Nota: Usa los folios generados anteriormente. 
-- Para este script, usaré variables, pero en producción deberás obtener los UUIDs reales
SET @ticket1 = (SELECT folio FROM tickets ORDER BY date LIMIT 1);
SET @ticket2 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 1);
SET @ticket3 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 2);
SET @ticket4 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 3);
SET @ticket5 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 4);
SET @ticket6 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 5);
SET @ticket7 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 6);
SET @ticket8 = (SELECT folio FROM tickets ORDER BY date LIMIT 1 OFFSET 7);

-- Ventas del Ticket 1
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket1, 1, 1, 2500.00, NULL),
(@ticket1, 3, 1, 1800.00, NULL);

-- Ventas del Ticket 2
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket2, 3, 1, 1800.00, NULL),
(@ticket2, 4, 1, 1800.00, NULL);

-- Ventas del Ticket 3
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket3, 6, 1, 25000.00, NULL);

-- Ventas del Ticket 4
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket4, 11, 2, 2200.00, NULL),
(@ticket4, 12, 2, 950.00, NULL),
(@ticket4, 22, 4, 150.00, NULL);

-- Ventas del Ticket 5
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket5, 7, 1, 18000.00, NULL);

-- Ventas del Ticket 6 (incluye pieza NO inventariada)
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket6, 18, 1, 1200.00, NULL),
(@ticket6, 23, 10, 200.00, NULL),
(@ticket6, 24, 5, 450.00, NULL),
(@ticket6, NULL, 1, 3500.00, 'Bomba de Gasolina Honda Accord 2018');

-- Ventas del Ticket 7
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket7, 5, 1, 4200.00, NULL);

-- Ventas del Ticket 8 (entrega a domicilio)
INSERT INTO sales (ticket_folio, part_id, quantity, price, part_name) VALUES
(@ticket8, 13, 2, 3500.00, NULL),
(@ticket8, 11, 2, 2200.00, NULL),
(@ticket8, NULL, 1, 1600.00, 'Baleros Traseros Toyota RAV4');

-- ===========================================================
-- 13. IMÁGENES DE TICKETS
-- ===========================================================
INSERT INTO ticket_images (ticket_folio, image_url) VALUES
(@ticket1, 'https://example.com/tickets/ticket1_receipt.jpg'),
(@ticket3, 'https://example.com/tickets/ticket3_motor_sold.jpg'),
(@ticket8, 'https://example.com/tickets/ticket8_delivery_parts.jpg');

-- ===========================================================
-- 14. ENTREGAS A DOMICILIO
-- ===========================================================
INSERT INTO ticket_delivery (ticket_folio, customer_id, delivery_address, delivery_status, delivered_at) VALUES
(@ticket8, 3, 'Calle Hidalgo 234, Tonalá, Jalisco, CP 45400', 'DELIVERED', '2024-10-06 11:30:00');

-- ===========================================================
-- 15. GARANTÍAS
-- ===========================================================
SET @sale_motor = (SELECT id FROM sales WHERE part_id = 6 LIMIT 1);
SET @sale_trans = (SELECT id FROM sales WHERE part_id = 7 LIMIT 1);
SET @sale_puerta = (SELECT id FROM sales WHERE part_id = 5 LIMIT 1);

INSERT INTO warranty (sale_id, status, expiration_date) VALUES
(@sale_motor, 'ACTIVE', DATE_ADD(CURDATE(), INTERVAL 6 MONTH)),
(@sale_trans, 'ACTIVE', DATE_ADD(CURDATE(), INTERVAL 3 MONTH)),
(@sale_puerta, 'EXPIRED', DATE_SUB(CURDATE(), INTERVAL 1 MONTH));

-- ===========================================================
-- 16. RECLAMACIONES DE GARANTÍA
-- ===========================================================
SET @warranty_motor = (SELECT id FROM warranty WHERE sale_id = @sale_motor LIMIT 1);

INSERT INTO warranty_claim (warranty_id, image_url, description, claim_type) VALUES
(@warranty_motor, 'https://example.com/warranty/motor_leak_evidence.jpg', 'El motor presenta fuga de aceite por el sello delantero', 'EXCHANGE');

-- ===========================================================
-- 17. APARTADOS (RESERVATIONS)
-- ===========================================================
INSERT INTO reservations (customer_id, deposit, total_price, part_id, part_name, balance, expiration_date, status) VALUES
(5, 5000.00, 18000.00, 7, NULL, 13000.00, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'ACTIVE'),
(7, 2000.00, 8500.00, 19, NULL, 6500.00, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'ACTIVE'),
(9, 1500.00, 4200.00, NULL, 'Cofre Ford Explorer 2018 Color Azul', 2700.00, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'ACTIVE');

-- ===========================================================
-- 18. PIEZAS RESERVADAS
-- ===========================================================
INSERT INTO parts_reserved (reservation_id, part_id, part_name, quantity) VALUES
(1, 7, NULL, 1),
(2, 19, NULL, 1),
(3, NULL, 'Cofre Ford Explorer 2018 Color Azul', 1);

-- ===========================================================
-- 19. QUEJAS DE CLIENTES
-- ===========================================================
INSERT INTO customer_issues (problem, status, customer_id) VALUES
('La pieza comprada no es compatible con mi vehículo', 'PENDING', 3),
('El faro tiene una grieta que no se mencionó en la venta', 'ATTENDED', 2),
('Solicito información sobre disponibilidad de motor para Nissan Sentra 2017', 'ATTENDED', 6),
('La pieza llegó con retraso', 'REJECTED', 8);

-- ===========================================================
-- 20. FACTURAS
-- ===========================================================
INSERT INTO invoices (folio, ticket_folio, invoice_number, receiver_customer, url_document) VALUES
(UUID(), @ticket3, 'A12345678', 'RAGP850615HJ2', 'https://example.com/invoices/A12345678.xml'),
(UUID(), @ticket5, 'A12345679', 'MEPL880305HJ8', 'https://example.com/invoices/A12345679.xml'),
(UUID(), @ticket2, 'A12345680', 'HEDS920420MG5', 'https://example.com/invoices/A12345680.xml');

-- ===========================================================
-- 21. GASTOS OPERATIVOS
-- ===========================================================
INSERT INTO expenses (name, amount, category, pay_before, payment_at, created_by) VALUES
('Compra de Toyota Corolla 2020', 45000.00, 'VEHICLE_PURCHASE', '2024-01-15', '2024-01-15', 4),
('Renta del local - Enero 2024', 15000.00, 'RENT', '2024-01-31', '2024-01-28', 2),
('Luz y agua - Enero 2024', 3500.00, 'UTILITIES', '2024-01-31', '2024-02-02', 2),
('Nómina - Enero 2024', 35000.00, 'SALARIES', '2024-01-31', '2024-01-31', 2),
('Compra de Honda Civic 2021', 48000.00, 'VEHICLE_PURCHASE', '2024-04-05', '2024-04-05', 4),
('Renta del local - Septiembre 2024', 15000.00, 'RENT', '2024-09-30', '2024-09-28', 2),
('Internet y teléfono - Septiembre 2024', 1200.00, 'SERVICES', '2024-09-30', '2024-09-25', 2),
('Mantenimiento de herramientas', 2800.00, 'MAINTENANCE', '2024-09-15', '2024-09-15', 4),
('Compra de Chevrolet Silverado 2020', 55000.00, 'VEHICLE_PURCHASE', '2024-08-30', '2024-08-30', 4),
('Nómina - Septiembre 2024', 35000.00, 'SALARIES', '2024-09-30', '2024-09-30', 2),
('Renta del local - Octubre 2024', 15000.00, 'RENT', '2024-10-31', NULL, 2),
('Luz y agua - Octubre 2024', 3800.00, 'UTILITIES', '2024-10-31', NULL, 2);

-- ===========================================================
-- 22. LOGS DE LOGIN
-- ===========================================================
INSERT INTO login_logs (user_id, ip_address, user_agent, role_snapshot) VALUES
(2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', 'ADMIN'),
(3, '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', 'SELLER'),
(2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', 'ADMIN'),
(4, '192.168.1.102', 'Mozilla/5.0 (X11; Linux x86_64)', 'INVENTORY_MANAGER'),
(3, '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', 'SELLER'),
(5, '192.168.1.103', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', 'ACCOUNTANT');

-- ===========================================================
-- 23. LOGS DE APLICACIÓN
-- ===========================================================
INSERT INTO app_logs (level, message, user_id, login_log_id, context, path) VALUES
('INFO', 'Usuario admin inició sesión exitosamente', 2, 1, 'LoginController', '/api/auth/login'),
('INFO', 'Venta registrada correctamente', 3, 2, 'SalesController', '/api/sales'),
('WARN', 'Intento de acceso a recurso restringido', 6, NULL, 'AuthMiddleware', '/api/admin/users'),
('ERROR', 'Error al procesar pago con tarjeta', 3, 5, 'PaymentService', '/api/payments/process'),
('INFO', 'Pieza agregada al inventario', 4, 4, 'PartsController', '/api/parts'),
('DEBUG', 'Consulta de inventario realizada', 2, 3, 'PartsController', '/api/parts/search');

-- ===========================================================
-- CONSULTAS DE VERIFICACIÓN
-- ===========================================================

-- Ver resumen de inventario
SELECT 
    b.name AS marca,
    m.name AS modelo,
    COUNT(p.id) AS total_piezas,
    SUM(p.quantity) AS cantidad_total,
    SUM(p.price * p.quantity) AS valor_inventario
FROM parts p
LEFT JOIN models m ON p.model_id = m.id
LEFT JOIN brands b ON m.brand_id = b.id
GROUP BY b.name, m.name
ORDER BY valor_inventario DESC;

-- Ver ventas del mes actual
SELECT 
    DATE(t.date) AS fecha,
    COUNT(t.folio) AS num_tickets,
    SUM(t.total) AS total_vendido
FROM tickets t
WHERE MONTH(t.date) = MONTH(CURDATE()) 
  AND YEAR(t.date) = YEAR(CURDATE())
GROUP BY DATE(t.date)
ORDER BY fecha DESC;

-- Ver apartados activos
SELECT 
    r.id,
    c.name AS cliente,
    COALESCE(p.name, r.part_name) AS pieza,
    r.deposit AS anticipo,
    r.balance AS saldo,
    r.expiration_date AS vencimiento,
    r.status
FROM reservations r
JOIN customer c ON r.customer_id = c.id
LEFT JOIN parts p ON r.part_id = p.id
WHERE r.status = 'ACTIVE'
ORDER BY r.expiration_date;

-- Ver garantías activas
SELECT 
    w.id,
    p.name AS pieza,
    s.price AS precio_venta,
    w.expiration_date AS vence,
    w.status
FROM warranty w
JOIN sales s ON w.sale_id = s.id
LEFT JOIN parts p ON s.part_id = p.id
WHERE w.status = 'ACTIVE'
ORDER BY w.expiration_date;

-- ===========================================================
-- FIN DEL SCRIPT
-- ===========================================================