# Reserva de Canchas

API REST desarrollada con Spring Boot para gestionar clientes, canchas y reservas deportivas.

## Descripción general

La aplicación permite administrar:

- Clientes
- Canchas deportivas
- Reservas por fecha y horario
- Confirmación y cancelación de reservas
- Validación de conflictos de horario
- Cálculo automático del total según precio por hora

La lógica principal está implementada en el módulo de servicios y usa JPA para persistir datos en MySQL.

## Stack tecnológico

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- MySQL Connector/J
- Lombok
- Springdoc OpenAPI / Swagger UI

## Estructura del proyecto

```text
reserva-cancha/
├── application.properties          # Configuración principal de la app
├── pom.xml                         # Dependencias y configuración Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── py/edu/snpp/reserva_cancha/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ReservaCanchaApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/py/edu/snpp/reserva_cancha/
├── mvnw
├── mvnw.cmd
└── target/
```

## Entidades principales

### Cliente

Representa a una persona que puede reservar una cancha.

Campos principales:
- id
- nombre
- apellido
- cedula
- telefono
- email

### Cancha

Representa una instalación deportiva disponible para reservar.

Tipos soportados:
- FUTBOL_5
- FUTBOL_7
- TENIS
- PADEL
- BASQUET

Además incluye:
- nombre
- precioHora
- activa

### Reserva

Representa una reserva de una cancha por un cliente en una fecha y horario específico.

Campos principales:
- cliente
- cancha
- fecha
- horaInicio
- horaFin
- estado
- total
- observacion

Estados soportados:
- PENDIENTE
- CONFIRMADA
- CANCELADA

## Reglas de negocio

La API valida y protege varios escenarios importantes:

- No se puede reservar una cancha inactiva
- La hora de fin debe ser posterior a la hora de inicio
- No se permiten horarios superpuestos en la misma cancha el mismo día
- No se puede confirmar una reserva que no esté en estado pendiente
- No se puede cancelar una reserva ya cancelada
- No se puede eliminar un cliente o cancha si tienen reservas asociadas
- La cédula del cliente debe ser única
- El precio total se calcula en base a la duración y el precio por hora

## Endpoints principales

### Clientes

- GET /api/v1/clientes
- GET /api/v1/clientes/{id}
- POST /api/v1/clientes
- PUT /api/v1/clientes/{id}
- DELETE /api/v1/clientes/{id}

### Canchas

- GET /api/v1/canchas
- GET /api/v1/canchas?tipo={TIPO}
- GET /api/v1/canchas/{id}
- POST /api/v1/canchas
- PUT /api/v1/canchas/{id}
- DELETE /api/v1/canchas/{id}

### Reservas

- GET /api/v1/reservas
- GET /api/v1/reservas/{id}
- GET /api/v1/reservas/cliente/{clienteId}
- GET /api/v1/reservas/cancha/{canchaId}?fecha=YYYY-MM-DD
- POST /api/v1/reservas
- PATCH /api/v1/reservas/{id}/confirmar
- PATCH /api/v1/reservas/{id}/cancelar

## Documentación Swagger

La documentación OpenAPI está habilitada con Springdoc.

- Swagger UI: http://localhost:8080/parcial/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/parcial/v3/api-docs

## Configuración

La configuración principal de la aplicación se encuentra en [application.properties](application.properties).

Ajusta los valores de base de datos según tu entorno local:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reserva_canchas?createDatabaseIfNotExist=true&serverTimezone=America/Asuncion&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
server.port=8080
server.servlet.context-path=/parcial
```

La base de datos esperada es MySQL y el proyecto se configura para crear la estructura al iniciar con JPA.

## Datos iniciales

El archivo [src/main/resources/data.sql](src/main/resources/data.sql) incluye datos semilla para:

- clientes
- canchas
- reservas de ejemplo

## Cómo ejecutar

Requisitos:

- Java 21
- MySQL corriendo localmente
- Base de datos `reserva_canchas` accesible

### Opción 1: usando Maven

```bash
./mvnw.cmd spring-boot:run
```

### Opción 2: compilar y ejecutar el JAR

```bash
./mvnw.cmd clean package
java -jar target/reserva-cancha-0.0.1-SNAPSHOT.jar
```

## Verificación

Se ejecutó la suite de pruebas con Maven y el proyecto respondió correctamente con salida final de `EXIT:True`.

## Observaciones

- La aplicación usa `spring.jpa.hibernate.ddl-auto=create`, por lo que la base de datos se recrea al iniciar.
- El valor `server.servlet.context-path=/parcial` hace que los endpoints queden bajo el prefijo `/parcial`.
- El proyecto está pensado como una API backend para una app de reservas deportivas, con posibilidad de extenderse por ejemplo con autenticación, pagos o disponibilidad por horarios.

## Licencia

Proyecto académico / de práctica sin licencia específica definida.
