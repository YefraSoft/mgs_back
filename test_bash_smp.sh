#!/bin/bash

# Script de Testing para API REST - Version Simplificada
# Usando IP del host Windows desde WSL2

# Obtener IP del host
HOST_IP=$(ip route | grep default | awk '{print $3}')
BASE_URL="http://${HOST_IP}:8080"
API_TOKEN=""
RANDOM_ID=$((RANDOM % 10000))
TEST_USER="testuser${RANDOM_ID}"
TEST_PASS="TestPass123!"

echo "=================================================="
echo "API Testing Script"
echo "Base URL: $BASE_URL"
echo "Test User: $TEST_USER"
echo "=================================================="
echo ""

PASSED=0
FAILED=0

# Funcion para hacer test y contar resultados
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_code=$4
    local description=$5
    
    echo -n "[$method] $endpoint - $description ... "
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $API_TOKEN" \
            -d "$data")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $API_TOKEN")
    fi
    
    status=$(echo "$response" | tail -n1)
    
    if [ "$status" = "$expected_code" ]; then
        echo "OK (HTTP $status)"
        ((PASSED++))
    else
        echo "FAILED (Expected $expected_code, got $status)"
        ((FAILED++))
    fi
}

# TEST 1: Register new user
echo "=== AUTHENTICATION TESTS ==="
echo -n "[POST] /api/auth/register - Register new user ... "
register_data="{\"name\":\"Test User\",\"username\":\"$TEST_USER\",\"password\":\"$TEST_PASS\",\"roleId\":1}"
register_response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/register" \
    -H "Content-Type: application/json" \
    -d "$register_data")

reg_status=$(echo "$register_response" | tail -n1)
# Accept both 200 and 201 for registration
if [ "$reg_status" = "200" ] || [ "$reg_status" = "201" ]; then
    echo "OK (HTTP $reg_status)"
    ((PASSED++))
else
    echo "FAILED (HTTP $reg_status)"
    ((FAILED++))
fi

# TEST 2: Login
echo -n "[POST] /api/auth/login - Login with valid credentials ... "
login_data="{\"username\":\"$TEST_USER\",\"password\":\"$TEST_PASS\"}"
login_response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/login" \
    -H "Content-Type: application/json" \
    -d "$login_data")

login_status=$(echo "$login_response" | tail -n1)
login_body=$(echo "$login_response" | sed '$d')

if [ "$login_status" = "200" ]; then
    echo "OK (HTTP $login_status)"
    ((PASSED++))
    # Extract token
    API_TOKEN=$(echo "$login_body" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
    echo "Token obtained: ${API_TOKEN:0:20}..."
else
    echo "FAILED (HTTP $login_status)"
    ((FAILED++))
fi

echo ""
echo "=== BRANDS TESTS ==="

# TEST 3: Create brand
test_endpoint "POST" "/api/brands" "{\"name\":\"BRAND_$RANDOM_ID\"}" "201" "Create brand"

# TEST 4: Get all brands
test_endpoint "GET" "/api/brands" "" "200" "Get all brands"

echo ""
echo "=== MODELS TESTS ==="

# TEST 5: Get all models
test_endpoint "GET" "/api/models" "" "200" "Get all models"

# TEST 6: Search models by name
test_endpoint "GET" "/api/models/search/by-name?name=civic" "" "200" "Search models by name"

echo ""
echo "=== PARTS TESTS ==="

# TEST 7: Get all parts
test_endpoint "GET" "/api/parts" "" "200" "Get all parts"

echo ""
echo "=== CUSTOMERS TESTS ==="

# TEST 8: Create customer
customer_data="{\"name\":\"Customer $RANDOM_ID\",\"phone\":\"555${RANDOM_ID}\",\"rfc\":null}"
test_endpoint "POST" "/api/customers" "$customer_data" "201" "Create customer"

# TEST 9: Get all customers
test_endpoint "GET" "/api/customers" "" "200" "Get all customers"

echo ""
echo "=== TICKETS TESTS ==="

# TEST 10: Get all tickets
test_endpoint "GET" "/api/tickets" "" "200" "Get all tickets"

echo ""
echo "=== SALES TESTS ==="

# TEST 11: Get all sales
test_endpoint "GET" "/api/sales" "" "200" "Get all sales"

echo ""
echo "=================================================="
echo "SUMMARY"
echo "=================================================="
echo "Tests Passed: $PASSED"
echo "Tests Failed: $FAILED"
TOTAL=$((PASSED + FAILED))
if [ $TOTAL -gt 0 ]; then
    SUCCESS_RATE=$((PASSED * 100 / TOTAL))
    echo "Success Rate: ${SUCCESS_RATE}%"
fi
echo "=================================================="
