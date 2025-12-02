# Tareas Pendientes (Extracto inicial)

Fecha: 2025-11-26

Este documento lista las tareas detectadas como "Falta" o "Verificar" a partir de `docs/reqv2.md` (rev 1.5) y del análisis inicial del código fuente.

## Mapeo - Iteración 1 (resumen)

Fecha: 2025-11-27

Objetivo: marcar cada bloque de requisitos como `Implementado` / `Parcial` / `Falta` y aportar evidencia de código (archivos relevantes).

1) Gestión de Vehículos y Piezas (REQ-001..010)

- Estado: Parcial
- Evidencia:
  - `VehicleBrands.kt` (marca): `src/main/kotlin/api/multipartes/dev/models/VehicleBrands.kt`
  - `Model.kt` (modelo de vehículo): `src/main/kotlin/api/multipartes/dev/models/Model.kt` (incluye `vehicleClass`, `year`, `serialNumber` en DTOs y servicio)
  - `Part.kt`: `src/main/kotlin/api/multipartes/dev/models/Part.kt` (código, nombre, lado, categoría, precio, cantidad, relación opcional `model`)
- Gap (Falta): REQ-004 (campo `acquisitionCost` para vehículo); REQ-005 (vehículo-fotos: no existe entidad clara `VehicleImage`, revisar `ModelImage.kt`/`PartImage.kt` para posible reutilización).

## Ventas y Facturación (REQ-011..018)

- Estado: Parcial
- Evidencia:
  - `Ticket.kt`: `src/main/kotlin/api/multipartes/dev/models/Ticket.kt` (folio CHAR(36), paymentMethod enum, total, items, date)
  - `Sale.kt`: `src/main/kotlin/api/multipartes/dev/models/Sale.kt` (vínculo a ticket, part opcional, partName, price, quantity)
  - `Invoice.kt` y `CustomerInvoiceData.kt`: facturación presente
- Gap (Falta): REQ-016..018 (descuentos): no hay campos `discount` ni `originalPrice` en `Ticket` o `Sale`.

## REQs detectados en `Rev1_5.md` y faltantes en `bd/bdv2.sql`

- REQ-016..018: Descuentos — Falta support en BD: agregar a `tickets` campos `discount_amount DECIMAL(10,2) NULL` y `original_total DECIMAL(10,2) NULL`; opcionalmente `sales.original_price` y `sales.price_with_discount`.
- REQ-026..028: Devoluciones/Anulaciones — Falta entidad/registro que permita ajustar ventas y tickets (por ejemplo `returns` o flag `is_return` en `sales`) y motivo de devolución. Recomendación: diseñar tabla `returns` y flujo de ajuste.
- REQ-031: Registrar vencimiento de garantía en calendario — BD posee `warranty.expiration_date` pero no tabla de calendario; si se requiere trazabilidad por calendario, agregar `warranty_events` o integrar con sistema de tareas.
- REQ-035..036: Flujo de cambio por reclamación — Falta mecanismo en BD para enlazar nueva venta generada por cambio con la reclamación (ej.: `warranty_claim.replacement_sale_id`).
- REQ-044: Registro de resolución de quejas — `customer_issues` carece de campos `resolution` y `resolved_at`. Recomendación: agregar `resolution VARCHAR(255)` y `resolved_at DATETIME`.
- REQ-049: Indicadores de rentabilidad — Aunque existe tabla `expenses`, faltan vistas/reportes y/o materialización para cálculo mensual; añadir procedures o queries de ejemplo y endpoints.

Cada ítem anterior se agregó a la lista de tareas pendientes con prioridad sugerida (Alta para descuentos, apartados ya implementados en BD, Devoluciones alta, Fotografías vehículo media, Resoluciones media, Rentabilidad alta).

## Apartados / Reservations (REQ-019..022)

- Estado: Falta
- Evidencia: No se encontró entidad `Reservation`/`Apartado` ni endpoints relacionados en `src/`.

## Entregas a Domicilio (REQ-023..025)

- Estado: Falta / Parcial
- Evidencia: `Ticket.kt` contiene `paymentMethod` pero no campos de `isDelivery`, `deliveryAddress` o `deliveryStatus`.
- Recomendación: Añadir `Delivery` embebido o entidad relacionada si se requiere trazabilidad.

## Devoluciones (REQ-026..028)

