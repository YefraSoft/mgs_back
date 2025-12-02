# DOCUMENTO DE REQUISITOS FUNCIONALES

## POS - Deshuace García

**REVICION:** 1.5  
**Fecha:** Noviembre 2025  
**Autor:** Ingeniería de Requisitos

---

## 1. GESTIÓN DE VEHÍCULOS Y PIEZAS

### 1.1 Vehículos Comprados (Modificado REQ-004)

**REQ-001**: El sistema debe permitir registrar vehículos completos comprados para deshuese  
**Prioridad:** Alta  
**Descripción:** Se compran vehículos usados completos que luego son desarmados para extraer piezas.

**REQ-002**: Cada vehículo debe registrar los siguientes datos

- Marca
- Modelo
- Año
- Número de serie (VIN)
- Tipo de motor
- Tipo de transmisión
- Clase de vehículo

**REQ-003**: El sistema debe permitir asociar piezas extraídas con el vehículo de origen  
**Prioridad:** Media  
**Descripción:** Aunque no es obligatorio, es útil saber de qué vehículo provino cada pieza.

**REQ-004**: Debe registrarse el costo de adquisición del vehículo  
**Prioridad:** Alta  
**Nota:** Se debe registrar para generar una grafica de cuanto falta para amortizar el vehiculo.

**REQ-005**: Los vehículos deben poder tener múltiples fotografías
**Prioridad:** Media

> Implementado REQ-001/2/3 Falta implementar REQ-004 y REQ-005

### 1.2 Inventario de Piezas

**REQ-006**: Cada pieza debe tener los siguientes atributos obligatorios

- Código interno
- Nombre
- Lado/posición (izquierdo, derecho, trasero, delantero, unidireccional)
- Categoría (motor, chasis, eléctrico, etc.)
- Precio de venta
- Cantidad en stock

**REQ-007**: Las piezas deben relacionarse con marca y modelo compatible  
**Prioridad:** Alta  
**Descripción:** Cada pieza debe indicar para qué marca/modelo de vehículo es compatible.

**REQ-008**: Las piezas deben poder asociarse a un vehículo  
**Prioridad:** Media  
**Nota:** Es opcional, pero útil para tracking.

**REQ-009**: Las piezas pueden tener múltiples fotografías  
**Prioridad:** Media  
**Nota:** No todas las piezas tendrán fotos, es opcional.

**REQ-010**: El sistema debe manejar tanto piezas usadas como nuevas sin distinción  
**Prioridad:** Baja  
**Nota:** No se requiere distinguir entre usadas y nuevas.

> REQ-007 debe retirarse; REQ-008 se modifico; Verificar el REQ-009.

## 2. VENTAS Y FACTURACIÓN

### 2.1 Proceso de Venta

**REQ-011**: El sistema debe permitir registrar ventas de piezas inventariadas  
**Prioridad:** Alta  
**Descripción:** Venta normal de piezas que ya están en el inventario.

**REQ-012**: El sistema debe permitir registrar ventas de piezas NO inventariadas  
**Prioridad:** Alta  
**Descripción:** Como el inventario aún no está completo, se necesita vender piezas que no están registradas, capturando: nombre, descripción, marca y modelo manualmente.

**REQ-013**: Cada venta debe generar un ticket con folio único  
**Prioridad:** Alta  
**Formato:** UUID

**REQ-014**: El ticket debe registrar

- Usuario/vendedor que realizó la venta
- Total de la venta
- Método de pago
- Número de items vendidos
- Detalles de los items vendidos <-- agregado en rev1.5
- Fecha y hora

**REQ-015**: Métodos de pago disponibles

- Efectivo
- Tarjeta de crédito
- Tarjeta de débito
- Efectivo contra entrega (domicilio)
- Otro

> Verificar REQ-012 y REQ-014.

### 2.2 Descuentos

**REQ-016**: El sistema debe permitir aplicar descuentos arbitrarios al momento de la venta  
**Prioridad:** Alta  
**Descripción:** Los vendedores pueden hacer descuentos discrecionales.

**REQ-017**: El descuento debe registrarse tanto a nivel de ticket como por pieza individual  
**Prioridad:** Alta

**REQ-018**: Debe registrarse el precio original y el precio con descuento  
**Prioridad:** Alta  
**Nota:** Para auditoría y reportes.

> REQ-016 verificar en el front y que lo acepte el back, retirar el REQ-017, solo de debe guardar el precio a nivel ticket Unificar en el REQ-18: Debe registrarse solo el precio a la hora del ticked y mostrar la diferencia del precio, pude hacerce en el front.

### 2.3 Apartados

**REQ-019**: El sistema debe permitir apartar piezas con anticipo  
**Prioridad:** Alta  
**Descripción:** Los clientes pueden reservar piezas dejando un anticipo.

**REQ-020**: Los apartados deben registrar

