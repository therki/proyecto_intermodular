# TODO App - Proyecto Intermodular DAW

Este es el repositorio de mi proyecto final intermodular para el **CFGS de Desarrollo de Aplicaciones Web (Curso 2025-2026)**.

---

## Tecnologías que he usado

Las tecnologías utilizadas para este preoyecto son:

- **Backend (Servidor):** Java 17 con **Spring Boot 3**. Uso **Spring Data JPA** (Hibernate) para conectar con la base de datos y **Spring Security 6** con cifrado **BCrypt** para proteger las contraseñas y controlar los accesos por rol.
- **Base de Datos:** MariaDB / MySQL.
- **Frontend (Cliente):** Las vistas están montadas con **Thymeleaf** (el motor de plantillas de Spring) y maquetadas con **Bootstrap 5** para que sea responsiva. Para hacer que los formularios de creación y edición se abran en ventanas modales sin recargar la página completa, he usado JavaScript nativo (**Fetch API**).
- **Despliegue e Infraestructura:** He contenerizado todo el entorno con **Docker** y **Docker Compose** (un contenedor para la app y otro para la base de datos). Para producción, lo tengo desplegado en la nube con **Railway**.

---

## Estructura del Código (Backend)

El código en la carpeta `src/main/java/com/todoapp/` está organizado siguiendo el patrón típico por capas:

- `config/`: Configuración de la seguridad (Spring Security) y restricciones de rutas.
- `controller/`: Los controladores web que manejan las rutas (Thymeleaf) y los endpoints para las llamadas asíncronas de la app.
- `dto/`: Los objetos que uso para pasar datos limpios entre capas sin exponer directamente las entidades.
- `error/`: Los objetos que uso para pasar datos limpios entre capas sin exponer directamente las entidades.
- `model/`: Las clases del modelo de datos mapeadas con la base de datos (User, Task, Tag, Category).
- `repos/`: Las interfaces que heredan de JpaRepository para hacer las consultas a la base de datos.
- `security/`: Las interfaces que heredan de JpaRepository para hacer las consultas a la base de datos.
- `service/`: La capa intermedia con toda la lógica de negocio del proyecto.

---

## Cómo ejecutar el proyecto en local

Como el proyecto está completamente dockerizado, no necesitas tener Java ni MariaDB instalados en tu ordenador. Solo necesitas tener instalado **Docker Desktop**.

**Pasos:**

1. Clona este repositorio en tu ordenador:
   ```bash
   git clone https://github.com/therki/proyecto_intermodular
   cd tu-repositorio-todoapp
   ```
2. Levanta los contenedores

   `docker compose up --build -d`

3. Abre el navegador y entra en:
   - Aplicación web: `http://localhost:8080/`
   - Documentación Swagger: `http://localhost:8080/swagger-ui/index.html`

4. Accede con los usuarios de prueba (usuario - contraseña):
   - **Administrador**. admin - 12345
   - **Gestor**. gestor - 12345
   - **Usuario**. usuario - 12345
