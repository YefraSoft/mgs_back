# Multipartes Garcia api

Esta api, será consumida desde una app de MAUI, además de ser consumida por una página web, pero únicamente
solo para consultar información de inventario piezas etc.

## TASK CONTEXT

1. Antes de cualquier tarea debes revisar este documento
2. Cada modificacion debe registrarse el el apartado LOGs
   - Debe estar detallado a formato de lista, con acciones consisas y docuemtos modificados.
3. Cuando realizes un endpoint nuevo, debes agregarlo al apartado de rutas.
   - Debe tener la ruta, el metodo y el nombre del endpoint.
   - Debe tener un ejemplo de la peticion y la respuesta.
4. Se deben actualizar los archivos como docs readme etc.

## DATABASE CONTEXT

Esta es la base de datos donde se conecta la API

```sql
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
--) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

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
```

## LOGs

### 2025-10-12 - Alineación de Entidades con la Base de Datos

#### Resumen

- Se corrigieron entidades existentes y se agregaron entidades faltantes para reflejar el esquema actualizado de la BD. No se agregaron endpoints.

#### Cambios Realizados

- **Part.kt** (`src/main/kotlin/api/multipartes/dev/models/Part.kt`)
  - Reemplazado `partCategory` por `categoryType: CategoryType` mapeado a columna `category_type`.
  - Agregado `model: Model?` con `@JoinColumn(name = "model_id")`.
  - Agregado `createdAt: LocalDateTime` mapeado a `created_at`.

- **Sale.kt** (`src/main/kotlin/api/multipartes/dev/models/Sale.kt`)
  - `part` ahora es opcional y se agregó `partName` (para piezas no inventariadas).
  - Agregados campos de garantía: `hasWarranty`, `warrantyStatus`, `warrantyExpirationDate`.

- **Invoice.kt** (`src/main/kotlin/api/multipartes/dev/models/Invoice.kt`)
  - Agregado `createdAt` mapeado a `created_at`.

- **CustomerInvoiceData.kt** (`src/main/kotlin/api/multipartes/dev/models/CustomerInvoiceData.kt`)
  - Agregado `businessName` mapeado a `business_name`.

- **Nuevas Entidades**
  - `ModelImage.kt` → tabla `model_images`.
  - `PartImage.kt` → tabla `parts_images`.
  - `TicketImage.kt` → tabla `ticket_images`.
  - `WarrantyClaim.kt` → tabla `warranty_claim`.

- **Nuevos Enums y Convertidores**
  - `WarrantyStatus.kt` (ACTIVE, REJECTED, EXPIRED, PENDING).
  - `ClaimType.kt` (RETURN, EXCHANGE).
  - `SideTypeConverter.kt` (mapeo de `SideType` a valores con guión en BD).

#### Archivos Creados/Modificados

- **Creados (7):**
  - `src/main/kotlin/api/multipartes/dev/models/ModelImage.kt`
  - `src/main/kotlin/api/multipartes/dev/models/PartImage.kt`
  - `src/main/kotlin/api/multipartes/dev/models/TicketImage.kt`
  - `src/main/kotlin/api/multipartes/dev/models/WarrantyClaim.kt`
  - `src/main/kotlin/api/multipartes/dev/enums/WarrantyStatus.kt`
  - `src/main/kotlin/api/multipartes/dev/enums/ClaimType.kt`
  - `src/main/kotlin/api/multipartes/dev/persistence/SideTypeConverter.kt`

- **Modificados (4):**
  - `src/main/kotlin/api/multipartes/dev/models/Part.kt`
  - `src/main/kotlin/api/multipartes/dev/models/Sale.kt`
  - `src/main/kotlin/api/multipartes/dev/models/Invoice.kt`
  - `src/main/kotlin/api/multipartes/dev/models/CustomerInvoiceData.kt`

> Nota: La entidad `PartCategory.kt` queda obsoleta respecto al esquema actual (la tabla fue eliminada). No se removió el archivo para evitar romper referencias; puede retirarse en una limpieza posterior si ya no se usa.

### 2025-01-11 - Mejoras de Seguridad Críticas

#### Resumen de Mejoras Implementadas

Se han implementado mejoras críticas de seguridad para proteger la API contra ataques comunes y exposición de información sensible.

#### 1. Validación de JWT Secret al Inicio

**Archivo Creado:** `src/main/kotlin/api/multipartes/dev/config/JwtSecretValidator.kt`

- **Validación automática** con `@PostConstruct` antes de que la aplicación inicie
- **Validaciones implementadas:**
  - Secret no puede estar vacío o en blanco
  - Debe tener al menos 32 caracteres de longitud
  - No puede usar valores comunes/default ("default", "secret", "changeme")
- **Comportamiento:** La aplicación **NO INICIA** si el secret no cumple los requisitos
- **Mensaje de confirmación:** Imprime longitud del secret si la validación pasa

#### 2. Rate Limiting en Login (Protección contra Fuerza Bruta)

**Archivo Creado:** `src/main/kotlin/api/multipartes/dev/config/RateLimitService.kt`

- **Librería utilizada:** Bucket4j con cache Caffeine
- **Límite de login:** 5 intentos por minuto por dirección IP
- **Límite general de API:** 100 requests por minuto por IP (implementado para uso futuro)
- **Características:**
  - Cache automático de buckets con expiración de 1 hora
  - Capacidad máxima de 100,000 IPs diferentes
  - Respuesta HTTP 429 (Too Many Requests) cuando se excede el límite
  - Incluye tiempo de espera en segundos en la respuesta de error

**Modificado:** `src/main/kotlin/api/multipartes/dev/endPoints/auth/AuthController.kt`