- Cliente que aparta
- Pieza(s) apartadas
- Monto de anticipo
- Saldo pendiente
- Fecha límite del apartado
- Estado del apartado

**REQ-021**: Estados de apartado

- **ACTIVO**: Apartado vigente, pieza reservada
- **COMPLETADO**: Cliente pagó el saldo, se generó venta
- **CANCELADO**: Cliente canceló el apartado
- **VENCIDO**: Se cumplió la fecha límite sin pagar

**REQ-022**: Al completar un apartado debe generarse automáticamente la venta definitiva  
**Prioridad:** Alta

> Implementar los requisitos desde REQ-19, hasta REQ-22, los estados de apartado en REQ-21 deben ser en ingles.

### 2.4 Entregas a Domicilio

**REQ-023**: Cada venta debe indicar si fue entrega a domicilio o en tienda  
**Prioridad:** Alta

**REQ-024**: Para entregas a domicilio debe registrarse

- Datos del cliente (nombre, teléfono, dirección)
- Dirección de entrega completa

**REQ-025**: Debe registrarse el estado de la entrega

- **PENDIENTE**: Pedido listo para envío
- **EN RUTA**: Pedido en camino
- **ENTREGADO**: Entrega exitosa
- **FALLIDO**: No se pudo entregar

> Falta por inplementar los REQ desde 023 hasta 025, pero en una siguiente version de la app.

### 2.5 Devoluciones

**REQ-026**: El sistema debe permitir eliminar/anular ventas por devolución  
**Prioridad:** Alta  
**Nota:** Solo se aceptan devoluciones por garantía o falla de la pieza.

**REQ-027**: Debe registrarse el motivo de la devolución  
**Prioridad:** Media

**REQ-028**: Las devoluciones por garantía deben vincularse al proceso de garantías  
**Prioridad:** Alta

> Falta verificar los REQ desde 026 hasta 028: 027 falta por implementar.

## 3. GARANTÍAS

### 3.1 Gestión de Garantías (Modificado REQ 0312: agregado Registro en calendario.)

**REQ-029**: Solo ciertas piezas/categorías tendrán garantía  
**Prioridad:** Alta  
**Nota:** La garantía es configurable por categoría de pieza.

**REQ-030**: La garantía debe solicitarse explícitamente por el cliente al momento de la venta  
**Prioridad:** Alta  
**Descripción:** No todas las ventas incluyen garantía automáticamente.

**REQ-031**: Cada venta con garantía debe registrar

- Duración en meses
- Fecha de expiración calculada
- Términos aplicables
- Registrar el vencimiento en calendario.

**REQ-032**: Estados de garantía

- **ACTIVA**: Garantía vigente
- **EXPIRADA**: Plazo vencido
- **RECLAMADA**: Cliente usó la garantía
- **ANULADA**: Garantía cancelada por incumplimiento de términos

> Verificar la implementacion los estado en REQ-032 deben ser en ingles.

### 3.2 Reclamaciones de Garantía

**REQ-033**: Al reclamar una garantía debe registrarse  
**Prioridad:** Alta

- Fecha de reclamación
- Motivo/descripción del problema
- Evidencia fotográfica del defecto

**REQ-034**: El sistema debe registrar la resolución aplicada  
**Prioridad:** Alta  
Opciones:

- Cambio de pieza
- Reembolso completo

**REQ-035**: Si se cambia la pieza, debe registrarse la nueva venta y vincularla con la reclamación  
**Prioridad:** Media

**REQ-036**: Las reclamaciones deben modificar/actualizar el registro de venta original  
**Prioridad:** Alta  
**Nota:** Para mantener historial completo.

> Verificar la implementacion de los requisitos desde REQ-033 hasta 036.

## 4. CLIENTES

### 4.1 Registro de Clientes (Modificado REQ-037, REQ-038 ELIMINADO REQ-40)

**REQ-037**: El sistema debe registrar clientes

- Pedidos/apartados
- Quejas y reclamaciones
- Entregas a domicilio

**REQ-038**: Cada cliente debe tener

- Nombre completo (obligatorio)
- Teléfono (obligatorio)
- Email (opcional)
- Dirección (opcional, ligada a RFC)

**REQ-039**: Los clientes pueden tener datos fiscales (RFC) para facturación  
**Prioridad:** Alta

> Verificar los REQ : 037 - 039

### 4.2 Quejas y Problemas (Modificacion REQ-041)

**REQ-041**: El sistema debe registrar quejas/problemas reportados por clientes  
**Prioridad:** Media
**NOTA:** Esto en el cliente WEB.

**REQ-042**: Cada queja debe registrar

- Descripción del problema
- Estado actual
- Fecha de registro
- Cliente que reporta
- Ticket/venta relacionada

**REQ-043**: Estados de queja

