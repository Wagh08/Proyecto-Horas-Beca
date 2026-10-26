
# Registro de Pruebas RF-07

## Proyecto
Horas Beca

## Requisito
RF-07: Confirmar y verificar la llegada

## Fecha de pruebas
2026-10-26

## Actividad utilizada
- ID: 1
- Nombre: Prueba RF06
- Base de datos: SQLite

## Pruebas de inscripción

| Prueba | Resultado |
|---|---|
| Inscribir estudiante | Aprobada |
| Reservar cupo | Aprobada |
| Mostrar inscripción confirmada | Aprobada |
| Mostrar cupos disponibles | Aprobada |

## Pruebas de cancelación

| Prueba | Resultado |
|---|---|
| Cancelar inscripción | Pendiente |
| Liberar cupo | Pendiente |
| Exigir justificación tardía | Pendiente |
| Conservar historial | Pendiente |

## Pruebas de asistencia

| Prueba | Resultado |
|---|---|
| Mostrar inscripción confirmada | Aprobada |
| Impedir aviso antes del inicio | Aprobada |
| Registrar aviso de llegada | Aprobada |
| Guardar LLEGADA_AVISADA en SQLite | Aprobada |
| Mostrar lista real de participantes | Aprobada |
| Mostrar aviso pendiente de verificación | Aprobada |
| Verificar llegada como organizador | Pendiente de confirmar en SQLite |
| Guardar LLEGADA_VERIFICADA | Pendiente de confirmar en SQLite |
| Impedir doble verificación | Pendiente |
| Rechazar acceso a actividad ajena | Pendiente |

## Pruebas automatizadas

Comando utilizado:

./mvnw test

Resultado: Pendiente de registrar.

## Observaciones

- Se comprobó la visualización de la inscripción.
- Se implementó el aviso de llegada del estudiante.
- Se conectó la lista de participantes con SQLite.
- Se agregó la verificación de llegada por el organizador.
- El QR y la acreditación de horas no forman parte de RF-07.
- Las pruebas pendientes deben ejecutarse antes de dar por finalizada la validación.