- Integrado `RateLimitService` en el endpoint de login
- Extracción inteligente de IP considerando proxies (X-Forwarded-For, X-Real-IP)
- Respuesta con código 429 y tiempo de espera cuando se excede el límite

#### 3. ExceptionHandler Mejorado (Sin Exposición de Información Sensible)

**Modificado:** `src/main/kotlin/api/multipartes/dev/config/GlobalExceptionHandler.kt`

**Mejoras implementadas:**

- **Logging estructurado:** Uso de SLF4J Logger para registrar errores internamente
- **Sanitización de mensajes:** Función `sanitizeMessage()` que redacta información sensible:
  - Passwords → `[REDACTED]`
  - Tokens → `[REDACTED]`
  - Secrets → `[REDACTED]`
  - API Keys → `[REDACTED]`
- **Manejo de validaciones:** Nuevo handler `MethodArgumentNotValidException` que retorna errores de validación estructurados
- **Manejo de permisos:** Nuevo handler `AccessDeniedException` con respuesta HTTP 403
- **Mensajes genéricos:** Los errores 500 ya no exponen stack traces al cliente
- **ErrorResponse mejorado:** Tipo de mensaje no nullable para evitar null en respuestas

#### 4. Validaciones contra Inyecciones SQL

**Dependencia agregada:** `spring-boot-starter-validation` en `pom.xml`

**DTOs validados completamente en:** `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

**LoginRequest:**

- `@NotBlank`, `@Size(min=3, max=50)`, `@Pattern` para username (solo alfanuméricos, guiones y underscores)
- `@NotBlank`, `@Size(min=6)` para password

**RegisterRequest:**

- `@NotBlank`, `@Size(min=2, max=50)` para name
- `@NotBlank`, `@Size(min=3, max=50)`, `@Pattern` para username
- `@NotBlank`, `@Size(min=8)` para password (incrementado de 6 a 8 caracteres)
- `@Min(1)`, `@Max(5)` para roleId

**CreateBrandRequest / UpdateBrandRequest:**

- `@NotBlank`, `@Size(min=2, max=50)`, `@Pattern` para name (solo letras, números, espacios y guiones)

**CreateModelRequest / UpdateModelRequest:**

- `@Min(1)` para brandId
- `@Size(max=100)` para serialNumber
- `@NotBlank`, `@Size(min=1, max=50)` para name, engine, vehicleClass
- `@Min(1900)`, `@Max(2100)` para year
- `@Pattern` para transmission (solo AUTOMATIC o STANDARD)

**CreateCustomerRequest / UpdateCustomerRequest:**

- `@NotBlank`, `@Size(min=2, max=100)` para name
- `@NotBlank`, `@Pattern(10-20 dígitos)` para phone
- `@Size(min=12, max=13)`, `@Pattern(RFC mexicano válido)` para rfc

**CreateCustomerIssueRequest / UpdateCustomerIssueRequest:**

- `@NotBlank`, `@Size(min=10, max=255)` para problem
- `@Pattern` para status (solo ATTENDED, REJECTED, NOT_FOUND, PENDING)
- `@Min(1)` para customerId

**Integración en Controllers:**

- Agregado `@Valid` en todos los `@RequestBody` de AuthController
- Los errores de validación son manejados automáticamente por `GlobalExceptionHandler`

#### 5. Funciones Sin Usar Removidas

**Removido de:** `src/main/kotlin/api/multipartes/dev/JwtService.kt`

- `extractUserId(token: String)` - Nunca utilizada en el proyecto
- `extractExpiration(token: String)` - Nunca utilizada en el proyecto

**Removido de:** `src/main/kotlin/api/multipartes/dev/config/JwtFilter.kt`

- `override fun destroy()` - Método deprecated y vacío

#### Dependencias Agregadas al pom.xml

1. **spring-boot-starter-validation** - Validación de DTOs con anotaciones Jakarta
2. **bucket4j-core (v8.10.1)** - Rate limiting con algoritmo token bucket
3. **caffeine** - Cache de alto rendimiento para almacenar buckets de rate limiting

#### Resumen de Archivos Creados/Modificados

**Archivos Creados (2):**

1. `src/main/kotlin/api/multipartes/dev/config/JwtSecretValidator.kt`
2. `src/main/kotlin/api/multipartes/dev/config/RateLimitService.kt`

**Archivos Modificados (5):**

1. `pom.xml` - Agregadas 4 dependencias de seguridad y validación
2. `src/main/kotlin/api/multipartes/dev/config/GlobalExceptionHandler.kt` - Sanitización y logging mejorado
3. `src/main/kotlin/api/multipartes/dev/endPoints/auth/AuthController.kt` - Rate limiting integrado
4. `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt` - Validaciones completas en todos los DTOs
5. `src/main/kotlin/api/multipartes/dev/JwtService.kt` - Removidas funciones sin usar

---

### 2025-01-11 - Creación de Endpoints para Customers y Customer Issues

#### Nuevos Endpoints Implementados

Se han creado endpoints completos (CRUD) para administrar clientes (customers) y problemas de clientes (customer_issues).

#### Enum Actualizado

- **RoleType.kt**
  - Agregado valor: `INVENTORY_MANAGER` (Gestor de inventario)
  - Modificado: `src/main/kotlin/api/multipartes/dev/enums/RoleType.kt`

#### Enum Creado

- **IssueStatus.kt**
  - Valores: `ATTENDED, REJECTED, NOT_FOUND, PENDING`
  - Creado: `src/main/kotlin/api/multipartes/dev/enums/IssueStatus.kt`

#### Modelo Creado

- **CustomerIssue.kt**
  - Nuevo modelo para tabla `customer_issues`
  - Campos: `id, problem, status, createdAt, customer`
  - Relación ManyToOne con Customer
  - Creado: `src/main/kotlin/api/multipartes/dev/models/CustomerIssue.kt`

#### DTOs Creados

- **CustomerResponse** - Respuesta de cliente con información básica
- **CreateCustomerRequest** - Request para crear nuevo cliente
- **UpdateCustomerRequest** - Request para actualizar cliente existente
- **CustomerIssueResponse** - Respuesta de problema con información completa
- **CreateCustomerIssueRequest** - Request para crear nuevo problema
- **UpdateCustomerIssueRequest** - Request para actualizar problema existente
- Modificado: `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