- **PENDIENTE**: Queja sin atender
- **ATENDIDA**: Problema resuelto
- **RECHAZADA**: Queja no procede
- **NO ENCONTRADA**: No se identificó el problema

**REQ-044**: Debe registrarse la resolución aplicada y fecha de resolución  
**Prioridad:** Media

> Ingresar en verciones posteriores.

## 5. GASTOS OPERATIVOS

### 5.1 Control de Gastos

**REQ-045**: El sistema debe registrar gastos mensuales del negocio  
**Prioridad:** Alta

**REQ-046**: Cada gasto debe tener

- Concepto/descripción
- Monto
- Fecha del gasto
- Categoría

**REQ-047**: Categorías de gasto

- Compra de vehículos
- Servicios (luz, agua, internet)
- Rentas
- Sueldos/nómina
- Mantenimiento
- Otros gastos

**REQ-048**: Debe calcularse automáticamente el total de gastos por mes  
**Prioridad:** Alta

> Verificar la implementacion de los REQ desde 045 - 048, e implementar en cliente graficas para mostrar los datos en el cliente solo en el el panel ADMIN.

### 5.2 Análisis de Rentabilidad

**REQ-049**: El sistema debe mostrar cuando las ventas del mes cubren los gastos  
**Prioridad:** Alta  
**Descripción:** Indicador visual de "meta cumplida".

**REQ-050**: Debe calcularse: Total ventas - Total gastos = Utilidad/Pérdida mensual  
**Prioridad:** Alta

**REQ-051**: Debe mostrarse un indicador claro cuando ventas > gastos  
**Prioridad:** Media

> Verificar implementacion de todos los requisitos de REQ-049 hasta 051; Tambien deben implementarse mediante graficas en cliente para el perfil ADMIN.

## 6. REPORTES

### 6.1 Reportes de Ventas (Modificado REQ-053)

**REQ-052**: Reporte de ventas por período  
**Prioridad:** Alta  
**Filtros disponibles:**

- Por día
- Por semana
- Por mes
- Por año
- Rango personalizado

**REQ-053**: Reporte de piezas más vendidas  
**Prioridad:** Media  
**Descripción:** Requerido, con graficas para mejorar el análisis.

**REQ-054**: Los reportes de venta deben incluir

- Total vendido
- Número de transacciones
- Desglose por método de pago
- Descuentos aplicados

> Implementar los requisitos faltantes desde REQ-052 hasta 054.

### 6.2 Reportes Financieros

**REQ-055**: Reporte mensual de gastos vs ingresos  
**Prioridad:** Alta  
**Visualización:** Comparativa clara de ambos rubros.

**REQ-056**: Cálculo de utilidad mensual  
**Prioridad:** Alta  
**Fórmula:** Ingresos - Gastos = Utilidad Neta

> Verificar redundancia de requisitos. en esta seccion.

## 7. USUARIOS Y PERMISOS

### 7.1 Roles del Sistema (Modificado REQ-058, REQ-060)

**REQ-057**: El sistema debe tener 5 roles predefinidos  
**Prioridad:** Alta

**REQ-058**: Rol USER  
**Permisos:**

- Acceso a Endponits públicas
- Consultas básicas de catálogo
- Sin permisos de modificación

**REQ-059**: Rol SELLER (Vendedor)  
**Permisos:**

- Realizar ventas
- Aplicar descuentos
- Modificar precios en ventas
- Gestionar apartados
- Registrar entregas
- Consultar inventario

**REQ-060**: Rol ACCOUNTANT (Contador)  
**Permisos:**

- Acceso de solo lectura a:
  - Datos de ventas
  - Reportes financieros
  - Gastos operativos
  - Facturas emitidas
- Sin permisos de modificación

**NOTA:** Requerira un dasboard para en el cliente, su cliente debera estar en el CLIENTE WEB tambien registrar su correo y o numero, para comunicacion interna..

**REQ-061**: Rol INVENTORY_MANAGER (Administrador de Inventario)  
**Permisos:**

- Agregar, editar, eliminar piezas
- Agregar, editar, eliminar vehículos donantes
- Gestionar categorías
- Actualizar precios del inventario
- Cargar imágenes de productos
- Ajustes de inventario

**REQ-062**: Rol ADMIN (Administrador)  
**Permisos:**

- Acceso total al sistema
- Gestión de usuarios
- Configuración del sistema
- Acceso a todos los módulos

> Implentar lo faltante en back y en front.

### 7.2 Auditoría

**REQ-063**: El sistema debe registrar inicios de sesión  
**Prioridad:** Media  
**Información a registrar:**

- Usuario
- Dirección IP
- Fecha y hora
- Navegador/dispositivo
- Éxito o fallo del login

**REQ-064**: El sistema debe registrar logs de aplicación para errores críticos  
**Prioridad:** Media  
**Niveles:** INFO, DEBUG, WARN, ERROR, FATAL

