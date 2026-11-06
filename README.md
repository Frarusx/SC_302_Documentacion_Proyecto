# TallerArte - Plataforma web para reservar talleres y actividades creativas

Aplicación web transaccional en **Java 17 + Spring Boot 3 (MVC)** para gestionar talleres creativos, sesiones y cupos,
reservas, asistencia, reseñas, reportes y roles (Cliente, Instructor, Administrador), con i18n español/inglés.

## Estado del avance (Avance 2)
Ver `Backlog_TallerArte_Avance2.xlsx` (columna Estado + enlace al PR de cada HU).

| HU | Título | Estado |
|---|---|---|
| HU-01 | Registro de cuenta | ✅|
| HU-02 | Inicio de sesión | ✅ |
| HU-03 | Catálogo de talleres | ✅ |
| HU-04 | Detalle del taller |✅ |
| HU-05 | Reservar un cupo | ✅ |
| HU-09 | CRUD de talleres | ✅|
| HU-10 | Sesiones | ✅ |
| HU-11 | Instructores y usuarios |✅ |
| HU-12 | Mis reservas | ✅ |
| HU-13 | Cancelación de reservas | ✅ |
| HU-17 | Textos desde archivos de propiedades | ✅ |
| HU-21 | Gestión de reservas (Administrador) |✅ |

> Cambiar ☐ por ✅ solo cuando la HU cumple todos sus criterios de aceptación.

## Tecnologías
Java 17, Spring Boot 3.x (MVC, Data JPA/Hibernate, Security), Thymeleaf, Bootstrap 5, MySQL, Lombok,
OpenPDF (librería investigada), Figma, Git/GitHub.

## Requisitos de ejecución
- JDK 17+, Maven 3.8+, MySQL en ejecución.

## Pasos
1. `git clone <url-del-repositorio>` y `cd` a la carpeta.
2. Crear la base: `CREATE DATABASE tallerarte;`
3. Copiar `application.properties.example` a `application.properties` y poner usuario/contraseña de MySQL (no subir credenciales reales).
4. Con `spring.jpa.hibernate.ddl-auto=update` Hibernate crea las tablas; cargar datos de prueba con `datos-prueba-reservas.sql`.
5. `mvn clean install` y `mvn spring-boot:run`
6. Abrir `http://localhost:8080`

## Usuarios de prueba
| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | admin@tallerarte.com | (definir) |
| Cliente | cliente@tallerarte.com | (definir) |

## Estructura
```
src/main/java/com/tallerarte
├── controller   # Controladores MVC
├── service      # Lógica de negocio (@Transactional)
├── repository   # Spring Data JPA
├── domain       # Entidades
└── config       # Seguridad
src/main/resources
├── templates    # Vistas Thymeleaf + Bootstrap
└── messages*.properties
```

## Flujo de trabajo en GitHub
- `main`: versión estable. Ramas `feature/...` por funcionalidad y `fix/...` por correcciones.
- Commits descriptivos; todo llega a `main` por pull request revisado por otro integrante.

## Equipo
Pablo González Garita · Ever Daniel Jiménez Mesen · Daniel Jiménez Morales · Audry María Porras Ulloa
Curso SC-403 Desarrollo de Aplicaciones Web y Patrones · Universidad Fidélitas

## Enlaces
- Prototipo (Figma): https://www.figma.com/design/h4Rw1uzr0D6ZoluUIm57gY/Prototipo-DAW?node-id=0-1&t=2690ukzbfQ77zu0I-1
- Video de demostración: [enlace]