#### Repositorios Creados

- **CustomerRepository.kt**

  - Métodos: `findByPhone`, `findByRfc`, `findByNameContainingIgnoreCase`, `existsByPhone`
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerRepository.kt`

- **CustomerIssueRepository.kt**
  - Métodos: `findByStatus`, `findByCustomerId`, `findByStatusAndCustomerId`
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueRepository.kt`

#### Servicios Creados

- **CustomerService.kt**

  - CRUD completo para clientes
  - Métodos: `getAllCustomers`, `getCustomerById`, `getCustomerByPhone`, `getCustomerByRfc`, `searchCustomersByName`, `createCustomer`, `updateCustomer`, `deleteCustomer`
  - Cache habilitado con `@Cacheable("customers")` en getAllCustomers
  - Cache eviction con `@CacheEvict` en create, update y delete
  - Validación de teléfonos duplicados y RFC existente en invoice_data
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerService.kt`

- **CustomerIssueService.kt**
  - CRUD completo para problemas de clientes
  - Métodos: `getAllIssues`, `getIssueById`, `getIssuesByStatus`, `getIssuesByCustomer`, `getIssuesByStatusAndCustomer`, `createIssue`, `updateIssue`, `deleteIssue`
  - Cache habilitado con `@Cacheable("customer-issues")` en getAllIssues
  - Cache eviction con `@CacheEvict` en create, update y delete
  - Validación de customer_id y status type
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueService.kt`

#### Controladores Creados

- **CustomerController.kt**

  - Endpoints REST para gestión de clientes
  - Rutas: GET /api/customers, GET /api/customers/{id}, GET /api/customers/search/by-phone/{phone}, GET /api/customers/search/by-rfc/{rfc}, GET /api/customers/search/by-name, POST /api/customers, PUT /api/customers/{id}, DELETE /api/customers/{id}
  - Manejo de errores con ResponseEntity
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerController.kt`

- **CustomerIssueController.kt**
  - Endpoints REST para gestión de problemas de clientes
  - Rutas: GET /api/customer-issues, GET /api/customer-issues/{id}, GET /api/customer-issues/search/by-status/{status}, GET /api/customer-issues/search/by-customer/{customerId}, GET /api/customer-issues/search/by-status-and-customer, POST /api/customer-issues, PUT /api/customer-issues/{id}, DELETE /api/customer-issues/{id}
  - Manejo de errores con ResponseEntity
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueController.kt`

#### Resumen de Archivos Creados/Modificados

**Archivos Creados (7):**

1. `src/main/kotlin/api/multipartes/dev/enums/IssueStatus.kt`
2. `src/main/kotlin/api/multipartes/dev/models/CustomerIssue.kt`
3. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerRepository.kt`
4. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueRepository.kt`
5. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerService.kt`
6. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueService.kt`
7. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerController.kt`
8. `src/main/kotlin/api/multipartes/dev/endPoints/customers/CustomerIssueController.kt`

**Archivos Modificados (4):**

1. `src/main/kotlin/api/multipartes/dev/enums/RoleType.kt`
2. `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`
3. `src/main/resources/application-dev.yml`
4. `src/main/resources/application-prod.yml`

---

### 2025-01-11 - Creación de Endpoints para Brands y Models

#### Nuevos Endpoints Implementados

Se han creado endpoints completos (CRUD) para administrar marcas de vehículos (brands) y modelos de vehículos (models).

#### DTOs Creados

- **BrandResponse** - Respuesta de marca con id y nombre
- **CreateBrandRequest** - Request para crear nueva marca
- **UpdateBrandRequest** - Request para actualizar marca existente
- **ModelResponse** - Respuesta de modelo con información completa incluyendo marca
- **CreateModelRequest** - Request para crear nuevo modelo
- **UpdateModelRequest** - Request para actualizar modelo existente
- Modificado: `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

#### Repositorios Creados

- **BrandRepository.kt**

  - Métodos: `existsByName`, `findByNameContainingIgnoreCase`
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandRepository.kt`

- **ModelRepository.kt**
  - Métodos: `findByBrandId`, `findByYear`, `findByNameContainingIgnoreCase`, `findByBrandIdAndYear`
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelRepository.kt`

#### Servicios Creados

- **BrandService.kt**

  - CRUD completo para marcas
  - Métodos: `getAllBrands`, `getBrandById`, `searchBrandsByName`, `createBrand`, `updateBrand`, `deleteBrand`
  - Cache habilitado con `@Cacheable("brands")` en getAllBrands
  - Cache eviction con `@CacheEvict` en create, update y delete
  - Validación de nombres duplicados
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandService.kt`

- **ModelService.kt**
  - CRUD completo para modelos
  - Métodos: `getAllModels`, `getModelById`, `getModelsByBrand`, `getModelsByYear`, `searchModelsByName`, `getModelsByBrandAndYear`, `createModel`, `updateModel`, `deleteModel`
  - Cache habilitado con `@Cacheable("models")` en getAllModels
  - Cache eviction con `@CacheEvict` en create, update y delete
  - Validación de brand_id y transmission type
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelService.kt`