> Verificar que este bien implementados los REQ 063 y 064.

## 8. FACTURACIÓN FISCAL

### 8.1 Datos Fiscales

**REQ-065**: El sistema debe almacenar datos fiscales de clientes para facturación  
**Prioridad:** Alta

**REQ-066**: Datos fiscales requeridos

- RFC (13 caracteres)
- Razón social o nombre completo
- Dirección fiscal completa
- Código postal
- Régimen fiscal
- Uso de CFDI
- Email para envío de factura

**REQ-067**: Las facturas deben relacionarse con tickets de venta  
**Prioridad:** Alta  
**Nota:** Una factura puede o no tener ticket asociado.

**REQ-068**: Cada factura debe registrar

- Folio interno (UUID)
- Número de folio fiscal del SAT
- RFC del receptor
- URL del documento (XML/PDF)
- Sello digital del SAT
- Fecha de emisión

> verificar la implementacion de los requisitos desde 065 hasta 068, ademas agregar un nuevo requisito: Mandar correo al perfil contador y mesaje dias antes del final de mes para notificacion de registro de facturas, cambio puede ser implementado en version 2.

## 9. IMÁGENES Y MULTIMEDIA

### 9.1 Fotografías y Archivos (Modificacion REQ-069,071 y 072)

**REQ-069**: Los vehículos adquiridos pueden tener múltiples fotografías  
**Prioridad:** Media  
**Formato:** URLs a almacenamiento externo

**REQ-070**: Las piezas inventariadas pueden tener múltiples fotografías  
**Prioridad:** Media  
**Nota:** Es opcional, no todas las piezas tendrán fotos.

**REQ-071**: Las ventas deben tener fotografías de las piezas vendidas  
**Prioridad:** Media  
**Uso:** Para piezas no inventariadas o evidencia de venta.

**REQ-072**: Los tickets deben tener fotografías de las piezas vendidas.  
**Prioridad:** Baja  
**Uso:** listado de los productos vendidos o solo de las piezas a nivel general.

**REQ-073**: Las reclamaciones de garantía deben poder adjuntar evidencia fotográfica  
**Prioridad:** Alta  
**Uso:** Demostrar el defecto o falla de la pieza.

> Verificar que se cumplan los requisitos desde 069 hasta 073.

## 10. BÚSQUEDA Y CONSULTAS

### 10.1 Búsqueda de Piezas (Eliminado REQ-80)

**REQ-074**: Búsqueda por código de pieza  
**Prioridad:** Alta  
**Tipo:** Búsqueda exacta

**REQ-075**: Búsqueda por nombre de pieza  
**Prioridad:** Alta  
**Tipo:** Búsqueda difusa (LIKE)

**REQ-076**: Búsqueda por marca del vehículo  
**Prioridad:** Alta

**REQ-077**: Búsqueda por modelo del vehículo  
**Prioridad:** Alta

**REQ-078**: Búsqueda por año del vehículo  
**Prioridad:** Media

**REQ-079**: Búsqueda por categoría de pieza  
**Prioridad:** Alta

> verificar implementacion desde REQ- 074 hasta 079

## 11. CATÁLOGO WEB (FUTURO)

### 11.1 Publicación en Línea

**REQ-081**: El sistema debe poder exportar un catálogo informativo para web  
**Prioridad:** Baja  
**Nota:** Funcionalidad futura, no prioritaria.

**REQ-082**: El catálogo debe mostrar

- Piezas disponibles
- Precios actualizados
- Fotografías
- Compatibilidad con vehículos

**REQ-083**: El catálogo debe ser de solo lectura  
**Nota:** Sin funcionalidad de compra en línea por ahora.

> pendiente para version 2, o relese WEB.

## CONCLUCIONES DE REVISION 1.5 A 27/11/2025

Se debe verificae el cumplimiento de los requisitos, aunque no esten marcados, se requiere el lanzamineto del relese movil, como prioridad, dejando funcionalidades no basicas para version 2 Movil, o bien en libreracion de relese WEB.

### Consideraciones

1. Verificaciones inmediatas
    - Base de datos cumple requisitos
    - API responde las necesdades de el cliente.
    - Base de datos con Vistas, o fuciones para reportes y consultas especiales en los requisitos.

2. Arquitectura actual:
    - Despliegue en PAAS de backend (PRIORIDAD ALTA)
    - Distrubucion de clleiente MOVIL en si version 1, MVP.(ALTA)

## 🔄 CONTROL DE CAMBIOS

| Versión | Fecha    | Autor      | Cambios           |
| ------- | -------- | ---------- | ----------------- |
| 1.0     | Oct 2025 | Eduardo | Documento inicial |
| 1.5   |nov 2025   |YefraSoft  | Verificacion/revision de requisitos previo a v1|
