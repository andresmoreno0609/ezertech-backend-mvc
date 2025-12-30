# Library App 📚

![Java](https://img.shields.io/badge/Java-21-red?logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-DB-336791?logo=postgresql)  

---

## 🎯 Objetivo del Proyecto
El **Library App** es un sistema para la **gestión de biblioteca digital**, que permite:  
- Administrar libros y sus estados de disponibilidad.  
- Gestionar préstamos y usuarios.   
- Garantizar validaciones robustas y un flujo claro de operaciones.  

---

## 🛠️ Retos Técnicos
Durante la implementación se abordaron los siguientes desafíos:  
- Uso de **Java 21** y **Spring Boot 4.0.1** con arquitectura modular.  
- Integración con **PostgreSQL**.  
- Documentación de APIs con **Swagger/OpenAPI 3**.  
- Pruebas unitarias e integración con **Mockito/MockMvc**, asegurando cobertura ≥ 80%. 

---

## 🚀 Tecnologías
- Java 21  
- Spring Boot 4.0.1  
- Spring Web, Data JPA, Validation  
- PostgreSQL (producción/local con Docker)  
- Swagger/OpenAPI 3 (documentación de APIs)  

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
 └─ LibraryApplication.java
```

## ▶️ Ejecución local
1. Compilar y ejecutar con Maven:
   ```bash
   mvn spring-boot:run
   ```
2. Acceder a:
   - API → `http://localhost:8080/api/books`  
   - Swagger UI → `http://localhost:8080/swagger-ui.html`

---

## 📦 Entregables
- Código fuente en este repositorio  
- Colección Postman para pruebas  
- Documentación en este README  
- Cobertura de pruebas ≥ 80%  
