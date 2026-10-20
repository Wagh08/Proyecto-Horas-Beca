# Proyecto-Horas-Beca

## Integrantes

- William García 261035
- Kembeck López 26636
- María Chávez 26187

Aplicación web diseñada para usarse desde teléfonos. Ayuda a los estudiantes becados a encontrar actividades, registrar su participación y consultar las horas beca acreditadas. También permite a los organizadores publicar actividades y gestionar la asistencia.

## Objetivo

Reunir en un mismo sistema las oportunidades de horas beca y el registro de las horas obtenidas por cada estudiante. El proceso abarca desde la publicación de una actividad hasta la acreditación de horas y su consulta en el progreso anual.

## Usuarios

### Estudiante

Puede consultar y filtrar actividades, revisar sus detalles, inscribirse, confirmar que llegó al lugar y consultar sus inscripciones. Al finalizar una actividad, puede mostrar el QR correspondiente para que se valide su participación. También puede revisar su progreso anual y el historial de horas acreditadas.

### Organizador

Puede publicar actividades con sus horarios, ubicación, requisitos, cupos y horas acreditables. Además, puede consultar participantes, verificar llegadas y validar la asistencia final mediante QR.

## Actividades

Cada actividad muestra su nombre, descripción, organizador, fecha, horario, ubicación, cupo, requisitos y cantidad de horas beca que acredita. Las horas acreditables se configuran al crearla: pueden ser distintas de su duración.

Los estudiantes pueden buscar y filtrar actividades por características como fecha, ubicación, horas acreditables y disponibilidad de cupos.

### Actividades individuales

El estudiante ocupa un cupo al inscribirse. Puede hacerlo mientras haya espacio y la actividad no haya comenzado. Si cancela antes del inicio, el cupo vuelve a estar disponible. Cuando faltan menos de 60 minutos para comenzar, debe indicar una justificación para cancelar; esta queda registrada para el organizador.

### Actividades recurrentes

Una actividad recurrente reúne a un grupo durante un período, por ejemplo, todos los martes de un semestre. El estudiante solicita una plaza una sola vez y el organizador decide si la acepta. Cada jornada tiene su propio registro de llegada, verificación de asistencia y acreditación de horas.

## Asistencia y acreditación

La participación sigue estas etapas:

1. El estudiante se inscribe en una actividad individual o es aceptado en una recurrente.
2. Al llegar, confirma su asistencia.
3. El organizador verifica su llegada.
4. Al terminar la actividad o jornada, el estudiante accede a su QR.
5. El organizador valida el QR y se acreditan las horas correspondientes.

La inscripción y la confirmación de llegada no acreditan horas por sí solas. El sistema evita acreditar dos veces la misma participación.

## Progreso anual

Cada estudiante puede consultar su meta anual, las horas acreditadas, las horas pendientes y el historial de actividades realizadas. La meta depende del porcentaje de beca:

| Porcentaje de beca | Horas anuales |
| --- | --- |
| 1% – 15% | 30 |
| 16% – 25% | 50 |
| 26% – 50% | 70 |
| 51% – 75% | 80 |
| 76% – 100% | 100 |

El período anual comienza el 1 de enero. El historial de años anteriores se conserva para consulta y las horas que excedan la meta se consideran para el año siguiente.

El ciclo académico del estudiante se registra como un dato del perfil y puede ser modificado por el equipo responsable del sistema.

## Interfaz

La aplicación está diseñada para pantallas de teléfono. Utiliza una barra de navegación inferior y ofrece tema claro y oscuro con una paleta centrada en el verde. Las opciones visibles dependen de si la persona ingresa como estudiante u organizador.

## Tecnologías

- **Java y Spring Boot:** funcionamiento de la aplicación y reglas del sistema.
- **Thymeleaf:** presentación de datos en las páginas HTML.
- **HTML, CSS y JavaScript:** estructura, apariencia e interacciones de la interfaz.
- **SQLite:** almacenamiento de usuarios, actividades, inscripciones y acreditaciones.
- **Maven:** gestión de dependencias y construcción del proyecto.

## Organización del repositorio

- **`src/main/java/`:** Código Java de la aplicación web.
- **`src/main/resources/templates/`:** Páginas HTML utilizadas por Thymeleaf.
- **`src/main/resources/static/`:** CSS, JavaScript e imágenes.
- **`prototipos/consola/`:** Programa Java inicial y su propio README.
- **`prototipos/interfaz/`:** Pruebas visuales de la aplicación.
- **`registro/`:** Documentos y decisiones del proyecto.