# Multipartes Garcia API

API REST desarrollada en Kotlin con Spring Boot para el sistema de gestión de inventario de piezas de desguace de vehículos.

## 📋 Descripción

Esta API proporciona servicios backend para la gestión completa de un negocio de desguace automotriz, incluyendo inventario de piezas, ventas, clientes y facturación. Está diseñada para ser consumida por aplicaciones MAUI y páginas web.

## 🏗️ Arquitectura

### Stack Tecnológico
- **Lenguaje:** Kotlin 1.9.25
- **Framework:** Spring Boot 3.4.5
- **Base de Datos:** MySQL 8
- **Seguridad:** Spring Security + JWT
- **ORM:** JPA/Hibernate
- **Java:** Versión 21

### Características Principales
- 🔐 Autenticación JWT con roles
- 📦 Gestión completa de inventario
- 💰 Sistema de ventas y tickets
- 👥 Gestión de clientes
- 🧾 Facturación electrónica
- 📊 Auditoría y logs
- 🌐 API RESTful

## 🗄️ Base de Datos

El sistema utiliza una base de datos MySQL con 12 tablas principales:

- **Marcas y Modelos:** Gestión de vehículos por marca y modelo
- **Piezas:** Inventario con categorías, precios y stock
- **Usuarios:** Sistema de roles (USER, ADMIN, SELLER, ACCOUNTANT)
- **Ventas:** Tickets y detalles de ventas
- **Clientes:** Información de clientes y datos fiscales
- **Facturación:** Emisión de facturas electrónicas
- **Auditoría:** Logs de acceso y eventos

## 🚀 Instalación y Configuración

### Prerrequisitos
- Java 21+
- MySQL 8+
- Maven 3.6+

### Configuración

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd mgs_back
```

2. **Configurar la base de datos**
```bash
# Crear la base de datos MySQL
mysql -u root -p
CREATE DATABASE mpgv1;
```

3. **Configurar variables de entorno**
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mpgv1
    username: your_username
    password: your_password
```

4. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

## 📚 Endpoints Principales

### Autenticación
```
POST /api/auth/login     - Iniciar sesión
POST /api/auth/register  - Registrar usuario
```

### Piezas
```
GET    /api/parts        - Listar todas las piezas
GET    /api/parts/{id}   - Obtener pieza por ID
POST   /api/parts        - Crear nueva pieza
PUT    /api/parts/{id}   - Actualizar pieza
DELETE /api/parts/{id}   - Eliminar pieza
```

### Ventas
```
GET    /api/sales                    - Listar todas las ventas
GET    /api/sales/{id}              - Obtener venta por ID
POST   /api/sales                   - Crear nueva venta
PUT    /api/sales/{id}              - Actualizar venta
DELETE /api/sales/{id}              - Eliminar venta
GET    /api/sales/search/by-date    - Buscar ventas por fecha
```

## 🔒 Seguridad

- **JWT Tokens:** Autenticación stateless
- **BCrypt:** Hash seguro de contraseñas
- **CORS:** Configurado para aplicaciones web
- **Roles:** Sistema de permisos granular
- **Auditoría:** Logs de acceso y eventos

## 🧪 Testing

```bash
# Ejecutar tests unitarios
./mvnw test

# Ejecutar tests de integración
./mvnw verify
```

## 📦 Build y Deploy

```bash
# Compilar proyecto
./mvnw clean compile

# Crear JAR ejecutable
./mvnw clean package

# Ejecutar JAR
java -jar target/dev-0.0.1-SNAPSHOT.jar
```

## 🏗️ Estructura del Proyecto

```
src/main/kotlin/api/multipartes/dev/
├── config/              # Configuración (Security, CORS, JWT)
├── dtos/                # Data Transfer Objects
├── endPoints/           # Controladores REST
│   ├── auth/            # Autenticación
│   ├── parts/           # Gestión de piezas
│   └── sales/           # Gestión de ventas
├── enums/               # Enumeraciones
├── models/              # Entidades JPA
├── role/                # Repositorio de roles
└── user/                # Repositorio de usuarios
```

## 🔧 Configuración de Perfiles

- **dev:** Desarrollo local
- **prod:** Producción

## 📝 Documentación de la API

La documentación completa de la API está disponible en el archivo `AGENTS.md` que incluye:
- Esquema completo de la base de datos
- Descripción detallada de cada tabla
- Relaciones entre entidades
- Índices y optimizaciones

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Soporte

Para soporte técnico o consultas sobre el proyecto, contactar al equipo de desarrollo.

---

**Desarrollado con ❤️ para Multipartes Garcia**
