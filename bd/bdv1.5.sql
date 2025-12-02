USE mpgv1;

-- ===========================================================
-- TABLA: donor_vehicles
-- Vehículos completos comprados para deshuese
-- REQ-001, REQ-002, REQ-003, REQ-004, REQ-005
-- ===========================================================
CREATE TABLE purchase_vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    purchase_date DATE COMMENT 'Fecha de adquisición',
    purchase_cost DECIMAL(10, 2) COMMENT 'Costo de compra del vehículo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES models (id) ON DELETE RESTRICT,
    INDEX idx_brand_model (brand_id, model_id),
    INDEX idx_purchase_date (purchase_date)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE ticked_delivey (
    id INT PRIMARY KEY,
    delivery_address VARCHAR(255),
    delivery_status ENUM(
        'PENDING',
        'IN_TRANSIT',
        'DELIVERED',
        'FAILED'
    ) COMMENT 'Estado de entrega',
    delivered_at DATETIME COMMENT 'Fecha/hora de entrega',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ticked_folio CHAR(36) NOT NULL,
    customer_id INT NOT NULL,
    Foreign Key (ticked_folio) REFERENCES tickeds (folio),
    Foreign Key (customer_id) REFERENCES customers (id)
);

-- ===========================================================
-- TABLA: reservations (Apartados)
-- Sistema de apartado de piezas con anticipo
-- REQ-019, REQ-020, REQ-021, REQ-022
-- ===========================================================
CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    part_id INT COMMENT 'Pieza inventariada',
    part_name VARCHAR(255) COMMENT 'Para piezas no inventariadas',
    deposit DECIMAL(10, 2) NOT NULL COMMENT 'Anticipo recibido',
    total_price DECIMAL(10, 2) NOT NULL COMMENT 'Precio total',
    balance DECIMAL(10, 2) NOT NULL COMMENT 'Saldo pendiente',
    expiration_date DATE NOT NULL COMMENT 'Fecha límite',
    status ENUM(
        'ACTIVE',
        'COMPLETED',
        'CANCELLED',
        'EXPIRED'
    ) DEFAULT 'ACTIVE',
    completed_ticket_folio CHAR(36) COMMENT 'Ticket generado al completar',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at DATETIME COMMENT 'Fecha de finalización',
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT,
    FOREIGN KEY (part_id) REFERENCES parts (id) ON DELETE SET NULL,
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
-- TABLA: expenses
-- Gastos operativos del negocio
-- REQ-045, REQ-046, REQ-047, REQ-048
-- ===========================================================
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category ENUM(
        'VEHICLE_PURCHASE',
        'SERVICES',
        'RENT',
        'SALARIES',
        'MAINTENANCE',
        'UTILITIES',
        'OTHER'
    ) NOT NULL,
    pay_before DATE NOT NULL,
    receipt_url VARCHAR(500) COMMENT 'URL del comprobante',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_at DATE,
    FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_expense_date (pay_before),
    INDEX idx_category (category),
    INDEX idx_created_by (created_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ===========================================================
-- VISTAS ÚTILES PARA REPORTES
-- ===========================================================

-- Vista: Ventas por período (REQ-052, REQ-054)
CREATE VIEW v_sales_report AS
SELECT
    DATE(t.date) as sale_date,
    COUNT(DISTINCT t.folio) as total_tickets,
    SUM(t.total) as total_sales,
    SUM(t.discount_amount) as total_discounts,
    t.payment_method,
    u.name as seller_name
FROM tickets t
    JOIN users u ON t.user_id = u.id
WHERE
    t.is_cancelled = FALSE
GROUP BY
    DATE(t.date),
    t.payment_method,
    u.name;

-- Vista: Piezas más vendidas (REQ-053)
CREATE VIEW v_top_selling_parts AS
SELECT
    COALESCE(p.name, s.part_name) as part_name,
    COALESCE(b.name, s.part_brand) as brand_name,
    COALESCE(m.name, s.part_model) as model_name,
    COUNT(*) as times_sold,
    SUM(s.quantity) as total_quantity,
    SUM(s.price * s.quantity) as total_revenue
FROM
    sales s
    LEFT JOIN parts p ON s.part_id = p.id
    LEFT JOIN brands b ON p.brand_id = b.id
    LEFT JOIN models m ON p.model_id = m.id
WHERE
    s.is_cancelled = FALSE
GROUP BY
    part_name,
    brand_name,
    model_name
ORDER BY total_quantity DESC;

-- Vista: Análisis de rentabilidad mensual (REQ-050, REQ-055, REQ-056)
CREATE VIEW v_monthly_profitability AS
SELECT
    DATE_FORMAT(sale_date, '%Y-%m') as month,
    total_revenue,
    total_expenses,
    (
        total_revenue - total_expenses
    ) as net_profit,
    CASE
        WHEN total_revenue > total_expenses THEN 'PROFITABLE'
        ELSE 'LOSS'
    END as status
FROM (
        SELECT DATE(t.date) as sale_date, SUM(t.total) as total_revenue
        FROM tickets t
        WHERE
            t.is_cancelled = FALSE
        GROUP BY
            DATE_FORMAT(t.date, '%Y-%m')
    ) revenue
    LEFT JOIN (
        SELECT
            DATE_FORMAT(expense_date, '%Y-%m') as expense_month, SUM(amount) as total_expenses
        FROM expenses
        GROUP BY
            DATE_FORMAT(expense_date, '%Y-%m')
    ) expenses ON DATE_FORMAT(revenue.sale_date, '%Y-%m') = expenses.expense_month;

-- Vista: Inventario con alertas de stock bajo
CREATE VIEW v_inventory_status AS
SELECT
    p.id,
    p.code,
    p.name,
    p.quantity,
    p.min_stock,
    p.price,
    b.name as brand_name,
    m.name as model_name,
    pc.name as category_name,
    CASE
        WHEN p.quantity = 0 THEN 'OUT_OF_STOCK'
        WHEN p.quantity <= p.min_stock THEN 'LOW_STOCK'
        ELSE 'AVAILABLE'
    END as stock_status
FROM
    parts p
    LEFT JOIN brands b ON p.brand_id = b.id
    LEFT JOIN models m ON p.model_id = m.id
    JOIN part_categories pc ON p.part_category_id = pc.id;

-- Vista: Garantías activas y próximas a vencer
CREATE VIEW v_active_warranties AS
SELECT
    s.id as sale_id,
    t.folio as ticket_folio,
    t.date as sale_date,
    c.name as customer_name,
    c.phone as customer_phone,
    COALESCE(p.name, s.part_name) as part_name,
    s.warranty_status,
    t.warranty_expiration_date,
    DATEDIFF(
        t.warranty_expiration_date,
        CURDATE()
    ) as days_remaining
FROM
    sales s
    JOIN tickets t ON s.ticket_folio = t.folio
    LEFT JOIN customer c ON t.customer_id = c.id
    LEFT JOIN parts p ON s.part_id = p.id
WHERE
    s.has_warranty = TRUE
    AND s.warranty_status = 'ACTIVE'
    AND t.warranty_expiration_date >= CURDATE()
ORDER BY t.warranty_expiration_date;

-- ===========================================================
-- PROCEDIMIENTOS ALMACENADOS ÚTILES
-- ===========================================================

-- Procedimiento: Completar un apartado y generar venta
DELIMITER /
/

CREATE PROCEDURE sp_complete_reservation(
    IN p_reservation_id INT,
    IN p_user_id INT,
    IN p_payment_method VARCHAR(20),
    IN p_final_payment DECIMAL(10, 2)
)
BEGIN
    DECLARE v_ticket_folio CHAR(36);
    DECLARE v_customer_id INT;
    DECLARE v_part_id INT;
    DECLARE v_part_name VARCHAR(255);
    DECLARE v_total_price DECIMAL(10, 2);
    DECLARE v_balance DECIMAL(10, 2);
    
    -- Obtener datos del apartado
    SELECT customer_id, part_id, part_name, total_price, balance
    INTO v_customer_id, v_part_id, v_part_name, v_total_price, v_balance
    FROM reservations
    WHERE id = p_reservation_id AND status = 'ACTIVE';
    
    -- Verificar que el pago final cubra el saldo
    IF p_final_payment >= v_balance THEN
        -- Crear ticket
        INSERT INTO tickets (user_id, customer_id, subtotal, total, payment_method, items)
        VALUES (p_user_id, v_customer_id, v_total_price, v_total_price, p_payment_method, 1);
        
        SET v_ticket_folio = LAST_INSERT_ID();
        
        -- Crear venta
        INSERT INTO sales (ticket_folio, part_id, part_name, quantity, original_price, price)
        VALUES (v_ticket_folio, v_part_id, v_part_name, 1, v_total_price, v_total_price);
        
        -- Actualizar apartado
        UPDATE reservations
        SET status = 'COMPLETED',
            completed_ticket_folio = v_ticket_folio,
            completed_at = NOW()
        WHERE id = p_reservation_id;
        
        -- Reducir inventario si es pieza inventariada
        IF v_part_id IS NOT NULL THEN
            UPDATE parts SET quantity = quantity - 1 WHERE id = v_part_id;
        END IF;
        
        SELECT 'SUCCESS' as result, v_ticket_folio as ticket_folio;
    ELSE
        SELECT 'INSUFFICIENT_PAYMENT' as result;
    END IF;
END
/
/

DELIMITER;

-- ===========================================================
-- TRIGGERS
-- ===========================================================

-- Trigger: Actualizar inventario al realizar una venta
DELIMITER /
/

CREATE TRIGGER tr_after_sale_insert
AFTER INSERT ON sales
FOR EACH ROW
BEGIN
    IF NEW.part_id IS NOT NULL AND NEW.is_cancelled = FALSE THEN
        UPDATE parts 
        SET quantity = quantity - NEW.quantity 
        WHERE id = NEW.part_id;
    END IF;
END
/
/

DELIMITER;

-- Trigger: Restaurar inventario al cancelar una venta
DELIMITER /
/

CREATE TRIGGER tr_after_sale_cancel
AFTER UPDATE ON sales
FOR EACH ROW
BEGIN
    IF NEW.is_cancelled = TRUE AND OLD.is_cancelled = FALSE AND NEW.part_id IS NOT NULL THEN
        UPDATE parts 
        SET quantity = quantity + NEW.quantity 
        WHERE id = NEW.part_id;
    END IF;
END
/
/

DELIMITER;