#### Controladores Creados

- **BrandController.kt**

  - Endpoints REST para gestión de marcas
  - Rutas: GET /api/brands, GET /api/brands/{id}, GET /api/brands/search, POST /api/brands, PUT /api/brands/{id}, DELETE /api/brands/{id}
  - Manejo de errores con ResponseEntity
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandController.kt`

- **ModelController.kt**
  - Endpoints REST para gestión de modelos
  - Rutas: GET /api/models, GET /api/models/{id}, GET /api/models/search/by-brand/{brandId}, GET /api/models/search/by-year/{year}, GET /api/models/search/by-name, GET /api/models/search/by-brand-and-year, POST /api/models, PUT /api/models/{id}, DELETE /api/models/{id}
  - Manejo de errores con ResponseEntity
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelController.kt`

#### Resumen de Archivos Creados/Modificados

**Archivos Creados (6):**

1. `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandRepository.kt`
2. `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandService.kt`
3. `src/main/kotlin/api/multipartes/dev/endPoints/brands/BrandController.kt`
4. `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelRepository.kt`
5. `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelService.kt`
6. `src/main/kotlin/api/multipartes/dev/endPoints/models/ModelController.kt`

**Archivos Modificados (1):**

1. `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

---

### 2025-01-11 - Corrección y Reestructuración de Endpoints Existentes

#### DTOs Reestructurados

- **allDTO.kt**
  - Eliminados: `SaleRequest` y `SaleResponse` antiguos
  - Creados:
    - `CreateTicketRequest` - Para crear tickets completos con sus items
    - `TicketItemRequest` - Items individuales del ticket
    - `TicketResponse` - Respuesta completa de ticket con ventas
    - `SaleResponse` - Respuesta de venta simplificada (vinculada a ticket)
    - `PartResponse` - Respuesta de parte con información de categoría
  - Modificado: `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

#### Repositorios Creados

- **TicketRepository.kt**
  - Repositorio para gestión de tickets
  - Métodos: `findByUserId`, `findByDateBetween`
  - Creado: `src/main/kotlin/api/multipartes/dev/repositories/TicketRepository.kt`

#### Servicios Modificados/Creados

- **TicketService.kt**

  - Nuevo servicio completo para gestión de tickets
  - Métodos:
    - `createTicket` - Crea ticket y sus ventas transaccionalmente
    - `getTicketByFolio` - Obtiene ticket con sus ventas
    - `getAllTickets` - Lista todos los tickets
    - `getTicketsByUser` - Filtra tickets por vendedor
    - `getTicketsByDateRange` - Filtra tickets por rango de fechas
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketService.kt`

- **SalesService.kt**

  - Reestructurado completamente para adaptarse al sistema de tickets
  - Eliminados métodos: `createSale`, `updateSale`, `deleteSale`, `getSalesByDateRange`
  - Métodos actualizados:
    - `getAllSales` - Retorna `List<SaleResponse>`
    - `getSalesByPart` - Retorna `List<SaleResponse>`
  - Nuevos métodos:
    - `getSalesByTicket` - Obtiene ventas de un ticket específico
  - Las ventas ahora solo se crean a través de tickets
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesService.kt`

- **PartsService.kt**
  - Actualizado para retornar `PartResponse` en lugar de `Part`
  - Métodos actualizados:
    - `findAll()` - Retorna `List<PartResponse>` con información de categoría
    - `findById()` - Retorna `PartResponse?` con información de categoría
  - Agregada anotación `@CacheEvict` en `deleteById`
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/parts/PartsService.kt`

#### Controladores Modificados/Creados

- **TicketController.kt**

  - Nuevo controlador completo para gestión de tickets
  - Endpoints:
    - `POST /api/tickets` - Crear nuevo ticket con ventas
    - `GET /api/tickets` - Listar todos los tickets
    - `GET /api/tickets/{folio}` - Obtener ticket por folio
    - `GET /api/tickets/user/{userId}` - Tickets por vendedor
    - `GET /api/tickets/search/by-date` - Tickets por rango de fechas
  - Creado: `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketController.kt`

- **SalesController.kt**

  - Reestructurado para adaptarse al nuevo sistema
  - Endpoints eliminados:
    - `POST /api/sales` - Las ventas se crean via tickets
    - `PUT /api/sales/{id}` - Las ventas no se modifican directamente
    - `DELETE /api/sales/{id}` - Las ventas no se eliminan directamente
    - `GET /api/sales/search/by-date` - Ahora se consulta via tickets
  - Endpoints actualizados:
    - `GET /api/sales` - Retorna `List<SaleResponse>`
    - `GET /api/sales/{id}` - Retorna `Sale` individual
    - `GET /api/sales/search/by-part/{partId}` - Retorna `List<SaleResponse>`
  - Endpoints nuevos:
    - `GET /api/sales/search/by-ticket/{ticketFolio}` - Ventas de un ticket
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesController.kt`

- **PartsController.kt**
  - Actualizado para retornar `PartResponse`
  - Endpoints actualizados:
    - `GET /api/parts` - Retorna `List<PartResponse>`
    - `GET /api/parts/{id}` - Retorna `PartResponse`
  - Endpoints de creación/actualización mantienen `Part` como entrada
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/parts/PartsController.kt`

#### Repositorios Actualizados

- **SalesRepo.kt**
  - Eliminado método obsoleto: `findByCreatedAtBetween` (se consulta via tickets)
  - Agregado método: `findByTicketFolio(ticketFolio: String): List<Sale>`
  - Mantenido método: `findByPartId(partId: Int): List<Sale>`
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesRepo.kt`

#### Resumen de Archivos Modificados/Creados

**Archivos Creados (3):**

