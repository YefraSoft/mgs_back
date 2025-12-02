# Script de Testing para API REST - PowerShell
# Sin caracteres especiales

$BaseURL = "http://localhost:8080"
$TotalTests = 0
$PassedTests = 0
$FailedTests = 0

function Write-Header {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Blue
    Write-Host "$($args[0])" -ForegroundColor Blue
    Write-Host "========================================" -ForegroundColor Blue
    Write-Host ""
}

function Write-Test {
    Write-Host "[TEST] $($args[0])" -ForegroundColor Yellow
}

function Write-Success {
    Write-Host "[OK] $($args[0])" -ForegroundColor Green
    $script:PassedTests++
    $script:TotalTests++
}

function Write-Failure {
    Write-Host "[FAIL] $($args[0])" -ForegroundColor Red
    $script:FailedTests++
    $script:TotalTests++
}

function Invoke-APIRequest {
    param([string]$Method, [string]$Endpoint, [string]$Body = $null)
    
    $url = "$BaseURL$Endpoint"
    $headers = @{ "Content-Type" = "application/json" }
    
    # Agregar token si está disponible
    if ($script:Token) {
        $headers["Authorization"] = "Bearer $($script:Token)"
    }
    
    try {
        $params = @{
            Uri = $url
            Method = $Method
            Headers = $headers
            TimeoutSec = 10
        }
        if ($Body) { $params["Body"] = $Body }
        
        $response = Invoke-WebRequest @params -ErrorAction Stop
        return @{ StatusCode = $response.StatusCode; Content = $response.Content; Success = $true }
    }
    catch {
        $statusCode = 0
        if ($_.Exception.Response) { $statusCode = [int]$_.Exception.Response.StatusCode }
        return @{ StatusCode = $statusCode; Content = $_.Exception.Message; Success = $false }
    }
}

Write-Host ""
Write-Host "API Testing Script - PowerShell" -ForegroundColor Blue
Write-Host "URL: $BaseURL" -ForegroundColor Blue
Write-Host ""

Write-Host "Verificando conexion..." -ForegroundColor Yellow
$response = Invoke-APIRequest -Method GET -Endpoint "/api/brands"
if ($response.StatusCode -eq 403) {
    Write-Host "OK - Servidor responde (requiere autenticacion)" -ForegroundColor Green
} else {
    Write-Host "Respuesta: Status $($response.StatusCode)" -ForegroundColor Yellow
}

# Tests de Autenticacion
Write-Header "AUTHENTICATION"

Write-Test "POST /api/auth/register - Registrar nuevo usuario"
$randomId = Get-Random -Minimum 100000 -Maximum 999999
$body = "{`"username`":`"testuser$randomId`",`"password`":`"TestPass123!`",`"name`":`"Test User`",`"roleId`":1}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/auth/register" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/auth/register"
} else {
    Write-Failure "POST /api/auth/register - Status: $($response.StatusCode)"
}

Write-Test "POST /api/auth/login"
$body = "{`"username`":`"testuser$randomId`",`"password`":`"TestPass123!`"}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/auth/login" -Body $body
if ($response.StatusCode -eq 200) {
    Write-Success "POST /api/auth/login"
    try {
        $data = $response.Content | ConvertFrom-Json -ErrorAction SilentlyContinue
        if ($data.token) {
            $script:Token = $data.token
        }
    }
    catch {}
} else {
    Write-Failure "POST /api/auth/login - Status: $($response.StatusCode)"
}

# Tests de Brands
Write-Header "BRANDS"
Write-Test "GET /api/brands"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/brands"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/brands"
} else {
    Write-Failure "GET /api/brands - Status: $($response.StatusCode)"
}

$randomId = Get-Random -Minimum 1000 -Maximum 9999
Write-Test "POST /api/brands"
$body = "{`"name`":`"BRAND_$randomId`"}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/brands" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/brands"
} else {
    Write-Failure "POST /api/brands - Status: $($response.StatusCode)"
}

# Tests de Models
Write-Header "MODELS"
Write-Test "GET /api/models"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/models"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/models"
} else {
    Write-Failure "GET /api/models - Status: $($response.StatusCode)"
}

$randomId = Get-Random -Minimum 1000 -Maximum 9999
Write-Test "POST /api/models"
$body = "{`"name`":`"Model_$randomId`",`"year`":2024,`"transmission`":`"AUTOMATIC`",`"engine`":`"V6`",`"vehicleClass`":`"Sedan`",`"brandId`":1}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/models" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/models"
} else {
    Write-Failure "POST /api/models - Status: $($response.StatusCode)"
}

# Tests de Parts
Write-Header "PARTS"
Write-Test "GET /api/parts"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/parts"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/parts"
} else {
    Write-Failure "GET /api/parts - Status: $($response.StatusCode)"
}

$randomId = Get-Random -Minimum 100000 -Maximum 999999
Write-Test "POST /api/parts"
$body = "{`"code`":`"PART_$randomId`",`"name`":`"Test Part`",`"side`":`"UNIDIRECTIONAL`",`"categoryType`":`"ELECTRICAL`",`"color`":`"Silver`",`"price`":1500.0,`"quantity`":5}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/parts" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/parts"
} else {
    Write-Failure "POST /api/parts - Status: $($response.StatusCode)"
}

# Tests de Customers
Write-Header "CUSTOMERS"
Write-Test "GET /api/customers"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/customers"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/customers"
} else {
    Write-Failure "GET /api/customers - Status: $($response.StatusCode)"
}

$randomPhone = Get-Random -Minimum 1000000000 -Maximum 9999999999
Write-Test "POST /api/customers"
$body = "{`"name`":`"Test Customer`",`"phone`":`"$randomPhone`"}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/customers" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/customers"
} else {
    Write-Failure "POST /api/customers - Status: $($response.StatusCode)"
}

# Tests de Tickets
Write-Header "TICKETS"
Write-Test "GET /api/tickets"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/tickets"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/tickets"
} else {
    Write-Failure "GET /api/tickets - Status: $($response.StatusCode)"
}

Write-Test "POST /api/tickets"
$body = "{`"userId`":1,`"paymentMethod`":`"CASH`",`"items`":[{`"partId`":1,`"quantity`":2,`"price`":1500.0}]}"
$response = Invoke-APIRequest -Method POST -Endpoint "/api/tickets" -Body $body
if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) {
    Write-Success "POST /api/tickets"
} else {
    Write-Failure "POST /api/tickets - Status: $($response.StatusCode)"
}

# Tests de Sales
Write-Header "SALES"
Write-Test "GET /api/sales"
$response = Invoke-APIRequest -Method GET -Endpoint "/api/sales"
if ($response.StatusCode -eq 200) {
    Write-Success "GET /api/sales"
} else {
    Write-Failure "GET /api/sales - Status: $($response.StatusCode)"
}

# Resumen
Write-Header "RESUMEN FINAL"
Write-Host "Total de Tests:  $TotalTests" -ForegroundColor Blue
Write-Host "Tests Exitosos:  $PassedTests" -ForegroundColor Green
Write-Host "Tests Fallidos:  $FailedTests" -ForegroundColor Red

if ($TotalTests -gt 0) {
    $successRate = [math]::Round(($PassedTests / $TotalTests) * 100, 2)
    Write-Host "Tasa de Exito:   $successRate%" -ForegroundColor Blue
}

Write-Host ""
