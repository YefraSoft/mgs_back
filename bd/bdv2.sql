-- ===========================================================
-- Script de creación de base de datos: mpgv2 27/11/2025
-- Deshuace Garcia - Sistema de gestión de inventario de piezas
-- ===========================================================

DROP DATABASE IF EXISTS mpgv2;

CREATE DATABASE mpgv2;

USE mpgv2;

-- ===========================================================
-- TABLA: brands
-- Descripción: Lista de marcas de vehículos soportadas por el sistema. Se usa para relacionar modelos y compatibilidad de piezas.
-- Requisitos relacionados: REQ-001, REQ-007
-- ===========================================================
DROP TABLE IF EXISTS brands;

CREATE TABLE brands (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada marca
    name VARCHAR(50) NOT NULL UNIQUE, -- Nombre de la marca (Ejemplo: 'TOYOTA', 'FORD', 'HONDA')
    INDEX idx_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: models
-- Descripción: Modelos por marca; contiene VIN/serial, año, transmisión y clase para identificar compatibilidades.
-- Requisitos relacionados: REQ-001, REQ-002, REQ-003
-- ===========================================================
DROP TABLE IF EXISTS models;

CREATE TABLE models (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada modelo
    brand_id INT NOT NULL, -- ID de la marca a la que pertenece el modelo (relacionado con la tabla 'brands')
    serial_number VARCHAR(100), -- Número de serie del modelo (Ejemplo: 'JTDKB20U487500102')
    name VARCHAR(50) NOT NULL, -- Nombre del modelo (Ejemplo: 'Corolla', 'Civic', 'Mustang')
    year INT, -- Año de fabricación del vehículo (Ejemplo: 2020, 2021)
    transmission ENUM('AUTOMATIC', 'STANDARD'), -- Tipo de transmisión: Automática o Estándar
    engine VARCHAR(50) NOT NULL, -- Tipo de motor (Ejemplo: '4 cilindros', 'V6', 'Eléctrico')
    vehicle_class VARCHAR(50) NOT NULL, -- Clase del vehículo (Ejemplo: 'Sedan', 'SUV', 'Pickup', 'Coupe')
    FOREIGN KEY (brand_id) REFERENCES brands (id) ON DELETE CASCADE, -- Al borrar una marca, se borran sus modelos
    INDEX idx_brand_id (brand_id), -- Índice para mejorar rendimiento de búsquedas
    INDEX idx_year (year) -- Índice para búsquedas por año
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: purchase_vehicles
-- Descripción: Vehículos adquiridos completos; almacena fecha y costo de adquisición para amortización y trazabilidad.
-- Requisitos relacionados: REQ-001, REQ-004, REQ-005
-- ===========================================================
DROP TABLE IF EXISTS purchase_vehicles;

CREATE TABLE purchase_vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del vehículo comprado
    model_id INT NOT NULL, -- ID del modelo del vehículo (relación con 'models')
    purchase_date DATE, -- Fecha de compra del vehículo
    purchase_cost DECIMAL(10, 2), -- Costo de compra del vehículo
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de creación del registro
    FOREIGN KEY (model_id) REFERENCES models (id) ON DELETE RESTRICT, -- No permite borrar un modelo si existen compras asociadas
    INDEX idx_model_id (model_id),
    INDEX idx_purchase_date (purchase_date)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: model_images
-- Descripción: URLs de imágenes por modelo para la galería del sitio; registra fecha de creación.
-- Requisitos relacionados: REQ-005
-- ===========================================================
DROP TABLE IF EXISTS model_images;

CREATE TABLE model_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL, -- URL de la imagen asociada al modelo
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES models (id) ON DELETE CASCADE,
    INDEX idx_model_id (model_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: parts
-- Descripción: Catálogo de piezas con sus atributos (lado, categoría, color, precio y stock) y relación opcional al modelo de vehículo.
-- Requisitos relacionados: REQ-006, REQ-007, REQ-008, REQ-009
-- ===========================================================
DROP TABLE IF EXISTS parts;

CREATE TABLE parts (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada pieza
    code VARCHAR(50), -- Código interno de la pieza
    name VARCHAR(255) NOT NULL, -- Nombre de la pieza (Ejemplo: 'Alternador', 'Amortiguador', 'Parachoques')
    side ENUM(
        'LEFT', -- Lado izquierdo
        'RIGHT', -- Lado derecho
        'LEFT-BACK', -- Trasero izquierdo
        'RIGHT-BACK', -- Trasero derecho
        'BACK', -- Trasero
        'FORWARD', -- Delantero
        'UNIDIRECTIONAL' -- Sin dirección específica
    ) NOT NULL, -- Posición de la pieza en el vehículo
    category_type ENUM(
        'COLLISION',
        'CHASSIS',
        'ENGINE',
        'TRANSMISSION',
        'SUSPENSION',
        'BRAKES',
        'INSIDE',
        'ELECTRICAL',
        'OTHER'
    ) NOT NULL, -- Tipo de categoría de la pieza
    color VARCHAR(50), -- Color de la pieza (Ejemplo: 'Negro', 'Blanco', 'Gris')
    price DECIMAL(10, 2) NOT NULL, -- Precio de venta de la pieza
    quantity TINYINT DEFAULT 1, -- Cantidad disponible en inventario (por defecto 1)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    model_id INT,
    FOREIGN KEY (model_id) REFERENCES models (id) ON DELETE SET NULL, -- Relación con la tabla 'models'
    INDEX idx_model_id (model_id),
    INDEX idx_code (code) -- Índice para búsquedas por código
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: parts_images
-- Descripción: Imágenes vinculadas a piezas del inventario para mostrar en la web o en listados.
-- Requisitos relacionados: REQ-009
-- ===========================================================
DROP TABLE IF EXISTS parts_images;

CREATE TABLE parts_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    part_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL, -- URL de la imagen asociada a la pieza
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (part_id) REFERENCES parts (id) ON DELETE CASCADE,
    INDEX idx_part_id (part_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: roles
-- Descripción: Roles de usuario del sistema
-- ===========================================================
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
    id TINYINT PRIMARY KEY, -- Identificador único del rol (1-4)
    role ENUM(
        'USER', -- Usuario básico
        'ADMIN', -- Administrador del sistema
        'SELLER', -- Vendedor
        'INVENTORY_MANAGER', -- Gestor de inventario
        'ACCOUNTANT' -- Contador
    ) NOT NULL DEFAULT 'USER' -- Rol del usuario en el sistema
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: users
-- Descripción: Usuarios del sistema
-- ===========================================================
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada usuario
    name VARCHAR(50) NOT NULL, -- Nombre completo del usuario
    role_id TINYINT, -- ID del rol asignado (relacionado con la tabla 'roles')
    username VARCHAR(50) NOT NULL UNIQUE, -- Nombre de usuario único para el acceso al sistema
    password VARCHAR(255) NOT NULL, -- Contraseña cifrada del usuario (usar bcrypt o similar)
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE SET NULL, -- Relación con la tabla 'roles'
    INDEX idx_username (username), -- Índice para mejorar velocidad de login
    INDEX idx_role_id (role_id) -- Índice para filtrado por rol
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: customer
-- Descripción: Información de clientes del negocio
-- ===========================================================
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada cliente
    name VARCHAR(100) NOT NULL, -- Nombre completo o razón social del cliente
    phone VARCHAR(20) NOT NULL, -- Número telefónico de contacto
    rfc VARCHAR(13), -- RFC del cliente (opcional), usado para facturación cuando exista
    INDEX idx_phone (phone), -- Índice para búsqueda por teléfono
    INDEX idx_rfc (rfc) -- Índice para búsqueda por RFC
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: tickets
-- Descripción: Tickets de venta (folio UUID) que agregan ventas, método de pago, total y metadata para facturación.
-- Requisitos relacionados: REQ-011, REQ-013, REQ-014, REQ-015
-- ===========================================================

DROP TABLE IF EXISTS tickets;

CREATE TABLE tickets (
    folio CHAR(36) PRIMARY KEY DEFAULT(UUID()), -- Folio único del ticket generado automáticamente con UUID
    user_id INT NOT NULL, -- ID del usuario/vendedor que realizó la venta (relacionado con la tabla 'users')
    total DECIMAL(10, 2) NOT NULL, -- Monto total del ticket
    payment_method ENUM(
        'CASH', -- Efectivo
        'CREDIT_CARD', -- Tarjeta de crédito
        'DEBIT_CARD', -- Tarjeta de débito
        'CASH_ON_DELIVERY', -- Efectivo al recibir
        'OTHER' -- Otro método de pago
    ) NOT NULL, -- Método de pago
    items TINYINT DEFAULT 1, -- Número de items/piezas en el ticket
    date DATETIME DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de generación del ticket
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT, -- Relación con la tabla 'users' (no permite eliminar usuarios con tickets)
    INDEX idx_user_id (user_id), -- Índice para reportes por vendedor
    INDEX idx_date (date) -- Índice para reportes por fecha
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: ticket_images
-- Descripción: Imágenes asociadas a tickets
-- ===========================================================
DROP TABLE IF EXISTS ticket_images;

-- ===========================================================
-- TABLA: ticket_images
-- Descripción: Imágenes asociadas a tickets (evidencias o recibos). Guarda la URL de la imagen y fecha de creación.
-- Requisitos relacionados: REQ-014 (detalles del ticket), REQ-033 (evidencia en reclamaciones)
-- ===========================================================
CREATE TABLE ticket_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_folio CHAR(36) NOT NULL, -- Folio del ticket
    image_url VARCHAR(500) NOT NULL, -- URL de la imagen (foto del ticket o evidencia)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE,
    INDEX idx_ticket_folio (ticket_folio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: ticket_delivery
-- Descripción: Gestiona entregas a domicilio asociadas a un ticket; registra dirección, estado y trazabilidad.
-- Requisitos relacionados: REQ-023, REQ-024, REQ-025
-- ===========================================================
CREATE TABLE ticket_delivery (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_folio CHAR(36) NOT NULL, -- Folio del ticket asociado
    customer_id INT NOT NULL, -- Cliente receptor de la entrega
    delivery_address VARCHAR(255) NOT NULL, -- Dirección completa de entrega
    delivery_status ENUM(
        'PENDING',
        'IN_TRANSIT',
        'DELIVERED',
        'FAILED'
    ) NOT NULL DEFAULT 'PENDING', -- Estado de la entrega
    delivered_at DATETIME, -- Fecha/hora de entrega
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT,
    INDEX idx_ticket_folio_delivery (ticket_folio),
    INDEX idx_customer_id_delivery (customer_id),
    INDEX idx_delivery_status (delivery_status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: sales
-- Descripción: Línea de detalle por ticket: pieza vendida, cantidad, precio y campos de garantía asociados.
-- Requisitos relacionados: REQ-011, REQ-012, REQ-031, REQ-033
-- ===========================================================
DROP TABLE IF EXISTS sales;

CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada registro de venta
    ticket_folio CHAR(36) NOT NULL, -- Folio del ticket asociado (relacionado con la tabla 'tickets')
    part_id INT, -- ID de la pieza vendida (relacionado con la tabla 'parts')
    quantity TINYINT DEFAULT 1, -- Cantidad de piezas vendidas
    price DECIMAL(10, 2) NOT NULL, -- Precio unitario de la pieza al momento de la venta
    part_name VARCHAR(200), -- Pieza no inventariada (se usa cuando la pieza no existe en inventario)
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE, -- Relación con la tabla 'tickets'
    FOREIGN KEY (part_id) REFERENCES parts (id) ON DELETE CASCADE, -- Relación con la tabla 'parts'
    INDEX idx_ticket_folio (ticket_folio), -- Índice para consulta de ventas por ticket
    INDEX idx_part_id (part_id), -- Índice para reportes de piezas más vendidas
    CHECK (
        part_id IS NOT NULL
        OR part_name IS NOT NULL
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: warranty
-- Descripción: Información de la garantía asociada a una venta. Registra estado, duración y vínculo con la venta.
-- Requisitos relacionados: REQ-029, REQ-030, REQ-031, REQ-032
-- ===========================================================
DROP TABLE IF EXISTS warranty;

CREATE TABLE warranty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL, -- Venta asociada
    status ENUM(
        'ACTIVE',
        'REJECTED',
        'EXPIRED',
        'PENDING'
    ) DEFAULT 'PENDING', -- Estado de la garantía
    expiration_date DATE, -- Fecha de expiración de la garantía
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sale_id) REFERENCES sales (id) ON DELETE CASCADE,
    INDEX idx_sale_id (sale_id),
    INDEX idx_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: warranty_claim
-- Descripción: Evidencias y reclamaciones asociadas a una garantía; guarda fotos, descripción y tipo de reclamo.
-- Requisitos relacionados: REQ-033, REQ-034, REQ-035, REQ-036
-- ===========================================================
DROP TABLE IF EXISTS warranty_claim;

CREATE TABLE warranty_claim (
    id INT AUTO_INCREMENT PRIMARY KEY,
    warranty_id INT NOT NULL, -- Referencia a la garantía
    image_url VARCHAR(500) NOT NULL,
    description VARCHAR(255), -- Descripción del defecto o motivo de la reclamación
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    claim_type ENUM('RETURN', 'EXCHANGE') NOT NULL,
    FOREIGN KEY (warranty_id) REFERENCES warranty (id) ON DELETE CASCADE,
    INDEX idx_warranty_id (warranty_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: customer_issues
-- Descripción: Registra quejas/problemas reportados por clientes, su estado y relación con cliente/ticket.
-- Requisitos relacionados: REQ-041, REQ-042, REQ-043, REQ-044
-- ===========================================================
DROP TABLE IF EXISTS customer_issues;

CREATE TABLE customer_issues (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del problema
    problem VARCHAR(255) NOT NULL, -- Descripción del problema reportado por el cliente
    status ENUM(
        'ATTENDED', -- El problema fue atendido
        'REJECTED', -- El problema fue rechazado
        'NOT_FOUND', -- No se encontró el problema reportado
        'PENDING' -- Problema pendiente de atención
    ) NOT NULL DEFAULT 'PENDING', -- Estado actual del problema
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de registro del problema
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Fecha y hora de la última actualización
    customer_id INT, -- ID del cliente que reporta el problema
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE SET NULL -- Relación con la tabla 'customer'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: customer_invoice_data
-- Descripción: Datos fiscales (RFC, razón social, dirección) usados para emitir facturas a clientes.
-- Requisitos relacionados: REQ-039
-- ===========================================================
DROP TABLE IF EXISTS customer_invoice_data;

CREATE TABLE customer_invoice_data (
    customer_id INT NOT NULL,
    rfc VARCHAR(13) PRIMARY KEY NOT NULL, -- RFC único para cada cliente
    business_name VARCHAR(255) NOT NULL, -- Razón social
    address VARCHAR(255) NOT NULL, -- Dirección fiscal completa del cliente
    postal_code VARCHAR(6) NOT NULL, -- Código postal del domicilio fiscal
    tax_regime VARCHAR(50) NOT NULL, -- Régimen fiscal del cliente
    invoice_use VARCHAR(50) NOT NULL, -- Uso del CFDI
    email VARCHAR(100) NOT NULL, -- Correo electrónico para envío de facturas
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE, -- Relación con la tabla 'customer'
    INDEX idx_customer_id (customer_id), -- Índice para búsqueda por ID de cliente
    INDEX idx_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: invoices
-- Descripción: Registro de facturas (folio UUID) con referencia al ticket y URL del documento XML/PDF.
-- Requisitos relacionados: REQ-013, REQ-039
-- ===========================================================
DROP TABLE IF EXISTS invoices;

CREATE TABLE invoices (
    folio CHAR(36) PRIMARY KEY DEFAULT(UUID()) NOT NULL, -- Folio único de la factura generado con UUID
    ticket_folio CHAR(36) NULL, -- Folio del ticket asociado
    invoice_number VARCHAR(50) NOT NULL, -- Número de folio fiscal de la factura
    receiver_customer VARCHAR(13), -- RFC del cliente receptor de la factura
    url_document TEXT NOT NULL, -- URL o ruta donde se almacena el XML/PDF de la factura
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE, -- Relación con la tabla 'tickets'
    FOREIGN KEY (receiver_customer) REFERENCES customer_invoice_data (rfc) ON DELETE CASCADE, -- Relación con la tabla 'customer_invoice_data'
    INDEX idx_ticket_folio (ticket_folio), -- Índice para búsqueda de facturas por ticket
    INDEX idx_receiver (receiver_customer), -- Índice para búsqueda por cliente
    INDEX idx_invoice_number (invoice_number) -- Índice para búsqueda por folio fiscal
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: login_logs
-- Descripción: Registro de inicios de sesión de usuarios
-- ===========================================================
DROP TABLE IF EXISTS login_logs;

CREATE TABLE login_logs (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada registro de login
    user_id INT NOT NULL, -- ID del usuario que inició sesión
    ip_address VARCHAR(45), -- Dirección IP del dispositivo
    user_agent TEXT, -- Información del navegador/dispositivo utilizado
    role_snapshot VARCHAR(50), -- Rol del usuario al momento del login
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora del inicio de sesión
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, -- Relación con la tabla 'users'
    INDEX idx_user_id (user_id), -- Índice para auditoría por usuario
    INDEX idx_created_at (created_at) -- Índice para consultas por rango de fechas
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: app_logs
-- Descripción: Registro de eventos y errores de la aplicación
-- ===========================================================
DROP TABLE IF EXISTS app_logs;

CREATE TABLE app_logs (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada registro de log
    level ENUM(
        'INFO', -- Información general
        'DEBUG', -- Información de depuración
        'WARN', -- Advertencias
        'ERROR', -- Errores recuperables
        'FATAL' -- Errores críticos
    ) NOT NULL, -- Nivel de severidad del log
    message TEXT NOT NULL, -- Mensaje descriptivo del evento o error
    user_id INT, -- ID del usuario relacionado con el evento
    login_log_id INT, -- ID del login asociado al evento
    context VARCHAR(255), -- Datos adicionales de contexto
    path VARCHAR(255), -- Ruta o endpoint donde se produjo el evento
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora del evento
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL, -- Relación con la tabla 'users'
    FOREIGN KEY (login_log_id) REFERENCES login_logs (id) ON DELETE SET NULL, -- Relación con la tabla 'login_logs'
    INDEX idx_level (level), -- Índice para filtrado por nivel de severidad
    INDEX idx_user_id (user_id), -- Índice para búsqueda de logs por usuario
    INDEX idx_created_at (created_at) -- Índice para consultas por rango de fechas
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: reservations (Apartados)
-- Sistema de apartado de piezas con anticipo
-- REQ-019, REQ-020, REQ-021, REQ-022
-- ===========================================================
DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    deposit DECIMAL(10, 2) NOT NULL, -- Anticipo entregado
    total_price DECIMAL(10, 2) NOT NULL, -- Precio total de la(s) pieza(s)
    part_id INT, -- Pieza reservada (opcional, si existe en inventario)
    part_name VARCHAR(255), -- Nombre de la pieza cuando no está en inventario
    balance DECIMAL(10, 2) NOT NULL, -- Saldo pendiente por pagar
    expiration_date DATE NOT NULL, -- Fecha límite para completar el pago
    status ENUM(
        'ACTIVE',
        'COMPLETED',
        'CANCELLED',
        'EXPIRED'
    ) DEFAULT 'ACTIVE', -- Estado del apartado
    completed_ticket_folio CHAR(36), -- Folio del ticket generado al completar la venta
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT,
    FOREIGN KEY (completed_ticket_folio) REFERENCES tickets (folio) ON DELETE SET NULL,
    INDEX idx_customer_id (customer_id),
    INDEX idx_part_id (part_id),
    INDEX idx_status (status),
    INDEX idx_expiration (expiration_date),
    CHECK (
        part_id IS NOT NULL
        OR part_name IS NOT NULL
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: parts_reserved
-- Piezas apartadas en una reserva, en caso de cancelación se liberan al inventario.
-- ===========================================================

DROP TABLE IF EXISTS parts_reserved;

CREATE TABLE parts_reserved (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    part_id INT,
    part_name VARCHAR(255), -- Pieza no inventariada
    quantity TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE,
    FOREIGN KEY (part_id) REFERENCES parts (id) ON DELETE RESTRICT,
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_part_id (part_id),
    CHECK (
        part_id IS NOT NULL
        OR part_name IS NOT NULL
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: expenses
-- Descripción: Registra gastos operativos con categoría y fechas para análisis y reportes mensuales.
-- Requisitos relacionados: REQ-045, REQ-046, REQ-047, REQ-048
-- ===========================================================
DROP TABLE IF EXISTS expenses;

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL, -- Concepto/descripción del gasto
    amount DECIMAL(10, 2) NOT NULL, -- Monto del gasto
    category ENUM(
        'VEHICLE_PURCHASE',
        'SERVICES',
        'RENT',
        'SALARIES',
        'MAINTENANCE',
        'UTILITIES',
        'OTHER'
    ) NOT NULL, -- Categoría del gasto
    pay_before DATE NOT NULL, -- Fecha del gasto (para cálculos mensuales)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de registro
    payment_at DATE, -- Fecha de pago efectiva
    created_by INT NULL, -- Usuario que registró el gasto
    FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_expense_date (pay_before),
    INDEX idx_category (category),
    INDEX idx_created_by (created_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

ALTER TABLE tickets
   ADD COLUMN discount DECIMAL(10, 2);