1. `src/main/kotlin/api/multipartes/dev/repositories/TicketRepository.kt`
2. `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketService.kt`
3. `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketController.kt`

**Archivos Modificados (6):**

1. `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`
2. `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesRepo.kt`
3. `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesService.kt`
4. `src/main/kotlin/api/multipartes/dev/endPoints/sales/SalesController.kt`
5. `src/main/kotlin/api/multipartes/dev/endPoints/parts/PartsService.kt`
6. `src/main/kotlin/api/multipartes/dev/endPoints/parts/PartsController.kt`

### 2025-01-11 - Corrección de Modelos para Alineación con BD

#### Problemas Identificados

1. **VehicleBrands.kt** - Campo `created_at` no existe en tabla `brands`
2. **User.kt** - Campo `created_at` no existe en tabla `users`
3. **Invoice.kt y Ticket.kt** - Tipo de columna `folio` incorrecta (VARCHAR vs CHAR)

#### Modelos Corregidos

- **VehicleBrands.kt**

  - Eliminado campo `createdAt: LocalDateTime`
  - Eliminado import `java.time.LocalDateTime`
  - Agregadas constraints: `nullable = false, length = 50` al campo `name`
  - Modificado: `src/main/kotlin/api/multipartes/dev/models/VehicleBrands.kt`

- **User.kt**

  - Eliminado campo `createdAt: LocalDateTime`
  - Eliminado import `java.time.LocalDateTime`
  - Agregadas constraints a campos: `name` (length = 50), `username` (length = 50), `password` (nullable = false)
  - Modificado `role_id` a nullable (permite SET NULL en BD)
  - Modificado: `src/main/kotlin/api/multipartes/dev/models/User.kt`

- **Ticket.kt**

  - Cambiado tipo de columna `folio` de `@Column(length = 36)` a `@Column(columnDefinition = "CHAR(36)")`
  - Alineado con definición BD que usa `CHAR(36)` en lugar de VARCHAR
  - Modificado: `src/main/kotlin/api/multipartes/dev/models/Ticket.kt`

- **Invoice.kt**
  - Cambiado tipo de columna `folio` de `@Column(length = 36)` a `@Column(columnDefinition = "CHAR(36)")`
  - Alineado con definición BD que usa `CHAR(36)` en lugar de VARCHAR
  - Modificado: `src/main/kotlin/api/multipartes/dev/models/Invoice.kt`

#### Resumen de Archivos Modificados

**Archivos Modificados (4):**

1. `src/main/kotlin/api/multipartes/dev/models/VehicleBrands.kt`
2. `src/main/kotlin/api/multipartes/dev/models/User.kt`
3. `src/main/kotlin/api/multipartes/dev/models/Ticket.kt`
4. `src/main/kotlin/api/multipartes/dev/models/Invoice.kt`

---

### 2025-01-11 - Agregado Campo payment_method a Tickets

#### Enumeración Creada

- **PaymentMethod.kt**
  - Nuevo enum para métodos de pago
  - Valores: `CASH, CREDIT_CARD, DEBIT_CARD, CASH_ON_DELIVERY, OTHER`
  - Creado: `src/main/kotlin/api/multipartes/dev/enums/PaymentMethod.kt`

#### Modelo Actualizado

- **Ticket.kt**
  - Agregado campo `paymentMethod: PaymentMethod`
  - Anotación `@Enumerated(EnumType.STRING)`
  - Almacenado en columna `payment_method` (no nullable)
  - Modificado: `src/main/kotlin/api/multipartes/dev/models/Ticket.kt`

#### DTOs Actualizados