- Estado: Parcial
- Evidencia: Existe `WarrantyClaim.kt` y `Sale` que permiten registrar reclamaciones; pero no hay flujo explícito para anular ventas y ajustar tickets automáticamente.

## Garantías (REQ-029..036)

- Estado: Implementado / Parcial
- Evidencia: `Sale.kt` (campos `hasWarranty`, `warrantyStatus`, `warrantyExpirationDate`) y `WarrantyClaim.kt` (evidencia fotográfica y `claimType`).

## Clientes y Quejas (REQ-037..044)

- Estado: Implementado
- Evidencia: `Customer.kt`, `CustomerIssue.kt`, repositorios y controladores existentes (`endPoints/customers`).

## Gastos y Reportes (REQ-045..056)

- Estado: Falta
- Evidencia: No se detectó entidad `Expense` ni servicios de reportes financieros completos. Hay reportes básicos en documentos pero no endpoints implementados.

## Usuarios y Permisos (REQ-057..064)

- Estado: Implementado (parcial)
- Evidencia: `RoleType.kt`, `User` model y security config, `login_logs` y `app_logs` se mencionan en `AGENTS.md` y hay `LoginLog`/`AppLog` models.

## Facturación Fiscal (REQ-065..068)

- Estado: Implementado
- Evidencia: `Invoice.kt`, `CustomerInvoiceData.kt`, y repositorio `CustomerInvoiceDataRepository.kt`.

## Multimedia e Imágenes (REQ-069..073)

- Estado: Parcial
- Evidencia: `ModelImage.kt`, `PartImage.kt`, `TicketImage.kt` existen; `VehicleImage` no existe explícitamente.

## Búsqueda y Consultas (REQ-074..079)

- Estado: Implementado (parcial)
- Evidencia: Repositorios con métodos `findBy...` para piezas, modelos y marcas (`BrandRepository`, `ModelRepository`, `PartsRepo` etc.).

---

Próximo paso: en la Iteración 2 ejecutar mapping más fino (por requisito individual) y proponer cambios de código mínimos (migrations, DTOs, endpoints) para cerrar los gaps priorizados (descuentos, apartados, delivery, acquisitionCost).

## Iteración 2 - Plan de trabajo (acciones concretas)

Objetivo: generar cambios mínimos y propuestas para cerrar los gaps de mayor prioridad. Para cada ítem se propone un paquete de trabajo con archivos a crear/modificar y criterios de aceptación.

1) Descuentos (REQ-016..018) - Prioridad: Alta

- Cambios propuestos:
  - Model: agregar a `Ticket` los campos `discountAmount: BigDecimal?` y `originalTotal: BigDecimal?` (o en `Sale` agregar `originalPrice` y `priceWithDiscount`).
  - DTOs: `CreateTicketRequest` y `TicketResponse` actualizados para aceptar/devolver descuentos.
  - Servicio: aplicar descuento durante creación de ticket y persistir `original` y `discount`.
- Criterios de aceptación:
  - POST `/api/tickets` permite enviar `discountAmount` y la respuesta incluye `originalTotal` y `total` con descuento aplicado.
  - Tests de integración que verifican cálculo y persistencia.

## Gaps detectados (Rev1_5.md vs `bd/bdv2.sql`) - Resumen rápido

Tras comparar `docs/Rev1_5.md` con `bd/bdv2.sql` se detectaron los siguientes requisitos que faltan o están solo parcialmente cubiertos por la definición de BD. Para cada uno incluyo una tarea sugerida.

