#!/bin/bash

# =============================================================================
# Script de Testing para API REST
# =============================================================================

# Configuración
# En WSL2, usar la IP del host en lugar de localhost
HOST_IP=$(ip route | grep default | awk '{print $3}')
BASE_URL="http://${HOST_IP}:8080"
RESULTS_FILE="test_results_$(date +%Y%m%d_%H%M%S).md"
TOKEN=""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contadores
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# =============================================================================
# Funciones Auxiliares
# =============================================================================

print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_test() {
    echo -e "${YELLOW}[TEST]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[✓]${NC} $1"
    ((PASSED_TESTS++))
    ((TOTAL_TESTS++))
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
    ((FAILED_TESTS++))
    ((TOTAL_TESTS++))
}

# Función para hacer peticiones HTTP
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local auth=$4
    
    local url="${BASE_URL}${endpoint}"
    local headers=(-H "Content-Type: application/json")
    
    if [ "$auth" = "true" ] && [ -n "$TOKEN" ]; then
        headers+=(-H "Authorization: Bearer $TOKEN")
    fi
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" "${headers[@]}" -d "$data")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" "${headers[@]}")
    fi
    
    echo "$response"
}

# Función para extraer el código HTTP
get_status_code() {
    echo "$1" | tail -n1
}

# Función para extraer el body
get_body() {
    echo "$1" | sed '$d'
}

# =============================================================================
# Tests de Autenticación
# =============================================================================

test_auth() {
    print_header "TESTING AUTHENTICATION"
    
    # Test: Register
    print_test "POST /api/auth/register - Crear nuevo usuario"
    data='{
        "username": "testuser@example.com",
        "password": "SecurePass123!",
        "name": "Test User"
    }'
    response=$(make_request "POST" "/api/auth/register" "$data" "false")
    status=$(get_status_code "$response")
    
    if [ "$status" = "201" ]; then
        print_success "Usuario registrado correctamente (201)"
    else
        print_error "Error al registrar usuario. Status: $status"
    fi
    
    # Test: Login válido
    print_test "POST /api/auth/login - Login con credenciales válidas"
    data='{
        "username": "testuser@example.com",
        "password": "SecurePass123!"
    }'
    response=$(make_request "POST" "/api/auth/login" "$data" "false")
    status=$(get_status_code "$response")
    body=$(get_body "$response")
    
    if [ "$status" = "200" ]; then
        TOKEN=$(echo "$body" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
        if [ -n "$TOKEN" ]; then
            print_success "Login exitoso, token obtenido (200)"
        else
            print_error "Login exitoso pero no se obtuvo token"
        fi
    else
        print_error "Error en login. Status: $status"
    fi
    
    # Test: Login inválido
    print_test "POST /api/auth/login - Login con credenciales inválidas"
    data='{
        "username": "testuser@example.com",
        "password": "WrongPassword"
    }'
    response=$(make_request "POST" "/api/auth/login" "$data" "false")
    status=$(get_status_code "$response")
    
    if [ "$status" = "401" ]; then
        print_success "Credenciales inválidas rechazadas correctamente (401)"
    else
        print_error "Debería retornar 401. Status: $status"
    fi
}

# =============================================================================
# Tests de Brands
# =============================================================================

