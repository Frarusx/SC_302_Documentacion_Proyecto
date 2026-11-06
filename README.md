# TallerArte - Plataforma web para reservar talleres y actividades creativas

Aplicación web transaccional en **Java 21 + Spring Boot 4.1 (MVC)** para gestionar talleres creativos y sus reservas,
con roles (Cliente, Instructor, Administrador) e internacionalización español/inglés.

## Estado del avance (Avance 2)

| HU | Título | Estado | Nota |
|---|---|---|---|
| HU-01 | Registro de cuenta | ✅ | Validaciones por campo, contraseña cifrada, rol Cliente por defecto |
| HU-02 | Inicio de sesión | ✅ | Redirección según el rol y menú por rol |
| HU-03 | Catálogo de talleres | 🟡 Parcial | Con filtro por categoría; falta la imagen del taller |
| HU-04 | Detalle del taller | 🟡 Parcial | Muestra un taller con una sola fecha y cupo; falta el manejo por sesiones |
| HU-05 | Reservar un cupo | ✅ | Descuenta el cupo y evita reservas duplicadas |
| HU-09 | CRUD de talleres | 🟡 Parcial | Lo gestiona el instructor sobre sus talleres; el administrador gestiona las categorías |
| HU-10 | Sesiones | ⏳ Pendiente | Hoy la fecha y el cupo van dentro del taller |
| HU-11 | Instructores y usuarios | ⏳ Pendiente | |
| HU-12 | Mis reservas | ✅ | Pestañas Próximas, Asistidas y Canceladas |
| HU-13 | Cancelación de reservas | ✅ | Con confirmación; devuelve el cupo al taller |
| HU-17 | Textos desde archivos de propiedades | ✅ | `messages.properties` y `messages_en.properties` |
| HU-21 | Gestión de reservas (Administrador) | ✅ | Filtros, marcar asistida y cancelar |

✅ = cumple los criterios de aceptación · 🟡 = implementada con diferencias respecto al Avance 1 · ⏳ = pendiente

## Tecnologías
- Java 21 y Spring Boot 4.1.1 (Spring MVC, Spring Data JPA/Hibernate)
- Spring Security con contraseñas cifradas (BCrypt)
- Thymeleaf y Bootstrap 5 (por WebJars)
- MySQL y Lombok
- Prototipo en Figma, Git y GitHub
- Librería investigada (prevista para el siguiente avance): OpenPDF, para generar comprobantes PDF

## Requisitos de ejecución
- JDK 21
- Maven 3.9+ (o Apache NetBeans, que ya lo incluye)
- MySQL en ejecución, y MySQL Workbench para correr los scripts
- Internet la primera vez, para descargar las dependencias

## Pasos para ejecutarlo
1. Clonar el repositorio: `git clone <url-del-repositorio>` y entrar a la carpeta.
2. Abrir MySQL Workbench con un usuario administrador (por ejemplo `root`) y ejecutar, **en este orden**, los scripts de la carpeta `sql/`:
   1. `crea_tallerarte.sql`: crea la base `tallerarte`, el usuario de MySQL, las tablas `rol` y `usuario`, los 3 roles y los usuarios de prueba.
   2. `talleres_tallerarte.sql`: crea las tablas `categoria` y `taller` con datos de ejemplo.
   3. `datos-prueba-reservas.sql`: datos de prueba del módulo de reservas.
3. Revisar `src/main/resources/application.properties`. Estos son los valores que usa el proyecto (la contraseña es la del script, cámbiala en ambos si la modificas):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tallerarte?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=usuario_tallerarte
   spring.datasource.password=CambiarEstaClave
   ```
4. Las tablas las crean los scripts SQL, no Hibernate.
5. Ejecutar con `mvn clean install` y `mvn spring-boot:run` (o **Clean and Build** y **Run** en NetBeans).
6. Abrir `http://localhost:8080`.

## Usuarios de prueba
| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | admin@tallerarte.cr | Admin2026 |
| Instructor | instructor@tallerarte.cr | Instructor2026 |
| Cliente | cliente@tallerarte.cr | Cliente2026 |

Son solo credenciales de prueba. Las contraseñas se guardan cifradas en la base de datos.

## Estructura del proyecto
```
src/main/java/com/tallerarte
├── TallerArteApplication.java   # Clase principal
├── ConfiguracionWeb.java        # Idioma (es/en) y archivos de mensajes
├── ConfiguracionSeguridad.java  # Rutas públicas, roles, login y cifrado
├── controller                   # Controladores MVC
├── service                      # Lógica de negocio (@Transactional)
├── repository                   # Spring Data JPA
└── domain                       # Entidades
src/main/resources
├── templates                    # Vistas Thymeleaf, una carpeta por módulo (general contiene la plantilla)
├── static                       # CSS
├── messages.properties          # Español (idioma por defecto)
└── messages_en.properties       # Inglés
sql                              # Scripts de la base de datos
```

## Roles y acceso
| Rol | Qué puede hacer |
|---|---|
| Visitante | Ver el catálogo y el detalle de los talleres, registrarse e iniciar sesión |
| Cliente | Reservar, cancelar y ver sus reservas |
| Instructor | Crear, editar y eliminar sus propios talleres |
| Administrador | Gestionar las categorías y las reservas de todos los clientes |

El acceso está restringido por rol: si un usuario abre una ruta que no le corresponde, el sistema le niega el acceso (error 403).

## Flujo de trabajo en GitHub
- `main`: versión estable. Ramas `feature/...` por funcionalidad y `fix/...` por correcciones.
- Commits descriptivos; los cambios llegan a `main` por pull request.
- **Nota:** durante el desarrollo hubo problemas de permisos en el repositorio y no todos los integrantes pudieron subir sus cambios. Por eso Pablo integró y subió el código de los tres módulos.

## Equipo (Grupo #4)
| Integrante | Módulo |
|---|---|
| Pablo González Garita (A) | Proyecto base, base de datos, registro, login, roles, plantilla e idioma, e integración |
| Audry María Porras Ulloa (B) | Talleres y categorías: catálogo, detalle, vista del instructor |
| Daniel Jiménez Morales (C) | Reservas: reservar, cancelar, mis reservas y gestión del administrador |

Curso SC-403 Desarrollo de Aplicaciones Web y Patrones · Universidad Fidélitas

## Enlaces
- Prototipo (Figma): https://www.figma.com/design/h4Rw1uzr0D6ZoluUIm57gY/Prototipo-DAW?node-id=0-1&t=2690ukzbfQ77zu0I-1
- Video de demostración: [enlace]