- **REQ-016..018 (Descuentos)**: Faltan columnas para registrar descuento a nivel ticket (`discountAmount`, `originalTotal`) y/o a nivel item. Tarea: agregar columnas en `tickets` y/o `sales`, DTOs y lógica de cálculo.
- **REQ-026..028 (Devoluciones / Anulación de ventas)**: No existe un flujo/tabla explícita para devoluciones (anular venta y ajustar ticket). Tarea: diseñar tabla `returns` o flag `is_return` en `sales` + registro de motivo y ajuste de ticket total.
- **REQ-031 (Garantía - duración / registro en calendario)**: `warranty` tiene `expiration_date` pero no `duration_months` ni `terms` ni mecanismo para registrar evento en calendario. Tarea: añadir `duration_months`, `terms TEXT` y campo `calendar_entry_id` o similar, y endpoint para calcular/registrar expiración.
- **REQ-034 (Resolución de reclamaciones)**: `warranty_claim` no guarda la resolución aplicada (cambio/reembolso) ni fecha de resolución. Tarea: agregar `resolution ENUM('EXCHANGE','REFUND','NONE')` y `resolved_at DATETIME`.
- **REQ-035 / REQ-036 (Vincular nueva venta y modificar venta original al cambiar pieza por garantía)**: No hay campo/link explícito para vincular nueva venta creada por cambio de pieza ni para marcar la venta original como ajustada. Tarea: añadir `replacement_sale_id` en `warranty_claim` o `warranty`, y `adjusted_amount`/`status` en `sales`.
- **REQ-038 (Cliente - email/dirección en `customer`)**: La tabla `customer` no incluye `email` ni `address` (solo `phone` y `rfc` en `customer_invoice_data`). Tarea: agregar `email` y `address` opcionales en `customer` o documentar uso de `customer_invoice_data` para facturación.
- **REQ-042 (Relacionar queja/issue con ticket/venta)**: `customer_issues` no tiene referencia a `tickets(folio)` o `sales(id)`. Tarea: agregar `ticket_folio CHAR(36) NULL` y/o `sale_id INT NULL` con FK.
- **REQ-044 (Resolución de quejas: registro y fecha)**: `customer_issues` carece de campo `resolution` y `resolved_at`. Tarea: añadir `resolution VARCHAR(255)` y `resolved_at DATETIME`.
- **REQ-005 (Vehículos - múltiples fotografías)**: Aunque existe `model_images`, no hay `vehicle_images` explícito para `purchase_vehicles`. Tarea: crear tabla `purchase_vehicle_images` o reutilizar `model_images` con aclaración de relación.

Cada ítem anterior debe transformarse en una tarea (issue + migration SQL + cambios en modelos/DTOs/servicios) si se decide implementarlo. Puedo generar las migrations y PRs para cualquiera de estos items; dime cuál priorizas.

## Apartados / Reservations (REQ-019..022) - Prioridad: Alta

- Cambios propuestos:
  - Nueva entidad `Reservation` (`reservations`): `id`, `customer_id`, `items` (json/text o relación), `deposit_amount`, `balance`, `expiry_date`, `status` (enum: ACTIVE, COMPLETED, CANCELLED, EXPIRED).
  - Endpoints: CRUD `GET/POST/PUT/DELETE /api/reservations` y `POST /api/reservations/{id}/complete` que genera automáticamente el ticket/venta.
- Criterios de aceptación:
  - Reserva creada y listada; completar reserva genera un ticket con items y ajusta stock.

## Costo de adquisición vehículos (REQ-004) - Prioridad: Alta

- Cambios propuestos:
  - Añadir campo `acquisitionCost: BigDecimal?` a `Model` o crear entidad `Vehicle` si se decide separar.
  - Reporte/endpoint que calcule amortización simple (ventas acumuladas vs costo) para un vehículo.
- Criterios de aceptación:
  - Campo persistido y visible en `GET /api/models/{id}` o `GET /api/vehicles/{id}`.

## Entregas a domicilio (REQ-023..025) - Prioridad: Media-Alta

- Cambios propuestos:
  - Añadir a `Ticket` campos `isDelivery: Boolean`, `deliveryAddress: String?`, `deliveryStatus: String?` (enum PENDING/EN_ROUTE/DELIVERED/FAILED).
  - Endpoints para actualizar `deliveryStatus`.
- Criterios de aceptación:
  - Ticket marcado como entrega y estado actualizable vía API.

Proceso recomendado para cada cambio

- Crear branch por tarea, diseñar migration SQL (ALTER TABLE ADD COLUMN...), implementar model+dto+service+controller, añadir tests unitarios e integración, y preparar PR.

---

Si deseas, en la Iteración 2 puedo generar los cambios de código mínimos para **uno** de los ítems anteriores (por ejemplo: descuentos o apartados). Indica cuál priorizas y procedo a crear los archivos iniciales (model/DTO/migration sugerida + pruebas básicas) y un `CHANGELOG` en `AGENTS.md` con el resumen de cambios.

## PENDIENTES

**REQ-004**: Debe registrarse el costo de adquisición del vehículo  
**Prioridad:** Alta  
**Nota:** Se debe registrar para generar una grafica de cuanto falta para amortizar el vehiculo.

**REQ-005**: Los vehículos deben poder tener múltiples fotografías
**Prioridad:** Media -> Implementado a nivel BD, Falta nivel api y cliente.
