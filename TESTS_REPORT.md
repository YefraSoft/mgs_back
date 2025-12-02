# Reporte de Tests de Endpoints - 2025-11-25

## Estado General

**Iniciado:** Tests de todos los endpoints de la API

## Endpoints a Probar

### Autenticación

- [OK] POST /api/auth/login
- [OK] POST /api/auth/register

Details:

- Errores no retornados al usuario, solo mensaje generico
- Sin validacion en nombre del usuario (no username; name)
- Rol: Numeric value (454) out of range of Java byte: mal manejo del roleId
- agregar Refresh tokken

### Brands

- [OK] GET /api/brands
- [OK] GET /api/brands/{id}
  - Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: "dfgd$" -> Descripcion en erroes y validacion.
- [OK] GET /api/brands/search
- [SO-SO] POST /api/brands
  - Sin validacion: "name": "#$#$!" aceptado
- [SO-SO] PUT /api/brands/{id}
  - Sin validacion: "name": "$$%$%$#$&" aceptado
- [OK] DELETE /api/brands/{id}

### Models

- [OK] GET /api/models
- [OK] GET /api/models/{id}
- [DROP] GET /api/models/search/by-brand/{brandId}
  - Endpoint Repetido /api/models/{id}.
- [OK] GET /api/models/search/by-year/{year}
- [OK] GET /api/models/search/by-name
- [DROP] GET /api/models/search/by-brand-and-year
- [SO-SO] POST /api/models
  - Seriales repetidos
  - Se pueden poner varias veces el mismo registro, solo se suma id
- [OK] PUT /api/models/{id}
  - Se puede realizar nveces la llamada de actualizacion al mismo objeto, verificar antes de actualizar.
- [OK] DELETE /api/models/{id}

### Customers

- [OK] GET /api/customers
- [OK] GET /api/customers/{id}
- [SO-SO] GET /api/customers/search/by-phone/{phone}
  - numero debe de ser ecxacto
- [SO-SO] GET /api/customers/search/by-rfc/{rfc}
  - rfc debe de ser ecxacto
- [OK] GET /api/customers/search/by-name
- [OK] POST /api/customers
- [OK] PUT /api/customers/{id}
  - No se pouede modificar todos los valores individualmente, solo el rfc puede no ser enviado.
- [OK] DELETE /api/customers/{id}

### Customer Issues

- [OK] GET /api/customer-issues
- [OK] GET /api/customer-issues/{id}
- [OK] GET /api/customer-issues/search/by-status/{status}
- [OK] GET /api/customer-issues/search/by-customer/{customerId}
- [DROP] GET /api/customer-issues/search/by-status-and-customer
- [SO-SO] POST /api/customer-issues
  - Verificar la duplicidad de reportes, se puede registrar n veses el mismo tipo de reporte de la misma empresa.
- [SO-SO] PUT /api/customer-issues/{id}
  - Solo siempre se debe mandar el problema, aunque no se modifique.
- [OK] DELETE /api/customer-issues/{id}

### Tickets

- [SO-SO] GET /api/tickets

  - "folio": "ddd1876c-a73e-11f0-b54b-f4b5203d1017",
            "userId": 2,
            "userName": "Ana López",
            "total": 3100.00,
            "paymentMethod": "CASH",
           "items": 2,
         "date": "2025-10-12T01:41:29",
         "sales": [
             {
                 "id": 1,
                 "ticketFolio": "ddd1876c-a73e-11f0-b54b-f4b5203d1017",
                 "partId": 1,
                 "partName": "Alternador",
                 "quantity": 1,
                    "price": 1200.00
             },
              {
                 "id": 2,
                 "ticketFolio": "ddd1876c-a73e-11f0-b54b-f4b5203d1017",
                  "partId": 2,
                 "partName": "Parachoques delantero",
                 "quantity": 1,
                 "price": 2500.00
                }
            ]
  - Modificar la respuesta, no enviar ticketFolio dentro de sales, para reducir el objeto

- [OK] GET /api/tickets/{folio}
  - cacheo no se refresca al actualizar la BD.
- [SO-SO] GET /api/tickets/user/{userId}
  - Cambiar userName por seller en objeto y ruta.
- [FAIL] GET /api/tickets/search/by-date
  - search/by-date?startDate=2025-11-01 00:00:00&endDate=2025-11-25 23:59:59 : simpleificar las fechas en la ruta.
  - agregar busqueda por dia, mes y año, en el endpoint search/by-date, ?day, month etc.