- **CreateTicketRequest**

  - Agregado campo `paymentMethod: String`
  - Validación de valor enum en servicio
  - Modificado: `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

- **TicketResponse**
  - Agregado campo `paymentMethod: String`
  - Retorna nombre del enum (`CASH`, `CREDIT_CARD`, etc.)
  - Modificado: `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`

#### Servicio Actualizado

- **TicketService.kt**
  - Validación de `paymentMethod` en `createTicket()`
  - Conversión de String a enum con manejo de errores
  - Actualizado mapeo en todos los métodos de respuesta
  - Métodos actualizados: `createTicket`, `getTicketByFolio`, `getAllTickets`, `getTicketsByUser`, `getTicketsByDateRange`
  - Modificado: `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketService.kt`

#### Resumen de Archivos Modificados/Creados

**Archivos Creados (1):**

1. `src/main/kotlin/api/multipartes/dev/enums/PaymentMethod.kt`

**Archivos Modificados (3):**

1. `src/main/kotlin/api/multipartes/dev/models/Ticket.kt`
2. `src/main/kotlin/api/multipartes/dev/dtos/allDTO.kt`
3. `src/main/kotlin/api/multipartes/dev/endPoints/tickets/TicketService.kt`

---

## RUTAS DE LA API

### Autenticación

#### POST /api/auth/login

**Descripción:** Autenticación de usuario

**Request:**

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**

```json
{
  "role": "ADMIN",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST /api/auth/register

**Descripción:** Registro de nuevo usuario

**Request:**

```json
{
  "name": "Juan Pérez",
  "username": "jperez",
  "password": "password123",
  "roleId": 1
}
```

**Response:**

```json
{
  "id": 1,
  "role": "USER"
}
```

---

### Tickets (Sistema de Ventas)

#### POST /api/tickets

**Descripción:** Crear un nuevo ticket de venta con sus items

**Request:**

```json
{
  "userId": 1,
  "paymentMethod": "CASH",
  "items": [
    {
      "partId": 5,
      "quantity": 2,
      "price": 450.0
    },
    {
      "partId": 8,
      "quantity": 1,
      "price": 1200.5
    }
  ]
}
```

**Response:**

```json
{
  "folio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "userId": 1,
  "userName": "Juan Pérez",
  "total": 2100.5,
  "paymentMethod": "CASH",
  "items": 2,
  "date": "2025-01-11T01:30:00",
  "sales": [
    {
      "id": 1,
      "ticketFolio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "partId": 5,
      "partName": "Parachoques delantero",
      "quantity": 2,
      "price": 450.0
    },
    {
      "id": 2,
      "ticketFolio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "partId": 8,
      "partName": "Alternador",
      "quantity": 1,
      "price": 1200.5
    }
  ]
}
```

**Métodos de Pago Válidos:** `CASH`, `CREDIT_CARD`, `DEBIT_CARD`, `CASH_ON_DELIVERY`, `OTHER`

#### GET /api/tickets

**Descripción:** Obtener todos los tickets

**Response:**

```json
[
  {
    "folio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": 1,
    "userName": "Juan Pérez",
    "total": 2100.50,
    "paymentMethod": "CASH",
    "items": 2,
    "date": "2025-01-11T01:30:00",
    "sales": [...]
  }
]
```

#### GET /api/tickets/{folio}

**Descripción:** Obtener ticket específico por folio

**Parámetros de ruta:**

- `folio`: UUID del ticket

**Response:** Igual que POST /api/tickets

#### GET /api/tickets/user/{userId}

**Descripción:** Obtener tickets de un vendedor específico

**Parámetros de ruta:**

- `userId`: ID del usuario/vendedor

**Response:** Array de tickets (mismo formato que GET /api/tickets)

#### GET /api/tickets/search/by-date

**Descripción:** Obtener tickets en un rango de fechas

**Parámetros de query:**

- `startDate`: Fecha inicial (formato ISO: `2025-01-01T00:00:00`)
- `endDate`: Fecha final (formato ISO: `2025-01-31T23:59:59`)

**Ejemplo:** `/api/tickets/search/by-date?startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59`

**Response:** Array de tickets (mismo formato que GET /api/tickets)

---

### Ventas (Sales)

#### GET /api/sales

**Descripción:** Obtener todas las ventas

**Response:**

```json
[
  {
    "id": 1,
    "ticketFolio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "partId": 5,
    "partName": "Parachoques delantero",
    "quantity": 2,
    "price": 450.0
  }
]
```

#### GET /api/sales/{id}

**Descripción:** Obtener venta específica por ID

**Response:**

```json
{
  "id": 1,
  "ticket": {
    "folio": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "user": {...},
    "total": 2100.50,
    "paymentMethod": "CASH",
    "items": 2,
    "date": "2025-01-11T01:30:00"
  },
  "part": {
    "id": 5,
    "name": "Parachoques delantero",
    ...
  },
  "quantity": 2,
  "price": 450.00
}
```

#### GET /api/sales/search/by-part/{partId}

**Descripción:** Obtener ventas de una pieza específica

**Parámetros de ruta:**

- `partId`: ID de la pieza

**Response:** Array de ventas (formato SaleResponse)

#### GET /api/sales/search/by-ticket/{ticketFolio}

**Descripción:** Obtener todas las ventas de un ticket

**Parámetros de ruta:**

- `ticketFolio`: UUID del ticket

**Response:** Array de ventas (formato SaleResponse)

---

### Piezas (Parts)

#### GET /api/parts

**Descripción:** Obtener todas las piezas del inventario

**Response:**

```json
[
  {
    "id": 1,
    "code": "PC-001",
    "name": "Parachoques delantero",
    "side": "FORWARD",
    "categoryId": 1,
    "categoryName": "COLLISION",
    "color": "Negro",
    "price": 450.0,
    "quantity": 5
  }
]
```

#### GET /api/parts/{id}

**Descripción:** Obtener pieza específica por ID

**Response:**

```json
{
  "id": 1,
  "code": "PC-001",
  "name": "Parachoques delantero",
  "side": "FORWARD",
  "categoryId": 1,
  "categoryName": "COLLISION",
  "color": "Negro",
  "price": 450.0,
  "quantity": 5
}
```

#### POST /api/parts

**Descripción:** Crear nueva pieza

**Request:**

```json
{
  "code": "PC-001",
  "name": "Parachoques delantero",
  "side": "FORWARD",
  "partCategory": {
    "id": 1
  },
  "color": "Negro",
  "price": 450.0,
  "quantity": 5
}
```

**Response:** Entidad Part completa

#### PUT /api/parts/{id}

**Descripción:** Actualizar pieza existente

**Request:** Igual que POST /api/parts

**Response:** Entidad Part actualizada

#### DELETE /api/parts/{id}

**Descripción:** Eliminar pieza

**Response:** 204 No Content

---

### Marcas de Vehículos (Brands)

#### GET /api/brands

**Descripción:** Obtener todas las marcas de vehículos

**Response:**

```json
[
  {
    "id": 1,
    "name": "TOYOTA"
  },
  {
    "id": 2,
    "name": "FORD"
  }
]
```

#### GET /api/brands/{id}

**Descripción:** Obtener marca específica por ID

**Parámetros de ruta:**

- `id`: ID de la marca

**Response:**

```json
{
  "id": 1,
  "name": "TOYOTA"
}
```

#### GET /api/brands/search

**Descripción:** Buscar marcas por nombre (búsqueda parcial, case-insensitive)

**Parámetros de query:**

- `name`: Texto a buscar en el nombre

**Ejemplo:** `/api/brands/search?name=toy`

**Response:** Array de marcas (mismo formato que GET /api/brands)

#### POST /api/brands

**Descripción:** Crear nueva marca

**Request:**

```json
{
  "name": "HONDA"
}
```

**Response:**

```json
{
  "id": 3,
  "name": "HONDA"
}
```

**Código de estado:** 201 Created

#### PUT /api/brands/{id}

**Descripción:** Actualizar marca existente

**Parámetros de ruta:**

- `id`: ID de la marca

**Request:**

```json
{
  "name": "TOYOTA MOTORS"
}
```

**Response:**

```json
{
  "id": 1,
  "name": "TOYOTA MOTORS"
}
```

#### DELETE /api/brands/{id}

**Descripción:** Eliminar marca (también elimina sus modelos en cascada)

**Parámetros de ruta:**

- `id`: ID de la marca

**Response:** 204 No Content

---

### Modelos de Vehículos (Models)

#### GET /api/models

**Descripción:** Obtener todos los modelos de vehículos

**Response:**

```json
[
  {
    "id": 1,
    "brandId": 1,
    "brandName": "TOYOTA",
    "serialNumber": "JTDKB20U487500102",
    "name": "Corolla",
    "year": 2020,
    "transmission": "AUTOMATIC",
    "engine": "4 cilindros",
    "vehicleClass": "Sedan"
  }
]
```

#### GET /api/models/{id}

**Descripción:** Obtener modelo específico por ID

**Parámetros de ruta:**

- `id`: ID del modelo

**Response:** Igual que item individual de GET /api/models

#### GET /api/models/search/by-brand/{brandId}

**Descripción:** Obtener todos los modelos de una marca específica

**Parámetros de ruta:**

- `brandId`: ID de la marca

**Response:** Array de modelos (mismo formato que GET /api/models)

#### GET /api/models/search/by-year/{year}

**Descripción:** Obtener modelos por año de fabricación

**Parámetros de ruta:**

- `year`: Año de fabricación (ejemplo: 2020)

**Response:** Array de modelos (mismo formato que GET /api/models)

#### GET /api/models/search/by-name

**Descripción:** Buscar modelos por nombre (búsqueda parcial, case-insensitive)

**Parámetros de query:**

- `name`: Texto a buscar en el nombre

**Ejemplo:** `/api/models/search/by-name?name=corolla`

**Response:** Array de modelos (mismo formato que GET /api/models)

#### GET /api/models/search/by-brand-and-year

**Descripción:** Buscar modelos por marca y año combinados

**Parámetros de query:**

- `brandId`: ID de la marca
- `year`: Año de fabricación

**Ejemplo:** `/api/models/search/by-brand-and-year?brandId=1&year=2020`

**Response:** Array de modelos (mismo formato que GET /api/models)

#### POST /api/models

**Descripción:** Crear nuevo modelo

**Request:**

```json
{
  "brandId": 1,
  "serialNumber": "JTDKB20U487500102",
  "name": "Corolla",
  "year": 2020,
  "transmission": "AUTOMATIC",
  "engine": "4 cilindros",
  "vehicleClass": "Sedan"
}
```

**Notas sobre campos:**

- `serialNumber`: Opcional
- `year`: Opcional
- `transmission`: Opcional. Valores válidos: `AUTOMATIC`, `STANDARD`
- `brandId`: Obligatorio (debe existir en la tabla brands)

**Response:**

```json
{
  "id": 1,
  "brandId": 1,
  "brandName": "TOYOTA",
  "serialNumber": "JTDKB20U487500102",
  "name": "Corolla",
  "year": 2020,
  "transmission": "AUTOMATIC",
  "engine": "4 cilindros",
  "vehicleClass": "Sedan"
}
```

**Código de estado:** 201 Created

#### PUT /api/models/{id}

**Descripción:** Actualizar modelo existente

**Parámetros de ruta:**

- `id`: ID del modelo

**Request:** Igual que POST /api/models

**Response:** Modelo actualizado (mismo formato que respuesta de POST)

#### DELETE /api/models/{id}

**Descripción:** Eliminar modelo

**Parámetros de ruta:**

- `id`: ID del modelo

**Response:** 204 No Content

---

### Clientes (Customers)

#### GET /api/customers

**Descripción:** Obtener todos los clientes

**Response:**

```json
[
  {
    "id": 1,
    "name": "Juan Pérez García",
    "phone": "5551234567",
    "rfc": "PEGJ850101XXX"
  },
  {
    "id": 2,
    "name": "María López",
    "phone": "5559876543",
    "rfc": null
  }
]
```

#### GET /api/customers/{id}

**Descripción:** Obtener cliente específico por ID

**Parámetros de ruta:**

- `id`: ID del cliente

**Response:**

```json
{
  "id": 1,
  "name": "Juan Pérez García",
  "phone": "5551234567",
  "rfc": "PEGJ850101XXX"
}
```

#### GET /api/customers/search/by-phone/{phone}

**Descripción:** Buscar cliente por número de teléfono

**Parámetros de ruta:**

- `phone`: Número de teléfono del cliente

**Ejemplo:** `/api/customers/search/by-phone/5551234567`

**Response:**

```json
{
  "id": 1,
  "name": "Juan Pérez García",
  "phone": "5551234567",
  "rfc": "PEGJ850101XXX"
}
```

#### GET /api/customers/search/by-rfc/{rfc}

**Descripción:** Buscar cliente por RFC

**Parámetros de ruta:**

- `rfc`: RFC del cliente

**Ejemplo:** `/api/customers/search/by-rfc/PEGJ850101XXX`

**Response:**

```json
{
  "id": 1,
  "name": "Juan Pérez García",
  "phone": "5551234567",
  "rfc": "PEGJ850101XXX"
}
```

#### GET /api/customers/search/by-name

**Descripción:** Buscar clientes por nombre (búsqueda parcial, case-insensitive)

**Parámetros de query:**

- `name`: Texto a buscar en el nombre

**Ejemplo:** `/api/customers/search/by-name?name=juan`

**Response:** Array de clientes (mismo formato que GET /api/customers)

#### POST /api/customers

**Descripción:** Crear nuevo cliente

**Request:**

```json
{
  "name": "Carlos Ramírez",
  "phone": "5552223333",
  "rfc": "RAC900505XXX"
}
```

**Notas sobre campos:**

- `name`: Obligatorio (máx. 100 caracteres)
- `phone`: Obligatorio (máx. 20 caracteres, debe ser único)
- `rfc`: Opcional (debe existir en tabla `customer_invoice_data`)

**Response:**

```json
{
  "id": 3,
  "name": "Carlos Ramírez",
  "phone": "5552223333",
  "rfc": "RAC900505XXX"
}
```

**Código de estado:** 201 Created

#### PUT /api/customers/{id}

**Descripción:** Actualizar cliente existente

**Parámetros de ruta:**

- `id`: ID del cliente

**Request:**

```json
{
  "name": "Carlos Ramírez Sánchez",
  "phone": "5552223333",
  "rfc": "RAC900505XXX"
}
```

**Response:**

```json
{
  "id": 3,
  "name": "Carlos Ramírez Sánchez",
  "phone": "5552223333",
  "rfc": "RAC900505XXX"
}
```

#### DELETE /api/customers/{id}

**Descripción:** Eliminar cliente (también elimina sus problemas reportados en cascada)

**Parámetros de ruta:**

- `id`: ID del cliente

**Response:** 204 No Content

---

### Problemas de Clientes (Customer Issues)

#### GET /api/customer-issues

**Descripción:** Obtener todos los problemas reportados

**Response:**

```json
[
  {
    "id": 1,
    "problem": "Pieza defectuosa al recibirla",
    "status": "PENDING",
    "createdAt": "2025-01-11T10:30:00",
    "customerId": 1,
    "customerName": "Juan Pérez García"
  },
  {
    "id": 2,
    "problem": "No encontraron la pieza solicitada",
    "status": "NOT_FOUND",
    "createdAt": "2025-01-11T11:00:00",
    "customerId": 2,
    "customerName": "María López"
  }
]
```

#### GET /api/customer-issues/{id}

**Descripción:** Obtener problema específico por ID

**Parámetros de ruta:**

- `id`: ID del problema

**Response:** Igual que item individual de GET /api/customer-issues

#### GET /api/customer-issues/search/by-status/{status}

**Descripción:** Obtener problemas por estado

**Parámetros de ruta:**

- `status`: Estado del problema (`ATTENDED`, `REJECTED`, `NOT_FOUND`, `PENDING`)

**Ejemplo:** `/api/customer-issues/search/by-status/PENDING`

**Response:** Array de problemas (mismo formato que GET /api/customer-issues)

#### GET /api/customer-issues/search/by-customer/{customerId}

**Descripción:** Obtener todos los problemas de un cliente específico

**Parámetros de ruta:**

- `customerId`: ID del cliente

**Response:** Array de problemas (mismo formato que GET /api/customer-issues)

#### GET /api/customer-issues/search/by-status-and-customer

**Descripción:** Buscar problemas por estado y cliente combinados

**Parámetros de query:**

- `status`: Estado del problema
- `customerId`: ID del cliente

**Ejemplo:** `/api/customer-issues/search/by-status-and-customer?status=PENDING&customerId=1`

**Response:** Array de problemas (mismo formato que GET /api/customer-issues)

#### POST /api/customer-issues

**Descripción:** Crear nuevo problema reportado por cliente

**Request:**

```json
{
  "problem": "La pieza no coincide con el modelo del vehículo",
  "customerId": 1
}
```

**Notas sobre campos:**

- `problem`: Obligatorio (máx. 255 caracteres)
- `customerId`: Opcional (si se proporciona, debe existir en tabla `customer`)
- `status`: Se establece automáticamente como `PENDING`

**Response:**

```json
{
  "id": 3,
  "problem": "La pieza no coincide con el modelo del vehículo",
  "status": "PENDING",
  "createdAt": "2025-01-11T12:00:00",
  "customerId": 1,
  "customerName": "Juan Pérez García"
}
```

**Código de estado:** 201 Created

#### PUT /api/customer-issues/{id}

**Descripción:** Actualizar problema existente (cambiar estado o descripción)

**Parámetros de ruta:**

- `id`: ID del problema

**Request:**

```json
{
  "problem": "La pieza no coincide con el modelo del vehículo - Resuelto con cambio",
  "status": "ATTENDED",
  "customerId": 1
}
```

**Valores de status válidos:** `ATTENDED`, `REJECTED`, `NOT_FOUND`, `PENDING`

**Response:**

```json
{
  "id": 3,
  "problem": "La pieza no coincide con el modelo del vehículo - Resuelto con cambio",
  "status": "ATTENDED",
  "createdAt": "2025-01-11T12:00:00",
  "customerId": 1,
  "customerName": "Juan Pérez García"
}
```

#### DELETE /api/customer-issues/{id}

**Descripción:** Eliminar problema

**Parámetros de ruta:**

- `id`: ID del problema

**Response:** 204 No Content

---

### Notas Importantes

- **Autenticación:** Todos los endpoints (excepto /api/auth/\*) requieren JWT token en header `Authorization: Bearer {token}`
- **Formato de fechas:** ISO 8601 (`yyyy-MM-ddTHH:mm:ss`)
- **Gestión de ventas:** Las ventas solo se crean a través de tickets, no se pueden crear/modificar/eliminar individualmente
- **IDs autogenerados:** Los tickets usan UUID, las demás entidades usan IDs autoincrementales
