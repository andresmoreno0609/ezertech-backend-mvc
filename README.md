# Library App 📚

![Java](https://img.shields.io/badge/Java-17-red?logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?logo=springboot)  
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)  
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-DB-336791?logo=postgresql)  
![H2](https://img.shields.io/badge/H2-Database-lightgrey)  
![Coverage](https://img.shields.io/badge/Coverage-80%25-green?logo=codecov)  

---

## 🎯 Objetivo del Proyecto
El **Library App** es un sistema para la **gestión de biblioteca digital**, que permite:  
- Administrar libros y sus estados de disponibilidad.  
- Gestionar préstamos y usuarios.  
- Ofrecer una interfaz localizada en español.  
- Garantizar validaciones robustas y un flujo claro de operaciones.  

---

## 🛠️ Retos Técnicos
Durante la implementación se abordaron los siguientes desafíos:  
- Uso de **Java 17** y **Spring Boot 3.5.5** con arquitectura modular.  
- Integración con **PostgreSQL** para producción y **H2** para pruebas unitarias.  
- Configuración de **Docker & Docker Compose** para despliegue local y en la nube.  
- Documentación de APIs con **Swagger/OpenAPI 3**.  
- Migraciones de base de datos con **Flyway**.  
- Pruebas unitarias e integración con **Mockito/MockMvc**, asegurando cobertura ≥ 80% con **JaCoCo**.  
- Soporte de localización en español para la interfaz y mensajes de validación.  

---

## 🚀 Tecnologías
- Java 17  
- Spring Boot 3.5.5  
- Spring Web, Data JPA, Validation  
- PostgreSQL (producción/local con Docker)  
- H2 (pruebas unitarias y perfil demo)  
- Docker & Docker Compose  
- Swagger/OpenAPI 3 (documentación de APIs)  
- Flyway (migraciones de BD)  
- JaCoCo (cobertura de pruebas)  

---

## 📂 Estructura del Proyecto
El proyecto sigue una arquitectura modular y mantenible:

```
src/main/java/com/ezertech/library
 ├─ controller/           # Controladores REST (Books, Loans)
 ├─ dto/                  # Objetos de transferencia (Request / Response)
 ├─ entity/               # Entidades JPA (Book, Loan)
 ├─ enums/                # Enumeraciones (BookStatus, LoanStatus)
 ├─ exceptions/           # Manejo de errores y validaciones
 ├─ repository/           # Repositorios JPA
 ├─ service/              # Lógica de negocio (BookService, LoanService)
 ├─ config/               # Configuración (Swagger, DB, Profiles)
 └─ LibraryApplication.java
```

---

## ⚙️ Configuración
Parámetros clave en `application.yml` / perfiles:
- `spring.datasource.url` → conexión a PostgreSQL/H2  
- `spring.jpa.hibernate.ddl-auto` → estrategia de creación/actualización de tablas  
- `spring.profiles.active` → perfil activo (`dev`, `test`, `prod`)  
- `swagger.enabled` → habilitar documentación de APIs  

---

## ▶️ Ejecución local
1. Compilar y ejecutar con Maven:
   ```bash
   mvn spring-boot:run
   ```
2. Acceder a:
   - API → `http://localhost:8080/api/books`  
   - Swagger UI → `http://localhost:8080/swagger-ui.html`

---

### Con Docker Compose
```bash
docker-compose up --build
```

Servicios:
- **API**: http://localhost:8080  
- **Swagger**: http://localhost:8080/swagger-ui.html  
- **DB (Postgres)**: localhost:5432  

---

## 🌐 Producción (ejemplo con Cloud Run / Docker)
El servicio puede desplegarse en **Google Cloud Run** o cualquier infraestructura Dockerizada:

- **Base URL**:  
  `https://library-app-service.run.app`

- **Endpoints principales**:
  - `GET /api/books` → listar libros  
  - `POST /api/loans` → crear préstamo  
  - Swagger UI →  
    [https://library-app-service.run.app/swagger-ui.html](https://library-app-service.run.app/swagger-ui.html)  
  - OpenAPI docs →  
    [https://library-app-service.run.app/v3/api-docs](https://library-app-service.run.app/v3/api-docs)  

---

## ✅ Pruebas
Ejecutar pruebas unitarias con cobertura (JaCoCo ≥ 80%):
```bash
mvn clean verify
```

Reporte de cobertura:  
`target/site/jacoco/index.html`

---

## 🤖 Uso de AI
Durante el desarrollo de este proyecto se utilizó inteligencia artificial como apoyo para:  
- Depuración de validaciones y controladores REST  
- Configuración de pruebas unitarias con Mockito y MockMvc  
- Traducción y localización de interfaces al español  
- Documentación técnica y estructuración del README  

---

## 📦 Entregables
- Código fuente en este repositorio  
- API desplegada en Docker/Cloud  
- Colección Postman para pruebas  
- Diagramas de arquitectura  
- Documentación en este README  
- Cobertura de pruebas ≥ 80%  
