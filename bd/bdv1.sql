-- ===========================================================
-- Script de creación de base de datos: mpgv1
-- Deshuace Garcia - Sistema de gestión de inventario de piezas
-- ===========================================================

DROP DATABASE IF EXISTS mpgv1;

CREATE DATABASE mpgv1;

USE mpgv1;

-- ===========================================================
-- TABLA: brands
-- Descripción: Almacena las marcas de vehículos disponibles
-- ===========================================================
DROP TABLE IF EXISTS brands;

CREATE TABLE brands (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada marca
    name VARCHAR(50) NOT NULL UNIQUE, -- Nombre de la marca (Ejemplo: 'TOYOTA', 'FORD', 'HONDA')
    INDEX idx_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: models
-- Descripción: Almacena los modelos de vehículos por marca
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
    FOREIGN KEY (brand_id) REFERENCES brands (id) ON DELETE CASCADE, -- Relación con la tabla 'brands'
    INDEX idx_brand_id (brand_id), -- Índice para mejorar rendimiento de búsquedas
    INDEX idx_year (year) -- Índice para búsquedas por año
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: model_images
-- Descripción: Imágenes asociadas a modelos de vehículos
-- ===========================================================
CREATE TABLE model_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES models (id) ON DELETE CASCADE,
    INDEX idx_model_id (model_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: part_categories
-- Descripción: Categorías de piezas del vehículo
-- ===========================================================
DROP TABLE IF EXISTS part_categories;

-- CREATE TABLE part_categories (
--    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada categoría
--    category ENUM(
--        'COLLISION', -- Piezas de colisión (parachoques, guardabarros)
--        'CHASSIS', -- Piezas del chasis
--       'ENGINE', -- Piezas del motor
--      'TRANSMISSION', -- Piezas de transmisión
--        'SUSPENSION', -- Piezas de suspensión
--        'BRAKES', -- Piezas de frenos
--        'INSIDE', -- Piezas interiores
--        'ELECTRICAL', -- Piezas eléctricas
--        'OTHER' -- Otras piezas
--    ) NOT NULL -- Categoría de la pieza
-- ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: parts
-- Descripción: Almacena las piezas disponibles en el inventario
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
-- Descripción: Imágenes asociadas a piezas
-- ===========================================================
CREATE TABLE parts_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    part_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
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
-- TABLA: tickets
-- Descripción: Tickets de venta generados
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
CREATE TABLE ticket_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_folio CHAR(36) NOT NULL, -- Folio del ticket
    image_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE,
    INDEX idx_ticket_folio (ticket_folio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: sales
-- Descripción: Detalle de las ventas por ticket
-- ===========================================================
DROP TABLE IF EXISTS sales;

CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada registro de venta
    ticket_folio CHAR(36) NOT NULL, -- Folio del ticket asociado (relacionado con la tabla 'tickets')
    part_id INT, -- ID de la pieza vendida (relacionado con la tabla 'parts')
    quantity TINYINT DEFAULT 1, -- Cantidad de piezas vendidas
    price DECIMAL(10, 2) NOT NULL, -- Precio unitario de la pieza al momento de la venta
    has_warranty BIT DEFAULT 0, -- Indica si tiene garantía
    warranty_status ENUM(
        'ACTIVE', -- El problema fue atendido
        'REJECTED', -- El problema fue rechazado
        'EXPIRED', -- No se encontró el problema reportado
        'PENDING' -- Problema pendiente de atención
    ) DEFAULT 'PENDING', -- Estado de la garantía
    warranty_expiration_date DATE, -- Fecha de expiración de garantía
    part_name VARCHAR(200), -- Pieza no inventariada 
    FOREIGN KEY (ticket_folio) REFERENCES tickets (folio) ON DELETE CASCADE, -- Relación con la tabla 'tickets'
    FOREIGN KEY (part_id) REFERENCES parts (id) ON DELETE CASCADE, -- Relación con la tabla 'parts'
    INDEX idx_ticket_folio (ticket_folio), -- Índice para consulta de ventas por ticket
    INDEX idx_part_id (part_id), -- Índice para reportes de piezas más vendidas
    INDEX idx_warranty (has_warranty, warranty_status),
    CHECK (
        part_id IS NOT NULL
        OR part_name IS NOT NULL
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: customer_invoice_data
-- Descripción: Datos fiscales de clientes para facturación
-- ===========================================================
DROP TABLE IF EXISTS customer_invoice_data;

CREATE TABLE customer_invoice_data (
    rfc VARCHAR(13) PRIMARY KEY NOT NULL, -- RFC único para cada cliente
    business_name VARCHAR(255) NOT NULL, -- Razón social
    address VARCHAR(255) NOT NULL, -- Dirección fiscal completa del cliente
    postal_code VARCHAR(6) NOT NULL, -- Código postal del domicilio fiscal
    tax_regime VARCHAR(50) NOT NULL, -- Régimen fiscal del cliente
    invoice_use VARCHAR(50) NOT NULL, -- Uso del CFDI
    email VARCHAR(100) NOT NULL, -- Correo electrónico para envío de facturas
    INDEX idx_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: invoices
-- Descripción: Facturas emitidas relacionadas con tickets
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
-- TABLA: customer
-- Descripción: Información de clientes del negocio
-- ===========================================================
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único para cada cliente
    name VARCHAR(100) NOT NULL, -- Nombre completo o razón social del cliente
    phone VARCHAR(20) NOT NULL, -- Número telefónico de contacto
    rfc VARCHAR(13), -- RFC del cliente
    FOREIGN KEY (rfc) REFERENCES customer_invoice_data (rfc) ON DELETE SET NULL, -- Relación con la tabla 'customer_invoice_data'
    INDEX idx_phone (phone), -- Índice para búsqueda por teléfono
    INDEX idx_rfc (rfc) -- Índice para búsqueda por RFC
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- TABLA: customer_issues
-- Descripción: Registro de problemas reportados por clientes
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
    customer_id INT, -- ID del cliente que reporta el problema
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE SET NULL -- Relación con la tabla 'customer'
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
-- TABLA: warranty_claim
-- Evidencia fotográfica de reclamaciones de garantía
-- Debe dejar el precio de la venta en 0 cando sea RETURN y modificar el total del ticked
-- ===========================================================
CREATE TABLE warranty_claim (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    description VARCHAR(255) COMMENT 'Descripción del defecto mostrado',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    claim_type ENUM('RETURN', 'EXCHANGE') NOT NULL COMMENT 'Tipo de garantía: devolución o cambio',
    FOREIGN KEY (sale_id) REFERENCES sales (id) ON DELETE CASCADE,
    INDEX idx_sale_id (sale_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ============================
-- DATOS DE PRUEBA
-- ============================

-- Marcas de vehículos
INSERT INTO brands (name) VALUES ('TOYOTA'), ('FORD'), ('HONDA');

-- Modelos de vehículos
INSERT INTO
    models (
        brand_id,
        serial_number,
        name,
        year,
        transmission,
        engine,
        vehicle_class
    )
VALUES (
        1,
        'JTDKB20U487500102',
        'Corolla',
        2020,
        'AUTOMATIC',
        '4 cilindros',
        'Sedan'
    ),
    (
        2,
        '1FAFP404X1F123456',
        'Mustang',
        2021,
        'STANDARD',
        'V6',
        'Coupe'
    ),
    (
        3,
        '2HGFG12896H123456',
        'Civic',
        2019,
        'AUTOMATIC',
        '4 cilindros',
        'Sedan'
    );

-- Imágenes de modelos
INSERT INTO
    model_images (model_id, image_url)
VALUES (
        1,
        'https://ejemplo.com/corolla.jpg'
    ),
    (
        2,
        'https://ejemplo.com/mustang.jpg'
    ),
    (
        3,
        'https://ejemplo.com/civic.jpg'
    );

-- Piezas en inventario
INSERT INTO
    parts (
        code,
        name,
        side,
        category_type,
        color,
        price,
        quantity,
        model_id
    )
VALUES (
        'ALT-001',
        'Alternador',
        'UNIDIRECTIONAL',
        'ENGINE',
        'Negro',
        1200.00,
        3,
        1
    ),
    (
        'PAR-002',
        'Parachoques delantero',
        'FORWARD',
        'COLLISION',
        'Blanco',
        2500.00,
        2,
        2
    ),
    (
        'BRA-003',
        'Pastillas de freno',
        'UNIDIRECTIONAL',
        'BRAKES',
        'Gris',
        600.00,
        5,
        3
    );

-- Imágenes de piezas
INSERT INTO
    parts_images (part_id, image_url)
VALUES (
        1,
        'https://ejemplo.com/alternador.jpg'
    ),
    (
        2,
        'https://ejemplo.com/parachoques.jpg'
    ),
    (
        3,
        'https://ejemplo.com/pastillas.jpg'
    );

-- Roles de usuario
INSERT INTO
    roles (id, role)
VALUES (1, 'USER'),
    (2, 'ADMIN'),
    (3, 'SELLER'),
    (4, 'ACCOUNTANT'),
    (5, 'INVENTORY_MANAGER');

-- Usuarios del sistema
INSERT INTO
    users (
        name,
        role_id,
        username,
        password
    )
VALUES (
        'Juan Pérez',
        2,
        'admin',
        '$2b$10$abcdefg'
    ),
    (
        'Ana López',
        3,
        'vendedor1',
        '$2b$10$hijklmn'
    ),
    (
        'Carlos Ruiz',
        1,
        'usuario',
        '$2b$10$opqrstu'
    );

-- Datos fiscales de clientes
INSERT INTO
    customer_invoice_data (
        rfc,
        business_name,
        address,
        postal_code,
        tax_regime,
        invoice_use,
        email
    )
VALUES (
        'XAXX010101000',
        'Empresa S.A.',
        'Calle Falsa 123, CDMX',
        '01000',
        '601 - General de Ley Personas Morales',
        'G03 - Gastos en general',
        'cliente@correo.com'
    );

-- Clientes
INSERT INTO
    customer (name, phone, rfc)
VALUES (
        'Empresa S.A.',
        '555-1234',
        'XAXX010101000'
    ),
    (
        'Pedro Gómez',
        '555-5678',
        NULL
    );

-- Tickets de venta
INSERT INTO
    tickets (
        user_id,
        total,
        payment_method,
        items
    )
VALUES (2, 3100.00, 'CASH', 2),
    (3, 600.00, 'CREDIT_CARD', 1);

-- Imágenes de tickets
INSERT INTO
    ticket_images (ticket_folio, image_url)
VALUES (
        (
            SELECT folio
            FROM tickets
            LIMIT 1
        ),
        'https://ejemplo.com/ticket1.jpg'
    ),
    (
        (
            SELECT folio
            FROM tickets
            ORDER BY date DESC
            LIMIT 1
        ),
        'https://ejemplo.com/ticket2.jpg'
    );

-- Ventas (asociadas a los tickets generados)
INSERT INTO
    sales (
        ticket_folio,
        part_id,
        quantity,
        price
    )
VALUES (
        (
            SELECT folio
            FROM tickets
            LIMIT 1
        ),
        1,
        1,
        1200.00
    ),
    (
        (
            SELECT folio
            FROM tickets
            LIMIT 1
        ),
        2,
        1,
        2500.00
    ),
    (
        (
            SELECT folio
            FROM tickets
            ORDER BY date DESC
            LIMIT 1
        ),
        3,
        1,
        600.00
    );

-- Facturas
INSERT INTO
    invoices (
        ticket_folio,
        invoice_number,
        receiver_customer,
        url_document
    )
VALUES (
        (
            SELECT folio
            FROM tickets
            LIMIT 1
        ),
        'A1B2C3D4E5F6',
        'XAXX010101000',
        'https://ejemplo.com/factura1.xml'
    );

-- Logs de inicio de sesión
INSERT INTO
    login_logs (
        user_id,
        ip_address,
        user_agent,
        role_snapshot
    )
VALUES (
        2,
        '192.168.1.10',
        'Mozilla/5.0',
        'ADMIN'
    ),
    (
        3,
        '192.168.1.11',
        'Chrome/90.0',
        'SELLER'
    );

-- Logs de aplicación
INSERT INTO
    app_logs (
        level,
        message,
        user_id,
        login_log_id,
        context,
        path
    )
VALUES (
        'INFO',
        'Inicio de sesión exitoso',
        2,
        1,
        'Login',
        '/api/login'
    ),
    (
        'ERROR',
        'Error al crear venta',
        3,
        2,
        'Venta',
        '/api/sales/create'
    );

-- Problemas reportados por clientes
INSERT INTO
    customer_issues (problem, status, customer_id)
VALUES (
        'No recibí mi factura',
        'PENDING',
        1
    ),
    (
        'La pieza entregada está dañada',
        'ATTENDED',
        2
    ),
    (
        'No se encontró el modelo solicitado',
        'NOT_FOUND',
        1
    ),
    (
        'Solicitud de devolución rechazada',
        'REJECTED',
        2
    );

-- Reclamaciones de garantía
INSERT INTO
    warranty_claim (
        sale_id,
        image_url,
        description,
        claim_type
    )
VALUES (
        1,
        'https://ejemplo.com/garantia1.jpg',
        'Defecto en alternador',
        'RETURN'
    ),
    (
        2,
        'https://ejemplo.com/garantia2.jpg',
        'Parachoques con rayón',
        'EXCHANGE'
    );