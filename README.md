# TallerArte - Plataforma web para reservar talleres y actividades creativas

## Descripción del Proyecto
Esta aplicación web transaccional está desarrollada en **Java con Spring Boot** bajo la arquitectura MVC.

Su objetivo es resolver la gestión manual de talleres creativos (pintura, fotografía, cerámica, dibujo, cocina, entre otros), que suele manejarse por mensajes, llamadas u hojas de cálculo y provoca errores de cupos, reservas duplicadas y falta de control de asistencia. La plataforma permite la gestión de usuarios, roles, reservas de cupos en sesiones de talleres, registro de asistencia, reportes e internacionalización en tiempo real.

### Roles del sistema
| Rol | Funciones principales |
|---|---|
| **Cliente** | Registrarse, consultar el catálogo y el detalle de los talleres, reservar y cancelar cupos, ver su historial, calificar talleres y descargar comprobantes. |
| **Instructor** | Consultar su panel de talleres y sesiones, ver los inscritos y registrar la asistencia. |
| **Administrador** | Gestionar talleres, sesiones, instructores y usuarios, consultar el panel general y generar reportes de ingresos y ocupación. |

### Módulos
- Autenticación y roles
- Catálogo de talleres
- Sesiones y cupos
- Reservas (módulo transaccional)
- Asistencia
- Reseñas
- Panel de administración y reportes
- Internacionalización (español / inglés)

## Tecnologías Utilizadas
* **Lenguaje Backend:** Java 17+
* **Framework Principal:** Spring Boot 3.x
  * Spring Data JPA (Persistencia)
  * Spring Security (Autenticación y Roles)
  * Spring MVC
* **Motor de Plantillas:** Thymeleaf
* **Diseño Web:** Bootstrap 5, HTML5, CSS3
* **Base de Datos:** MySQL
* **Librería Investigada (No vista en clase):** OpenPDF / iText (Generación de comprobantes y reportes PDF)
* **Prototipo:** Figma
* **Gestión de Versiones:** Git / GitHub

## Funcionalidad de Internacionalización (i18n)
La aplicación cuenta con soporte multilingüe funcional mediante archivos de propiedades ubicados en `src/main/resources`:
* `messages.properties` (Español - Idioma predeterminado)
* `messages_en.properties` (Inglés)

### Configuración de Interceptor y Locale:
El cambio de idioma se gestiona mediante parámetros URL (`?lang=es` / `?lang=en`) manteniendo la persistencia a través de `CookieLocaleResolver`.

## Modelo de Datos
Entidades principales: `Rol`, `Usuario`, `Instructor`, `Categoria`, `Taller`, `Sede`, `Sesion`, `Reserva` (tabla transaccional) y `Resena`.

## Instrucciones de Configuración y Ejecución

### Prerrequisitos
* JDK 17 o superior instalado.
* Maven 3.8+ instalado.
* Motor de Base de Datos MySQL en ejecución.

### Pasos de Instalación
1. **Clonar el repositorio: **
```bash
   git clone https://github.com/[tu-usuario]/[nombre-del-repositorio].git
   cd [nombre-del-repositorio]
```

2. **Crear la base de datos:**
```sql
   CREATE DATABASE tallerarte;
```
   Luego ejecutar el script `[ruta/script.sql]` para crear las tablas y cargar los datos de prueba.

3. **Configurar la conexión** en `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tallerarte
   spring.datasource.username=[usuario]
   spring.datasource.password=[contraseña]
```
   > No subir contraseñas reales al repositorio. Usar un archivo de ejemplo para la configuración local.

4. **Compilar y ejecutar:**
```bash
   mvn clean install
   mvn spring-boot:run
```

5. **Abrir la aplicación** en el navegador: `http://localhost:8080`

### Usuarios de prueba
| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `[correo]` | `[contraseña de prueba]` |
| Instructor | `[correo]` | `[contraseña de prueba]` |
| Cliente | `[correo]` | `[contraseña de prueba]` |

## Estructura del Proyecto
```
src/main/java/[paquete]
├── controller    # Controladores (MVC)
├── service       # Lógica de negocio
├── repository    # Acceso a datos (Spring Data JPA)
└── domain        # Entidades
src/main/resources
├── templates     # Vistas Thymeleaf
├── static        # CSS, JS e imágenes
└── messages*.properties
```

## Equipo de Trabajo
| Integrante | Responsabilidades |
|---|---|
| [Pablo González] | [Historias /Documentacion/Prototipo/Modelo Preliminar] | [Ever Daniel Jiménez Mesen ] | [Historias /Mapa de Navegacion] |
| [Daniel Jimenez Morales] | [Historias / Github] |
| [Audry Maria Porras Ulloa] | [Historias ] |

**Curso:** SC-403 Desarrollo de Aplicaciones Web y Patrones · Universidad Fidélitas





## Flujo de Trabajo en GitHub
* Rama principal: `main` (versión estable).
* Ramas de trabajo por funcionalidad: `feature/autenticacion-roles`, `feature/reservas`, `feature/internacionalizacion`, y por correcciones: `fix/...`.
* Commits descriptivos en la rama correspondiente.
* Todo cambio llega a `main` mediante *pull request* revisado por al menos otro integrante.

## Estado del Proyecto
* **Avance 1 (semana 5):** historias de usuario, prototipo, mapa de navegación, modelo preliminar de datos y repositorio inicial.
* **Avance 2 (semana 9):** implementación funcional del 50% de las historias.
* **Entrega final (semana 15):** aplicación completa, artículo IEEE y defensa.

## Enlaces
* Prototipo (Figma): https://www.figma.com/design/h4Rw1uzr0D6ZoluUIm57gY/Prototipo-DAW?node-id=0-1&t=2690ukzbfQ77zu0I-1
* Video de presentación: En los apartados de entregas