- [FAIL] POST /api/tickets

### Sales

- [OK] GET /api/sales
- [CRITICAL] GET /api/sales/{id}
  - Devuelve la contraseña y dato sencibles del vendedor.
- [OK] GET /api/sales/search/by-ticket/{ticketFolio}
- [OK] GET /api/sales/search/by-part/{partId}

### Parts

- [OK] GET /api/parts
- [OK] GET /api/parts/{id}
- [SO-SO] POST /api/parts
  - necesita validaciones
- [FAIL] PUT /api/parts/{id}
  - si existe no se modifica codiogo puede ser null, color ser null
  - modificar obligatoriamente nombre, side, categori, pecio
  - Cantidad se modifica si no se envia.
  - model id no funciona: {
    "code": "ALT-002",
    "name": "Alternador Premium",
    "side": "UNIDIRECTIONAL",
    "categoryType": "ELECTRICAL",
    "price": -3000.0,
    "quantity": -3,
    "modelId": 3                         <--
}
- [OK] DELETE /api/parts/{id}

## Resultados

### TEST manuales en postman

- Tickeds
**Detalles:** Modificar tickeds para que hacepten el codigigo del cliente que realizo la compra, modificar para haceptar garantias. "hasWarranty": true, "warrantyExpirationDate": "2024-12-31"; Cacheo

### TEST sinteticos

#### WSL

PS C:\Users\yefra\Escritorio\Proyectos\DeshuaceGarcias\mgs_back> wsl -e bash -c "cd /mnt/c/Users/yefra/Escritorio/Proyectos/DeshuaceGarcias/mgs_back && bash test_bash_smp.sh 2>&1 | tail -30"
Token obtained: eyJhbGciOiJIUzUxMiJ9...

=== BRANDS TESTS ===
[POST] /api/brands - Create brand ... OK (HTTP 201)
[GET] /api/brands - Get all brands ... OK (HTTP 200)

=== MODELS TESTS ===
[GET] /api/models - Get all models ... OK (HTTP 200)
[GET] /api/models/search/by-name?name=civic - Search models by name ... OK (HTTP 200)

=== PARTS TESTS ===
[GET] /api/parts - Get all parts ... OK (HTTP 200)

=== CUSTOMERS TESTS ===
[POST] /api/customers - Create customer ... OK (HTTP 201)
[GET] /api/customers - Get all customers ... OK (HTTP 200)

=== TICKETS TESTS ===
[GET] /api/tickets - Get all tickets ... OK (HTTP 200)

=== SALES TESTS ===
[GET] /api/sales - Get all sales ... OK (HTTP 200)

SUMMARY

Tests Passed: 11
Tests Failed: 0
Success Rate: 100%

#### PSH

PS C:\Users\yefra\Escritorio\Proyectos\DeshuaceGarcias\mgs_back> .\test_endpoints_windows.ps1

API Testing Script - PowerShell                                                                                                                                                                                              URL: <http://localhost:8080>                                                                                                                                                                                                                                                                                                                                                                                                                                Verificando conexion...                                                                                                                                                                                                      OK - Servidor responde (requiere autenticacion)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            AUTHENTICATION

[TEST] POST /api/auth/register - Registrar nuevo usuario
[OK] POST /api/auth/register
[TEST] POST /api/auth/login                                                                                                                                                                                                  [OK] POST /api/auth/login                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   BRANDS

[TEST] GET /api/brands
[OK] GET /api/brands
[TEST] POST /api/brands                                                                                                                                                                                                      [OK] POST /api/brands                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    MODELS

[TEST] GET /api/models
[OK] GET /api/models
[TEST] POST /api/models                                                                                                                                                                                                      [OK] POST /api/models                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        PARTS

[TEST] GET /api/parts
[OK] GET /api/parts
[TEST] POST /api/parts                                                                                                                                                                                                       [OK] POST /api/parts
CUSTOMERS
========================================

[TEST] GET /api/customers
[OK] GET /api/customers
[TEST] POST /api/customers
[OK] POST /api/customers

TICKETS
========================================

[TEST] GET /api/tickets
[OK] GET /api/tickets
[TEST] POST /api/tickets
[OK] POST /api/tickets

SALES
========================================

[TEST] GET /api/sales
[OK] GET /api/sales

RESUMEN FINAL
========================================

Total de Tests:  13
Tests Exitosos:  13
Tests Fallidos:  0
Tasa de Exito:   100%
