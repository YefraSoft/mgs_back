# Requisitos Canónicos - ReqsMG v2

Versión: 2.0 (sintetizado)
Fecha: 2025-11-26
Autor: Ingeniería de Requisitos / Equipo técnico

Resumen

- Este documento unifica el `database/requirements_doc.md` (v1.0) y las anotaciones de `docs/reqv2.md` (rev 1.5). Contiene los requisitos canónicos que deben guiar el desarrollo y la verificación.

Notas sobre alcance

- Se priorizan las funcionalidades de ventas/tickets, gestión de piezas, garantías, clientes, facturación e imágenes.
- Los puntos operativos (apartados, entregas, gastos/finanzas avanzadas, dashboards) quedan como módulos de prioridad media/alta según se indica por requisito.

Requisitos (síntesis por secciones)

## Gestión de Vehículos y Piezas

- REQ-001: Registrar vehículos donantes (marca, modelo, año, VIN, motor, transmisión, clase). (Alta)
- REQ-002: Datos por vehículo (ver REQ-001). (Alta)
- REQ-003: Asociar piezas con vehículo de origen (opcional pero recomendado). (Media)
- REQ-004: Registrar costo de adquisición del vehículo (para amortización). (Alta)
- REQ-005: Vehículos con múltiples fotografías. (Media)
- REQ-006: Piezas con atributos (código, nombre, lado, categoría, precio, cantidad). (Alta)
- REQ-007: (REVISIÓN) - retirar requisito de relación obligatoria marca/modelo (ver nota en rev1.5).
- REQ-008: Asociar pieza a vehículo donante (opcional). (Media)
- REQ-009: Piezas pueden tener múltiples fotografías. (Media)

## Ventas y Facturación

- REQ-011..015: Ventas de piezas (inventariadas/no inventariadas), generación de ticket con UUID, datos (vendedor, total, método de pago, items, fecha). (Alta)
- REQ-016..018: Descuentos: aplicar descuentos en venta; almacenar precios originales y con descuento (auditoría). (Alta)
- REQ-019..022: Apartados (reservas) con anticipo, estados y generación automática de venta al completar. (Alta)
- REQ-023..025: Entregas a domicilio: registrar dirección y estado de entrega (PENDING/EN_ROUTE/DELIVERED/FAILED). (Alta)
- REQ-026..028: Devoluciones y vínculo con garantías. (Alta)

## Garantías

- REQ-029..032: Gestión de garantías por categoría; registro de duración, expiración y estados (ACTIVE, EXPIRED, RECLAIMED, CANCELED). (Alta)
- REQ-033..036: Reclamaciones de garantía con evidencia fotográfica, resolución y vínculos a ventas. (Alta)

## Clientes

- REQ-037..039: Registro de clientes para apartados/quejas/entregas; RFC y datos fiscales. (Alta)
- REQ-041..044: Gestión de quejas/problemas y resolución. (Media)

## Gastos y Reportes

- REQ-045..051: Gastos operativos y análisis de rentabilidad (mensual). (Alta)
- REQ-052..056: Reportes de ventas y financieros, incluir descuentos. (Alta)

## Usuarios y Permisos

- REQ-057..062: Roles (USER, SELLER, ACCOUNTANT, INVENTORY_MANAGER, ADMIN) y permisos por rol. (Alta)
- REQ-063..064: Auditoría y logs (login, app logs). (Media)

## Facturación Fiscal

- REQ-065..068: Datos fiscales y relación de facturas con tickets. (Alta)

## Multimedia e Imágenes

- REQ-069..073: Fotografías para vehículos, piezas, tickets y reclamaciones (evidencia). (Media-Alta)

Implementación: estado inicial (evidencia rápida)

- Implementado / presente (evidencia de archivos en `src/`):
  - Tickets / ventas: `src/main/kotlin/.../models/Ticket.kt`, `endPoints/tickets/TicketService.kt`, `repositories/TicketRepository.kt`.
  - Sales model: `src/main/kotlin/.../models/Sale.kt` (incluye campos de garantía como `hasWarranty`, `warrantyStatus`, `warrantyExpirationDate`).
  - Garantías: `src/main/kotlin/.../models/WarrantyClaim.kt`, `enums/WarrantyStatus.kt`.
  - Customer issues: `src/main/kotlin/.../models/CustomerIssue.kt`, `endPoints/customers/CustomerIssueController.kt`.
  - Facturación fiscal: `src/main/kotlin/.../models/Invoice.kt`, `CustomerInvoiceData.kt`, `repositories/CustomerInvoiceDataRepository.kt`.
  - Imágenes para piezas/modelos: `ModelImage.kt`, `PartImage.kt`, `TicketImage.kt`.
  - Payment methods enum: `src/main/kotlin/.../enums/PaymentMethod.kt` and used in `Ticket.kt`.

- Pendiente / Falta: (ver `docs/TareasPendientes.md` para detalle y prioridades)
  - REQ-004: costo de adquisición vehículos (campo y lógica de amortización).
  - REQ-005: fotografías de vehículos donantes (endpoint/tabla específica si es requerida).
  - REQ-016..018: soporte explícito para descuentos (registro de precio original y precio con descuento).
  - REQ-019..022: Apartados / reservations (entidades, endpoints y estados en inglés).
  - REQ-023..025: Entregas a domicilio y estados de entrega en ticket o entidad relacionada.
  - REQ-026..028: Devoluciones con vínculo explícito a reclamaciones y ajuste de tickets/ventas.

Siguientes pasos recomendados

- Implementar las entidades y endpoints faltantes priorizando: descuentos (REQ-016..018), apartados (REQ-019..022) y registro de costo vehículo (REQ-004).
- Añadir pruebas unitarias/integ. para los nuevos campos (tickets, ventas, apartados, devoluciones).
- Actualizar `docs/TareasPendientes.md` con tareas por archivo/PR y criterios de aceptación.

---

Archivo generado automáticamente a partir de `database/requirements_doc.md` y `docs/reqv2.md`.
