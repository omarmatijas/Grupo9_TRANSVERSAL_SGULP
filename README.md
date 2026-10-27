# SGULP – Sistema de Gestión para la Universidad de La Punta

**Proyecto Transversal – Laboratorio 1 – Grupo 9**

Sistema de escritorio en Java para registrar alumnos, materias e inscripciones
(cursadas) de la Universidad de La Punta, con persistencia en MariaDB mediante JDBC.

---

## 👥 Integrantes – 1ra Entrega

| Apellido y Nombre             |
|-------------------------------|
| Magallanes, Franco            |
| Auriol, Jessica               |
| Martin Asis, Gonzalo Exequiel |
| Matijas, Omar                 |

## 👥 Integrantes – 2da Entrega

| Apellido y Nombre             |
|-------------------------------|
| Magallanes, Franco            |
| Auriol, Jessica               |
| Martin Asis, Gonzalo Exequiel |
| Matijas, Omar                 |

---

## 📦 Contenido de la 1ra Entrega

| Requisito                     | Ubicación                             |
|-------------------------------|---------------------------------------|
| Script de la base de datos    | `GP9_universidad.sql`                 |
| Captura de la vista diseñador | `GP9_universidad_diseñador.png`       |
| Clase de dominio              | `modelo/Alumno.java`                  |
| Clase de conexión a MariaDB   | `persistencia/miConexion.java`        |
| Acceso a datos                | `persistencia/AlumnoData.java`        |
| Main con test de consola      | `vista/Grupo9_TRANSVERSAL_SGULP.java` |

## 📦 Contenido de la 2da Entrega

| Requisito                                        | Ubicación                                                   |
|--------------------------------------------------|-------------------------------------------------------------|
| Clases de entidad                                | `modelo/Alumno.java`, `modelo/Materia.java`, `modelo/Cursada.java` |
| Acceso a datos de alumnos y materias             | `persistencia/AlumnoData.java`, `persistencia/MateriaData.java` |
| Ventana principal (JFrame + DesktopPane + JMenu) | `vista/MenuPrincipal.java`                                  |
| Vista de alumnos (ABMC)                          | `vista/AlumnoVista.java`                                    |
| Vista de materias (ABMC)                         | `vista/MateriaVista.java`                                   |
| Datos de prueba                                  | `GP9_seeder.sql`                                            |

---

## 🗄️ Base de datos

Base: **`grupo9_universidad`** (motor InnoDB)

```
alumno (1) ──< cursada >── (1) materia
```

| Tabla     | Descripción                                                             |
|-----------|-------------------------------------------------------------------------|
| `alumno`  | Datos del alumno: DNI, nombre, fecha de nacimiento y estado activo      |
| `materia` | Materias que se dictan y su estado                                      |
| `cursada` | Inscripción de un alumno a una materia en un año, con nota y asistencia |

`cursada` tiene claves foráneas hacia `alumno` y `materia`.

![Vista diseñador de la base de datos](GP9_universidad_diseñador.png)

---

## ▶️ Cómo ejecutar

### Requisitos

- JDK 17 o superior
- Apache NetBeans (proyecto Maven)
- XAMPP con MariaDB/MySQL iniciado

### Pasos

1. Iniciar **MySQL** desde el panel de XAMPP.
2. En phpMyAdmin → **Importar** → seleccionar `GP9_universidad.sql` (crea la base y las tablas).
3. (Opcional) Importar `GP9_seeder.sql` para cargar alumnos, materias y cursadas de prueba.
4. Abrir el proyecto en NetBeans.
5. Si MariaDB usa un puerto distinto a `3306` (por ejemplo `3307`), cambiar la
   constante `PORT` en `vista/MenuPrincipal.java` y en `vista/Grupo9_TRANSVERSAL_SGULP.java`.
6. Ejecutar:
   - **Aplicación con ventanas:** clic derecho sobre `vista/MenuPrincipal.java` → **Run File** (`Shift+F6`).
   - **Test de consola:** clic derecho sobre `vista/Grupo9_TRANSVERSAL_SGULP.java` → **Run File** (`Shift+F6`).

### Resultado esperado

**Aplicación con ventanas:** se abre la ventana principal *SGULP - Universidad de la Punta*.
Desde el menú se accede a:

- **Alumnos → Gestión de Alumnos:** tabla con todos los alumnos y botones Nuevo, Guardar,
  Buscar (por DNI), Alta, Baja y Eliminar.
- **Materias → Gestión de Materias:** tabla con todas las materias y botones Nuevo, Guardar,
  Alta, Baja y Eliminar.

Al hacer clic en una fila de la tabla, sus datos se cargan en el formulario para modificarlos.
Si no hay conexión con la base de datos, el programa muestra un aviso y se cierra.

**Test de consola:** el programa se conecta a la base, ingresa a los integrantes del grupo
(solo si la tabla `alumno` está vacía), los lista por consola y prueba los métodos de
`AlumnoData` y `MateriaData` (buscar, actualizar, baja/alta lógica y borrar).

---

## 🧱 Estructura del proyecto

```
src/main/java/com/mycompany/grupo9_transversal_sgulp/
├── modelo/          → Alumno, Materia, Cursada
├── persistencia/    → miConexion, AlumnoData, MateriaData, CursadaData
└── vista/           → MenuPrincipal (ventana principal), AlumnoVista, MateriaVista,
                       Grupo9_TRANSVERSAL_SGULP (main con test de consola)
```

---

## 📝 Consideraciones del grupo – 2da Entrega

- La **baja** de alumnos y materias es **lógica**: se cambia el campo `activo` / `estado`
  y el registro se conserva junto con su historial. El botón **Alta** lo reactiva.
- El **borrado físico** (botón Eliminar) pide confirmación y no se permite si el alumno
  o la materia tienen cursadas asociadas, para no perder el historial académico.
- El botón **Guardar** inserta un registro nuevo si el formulario no tiene ID, y actualiza
  el registro seleccionado si lo tiene.
- Se validan los datos antes de guardar: DNI numérico y único, nombre obligatorio con el
  largo máximo de la base (30 caracteres para alumnos, 20 para materias) y fecha de
  nacimiento no futura.
- Todas las ventanas internas se abren desde `MenuPrincipal` con el mismo tamaño, centradas
  y con el mismo comportamiento (método `mostrar()`).
- Los errores de base de datos se informan al usuario con mensajes en pantalla.

---

## 🛠️ Tecnologías

- Java 17
- Maven
- MariaDB (XAMPP) + driver JDBC `mariadb-java-client`
- JCalendar (`JDateChooser`) para elegir la fecha de nacimiento