test_brands() {
    print_header "TESTING BRANDS"
    
    # Test: Crear brand
    print_test "POST /api/brands - Crear nueva marca"
    data='{"name": "HONDA"}'
    response=$(make_request "POST" "/api/brands" "$data" "true")
    status=$(get_status_code "$response")
    body=$(get_body "$response")
    
    if [ "$status" = "201" ]; then
        BRAND_ID=$(echo "$body" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        print_success "Marca creada correctamente (201)"
    else
        print_error "Error al crear marca. Status: $status"
    fi
    
    # Test: Listar brands
    print_test "GET /api/brands - Listar todas las marcas"
    response=$(make_request "GET" "/api/brands" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Marcas listadas correctamente (200)"
    else
        print_error "Error al listar marcas. Status: $status"
    fi
    
    # Test: Obtener brand por ID
    if [ -n "$BRAND_ID" ]; then
        print_test "GET /api/brands/{id} - Obtener marca por ID"
        response=$(make_request "GET" "/api/brands/$BRAND_ID" "" "true")
        status=$(get_status_code "$response")
        
        if [ "$status" = "200" ]; then
            print_success "Marca obtenida correctamente (200)"
        else
            print_error "Error al obtener marca. Status: $status"
        fi
    fi
    
    # Test: Brand no existente
    print_test "GET /api/brands/99999 - Marca inexistente"
    response=$(make_request "GET" "/api/brands/99999" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "404" ]; then
        print_success "Marca inexistente retorna 404 correctamente"
    else
        print_error "Debería retornar 404. Status: $status"
    fi
}

# =============================================================================
# Tests de Models
# =============================================================================

test_models() {
    print_header "TESTING MODELS"
    
    # Test: Crear modelo
    if [ -n "$BRAND_ID" ]; then
        print_test "POST /api/models - Crear nuevo modelo"
        data="{
            \"name\": \"Civic\",
            \"year\": 2024,
            \"brandId\": $BRAND_ID
        }"
        response=$(make_request "POST" "/api/models" "$data" "true")
        status=$(get_status_code "$response")
        body=$(get_body "$response")
        
        if [ "$status" = "201" ]; then
            MODEL_ID=$(echo "$body" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
            print_success "Modelo creado correctamente (201)"
        else
            print_error "Error al crear modelo. Status: $status"
        fi
    fi
    
    # Test: Buscar por marca
    if [ -n "$BRAND_ID" ]; then
        print_test "GET /api/models/search/by-brand/{brandId} - Buscar modelos por marca"
        response=$(make_request "GET" "/api/models/search/by-brand/$BRAND_ID" "" "true")
        status=$(get_status_code "$response")
        
        if [ "$status" = "200" ]; then
            print_success "Modelos encontrados por marca (200)"
        else
            print_error "Error al buscar modelos. Status: $status"
        fi
    fi
    
    # Test: Buscar por nombre
    print_test "GET /api/models/search/by-name?name=civ - Buscar modelos por nombre"
    response=$(make_request "GET" "/api/models/search/by-name?name=civ" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Búsqueda por nombre funciona (200)"
    else
        print_error "Error al buscar por nombre. Status: $status"
    fi
}

# =============================================================================
# Tests de Parts
# =============================================================================

test_parts() {
    print_header "TESTING PARTS"
    
    # Test: Crear parte
    if [ -n "$MODEL_ID" ]; then
        print_test "POST /api/parts - Crear nueva parte"
        data="{
            \"code\": \"ALT-$(date +%s)\",
            \"name\": \"Alternador Premium\",
            \"side\": \"UNIDIRECTIONAL\",
            \"categoryType\": \"ELECTRICAL\",
            \"color\": \"Silver\",
            \"price\": 3000.0,
            \"quantity\": 10,
            \"modelId\": $MODEL_ID
        }"
        response=$(make_request "POST" "/api/parts" "$data" "true")
        status=$(get_status_code "$response")
        body=$(get_body "$response")
        
        if [ "$status" = "201" ]; then
            PART_ID=$(echo "$body" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
            print_success "Parte creada correctamente (201)"
        else
            print_error "Error al crear parte. Status: $status"
        fi
    fi
    
    # Test: Listar partes
    print_test "GET /api/parts - Listar todas las partes"
    response=$(make_request "GET" "/api/parts" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Partes listadas correctamente (200)"
    else
        print_error "Error al listar partes. Status: $status"
    fi
    
    # Test: Validar precio > 0
    print_test "POST /api/parts - Validar precio > 0"
    data="{
        \"code\": \"TEST-001\",
        \"name\": \"Test Part\",
        \"price\": -100,
        \"quantity\": 5,
        \"modelId\": $MODEL_ID
    }"
    response=$(make_request "POST" "/api/parts" "$data" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "400" ]; then
        print_success "Precio negativo rechazado correctamente (400)"
    else
        print_error "Debería rechazar precio negativo. Status: $status"
    fi
}

# =============================================================================
# Tests de Customers
# =============================================================================

test_customers() {
    print_header "TESTING CUSTOMERS"
    
    # Test: Crear cliente
    print_test "POST /api/customers - Crear nuevo cliente"
    data="{
        \"name\": \"Jane Smith\",
        \"email\": \"jane$(date +%s)@example.com\",
        \"phone\": \"0987654321\",
        \"rfc\": \"XAXX010101000\"
    }"
    response=$(make_request "POST" "/api/customers" "$data" "true")
    status=$(get_status_code "$response")
    body=$(get_body "$response")
    
    if [ "$status" = "201" ]; then
        CUSTOMER_ID=$(echo "$body" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        print_success "Cliente creado correctamente (201)"
    else
        print_error "Error al crear cliente. Status: $status"
    fi
    
    # Test: Buscar por teléfono
    print_test "GET /api/customers/search/by-phone/0987654321 - Buscar por teléfono"
    response=$(make_request "GET" "/api/customers/search/by-phone/0987654321" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Cliente encontrado por teléfono (200)"
    else
        print_error "Error al buscar por teléfono. Status: $status"
    fi
    
    # Test: Listar clientes
    print_test "GET /api/customers - Listar todos los clientes"
    response=$(make_request "GET" "/api/customers" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Clientes listados correctamente (200)"
    else
        print_error "Error al listar clientes. Status: $status"
    fi
}

# =============================================================================
# Tests de Tickets
# =============================================================================

test_tickets() {
    print_header "TESTING TICKETS"
    
    # Test: Crear ticket
    if [ -n "$CUSTOMER_ID" ] && [ -n "$PART_ID" ]; then
        print_test "POST /api/tickets - Crear nuevo ticket"
        data="{
            \"userId\": 1,
            \"customerId\": $CUSTOMER_ID,
            \"paymentMethod\": \"CASH\",
            \"items\": [
                {
                    \"partId\": $PART_ID,
                    \"quantity\": 1,
                    \"price\": 2500.0,
                    \"hasWarranty\": true,
                    \"warrantyExpirationDate\": \"2024-12-31\"
                }
            ]
        }"
        response=$(make_request "POST" "/api/tickets" "$data" "true")
        status=$(get_status_code "$response")
        body=$(get_body "$response")
        
        if [ "$status" = "201" ]; then
            TICKET_FOLIO=$(echo "$body" | grep -o '"folio":"[^"]*' | cut -d'"' -f4)
            print_success "Ticket creado correctamente (201)"
        else
            print_error "Error al crear ticket. Status: $status"
        fi
    fi
    
    # Test: Obtener ticket por folio
    if [ -n "$TICKET_FOLIO" ]; then
        print_test "GET /api/tickets/{folio} - Obtener ticket por folio"
        response=$(make_request "GET" "/api/tickets/$TICKET_FOLIO" "" "true")
        status=$(get_status_code "$response")
        
        if [ "$status" = "200" ]; then
            print_success "Ticket obtenido correctamente (200)"
        else
            print_error "Error al obtener ticket. Status: $status"
        fi
    fi
}

# =============================================================================
# Tests de Sales
# =============================================================================

test_sales() {
    print_header "TESTING SALES"
    
    # Test: Buscar ventas por ticket
    if [ -n "$TICKET_FOLIO" ]; then
        print_test "GET /api/sales/search/by-ticket/{ticketFolio} - Buscar ventas por ticket"
        response=$(make_request "GET" "/api/sales/search/by-ticket/$TICKET_FOLIO" "" "true")
        status=$(get_status_code "$response")
        body=$(get_body "$response")
        
        if [ "$status" = "200" ]; then
            SALE_ID=$(echo "$body" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
            print_success "Ventas encontradas por ticket (200)"
        else
            print_error "Error al buscar ventas. Status: $status"
        fi
    fi
    
    # Test: Listar todas las ventas
    print_test "GET /api/sales - Listar todas las ventas"
    response=$(make_request "GET" "/api/sales" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Ventas listadas correctamente (200)"
    else
        print_error "Error al listar ventas. Status: $status"
    fi
}

# =============================================================================
# Tests de Customer Issues
# =============================================================================

test_customer_issues() {
    print_header "TESTING CUSTOMER ISSUES"
    
    # Test: Crear issue
    if [ -n "$CUSTOMER_ID" ] && [ -n "$SALE_ID" ]; then
        print_test "POST /api/customer-issues - Crear nuevo issue"
        data="{
            \"customerId\": $CUSTOMER_ID,
            \"title\": \"Problema con garantía\",
            \"description\": \"El alternador falló antes de tiempo\",
            \"saleId\": $SALE_ID,
            \"claimType\": \"EXCHANGE\"
        }"
        response=$(make_request "POST" "/api/customer-issues" "$data" "true")
        status=$(get_status_code "$response")
        
        if [ "$status" = "201" ]; then
            print_success "Issue creado correctamente (201)"
        else
            print_error "Error al crear issue. Status: $status"
        fi
    fi
    
    # Test: Buscar por estado
    print_test "GET /api/customer-issues/search/by-status/PENDING - Buscar por estado"
    response=$(make_request "GET" "/api/customer-issues/search/by-status/PENDING" "" "true")
    status=$(get_status_code "$response")
    
    if [ "$status" = "200" ]; then
        print_success "Issues encontrados por estado (200)"
    else
        print_error "Error al buscar issues. Status: $status"
    fi
}

# =============================================================================
# Generar Reporte
# =============================================================================

generate_report() {
    print_header "GENERANDO REPORTE"
    
    cat > "$RESULTS_FILE" << EOF
# Reporte de Tests de API
**Fecha:** $(date +"%Y-%m-%d %H:%M:%S")

## Resumen
- **Total de Tests:** $TOTAL_TESTS
- **Tests Exitosos:** $PASSED_TESTS
- **Tests Fallidos:** $FAILED_TESTS
- **Porcentaje de Éxito:** $(awk "BEGIN {printf \"%.2f\", ($PASSED_TESTS/$TOTAL_TESTS)*100}")%

## Detalles
EOF
    
    echo -e "\n${GREEN}Reporte generado: $RESULTS_FILE${NC}"
}

# =============================================================================
# Main
# =============================================================================

main() {
    echo -e "${BLUE}"
    echo "╔════════════════════════════════════════╗"
    echo "║  API Testing Script                    ║"
    echo "║  Base URL: $BASE_URL              ║"
    echo "╚════════════════════════════════════════╝"
    echo -e "${NC}"
    
    test_auth
    test_brands
    test_models
    test_parts
    test_customers
    test_tickets
    test_sales
    test_customer_issues
    
    print_header "RESUMEN FINAL"
    echo -e "Total de Tests: ${BLUE}$TOTAL_TESTS${NC}"
    echo -e "Tests Exitosos: ${GREEN}$PASSED_TESTS${NC}"
    echo -e "Tests Fallidos: ${RED}$FAILED_TESTS${NC}"
    
    if [ $TOTAL_TESTS -gt 0 ]; then
        success_rate=$(awk "BEGIN {printf \"%.2f\", ($PASSED_TESTS/$TOTAL_TESTS)*100}")
        echo -e "Tasa de Éxito: ${BLUE}${success_rate}%${NC}"
    fi
    
    generate_report
}

# Ejecutar tests
main