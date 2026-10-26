# Registro de pruebas RF-08

## Proyecto

Horas Beca

## Requisito

RF-08: Acreditar asistencia mediante QR.

## Fecha de las pruebas

26 de octubre de 2026.

## Pruebas automatizadas

Comando ejecutado desde la raíz del proyecto en PowerShell:

```powershell
.\mvnw.cmd test
```

Resultado comunicado por el equipo:

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

El total corresponde a cinco pruebas que ya existían en el proyecto y cuatro pruebas agregadas para RF-08.05.

| Casos de RF-08.05 | Resultado |
| --- | --- |
| Acreditar las horas de una inscripción válida una sola vez; rechazar un segundo intento sin duplicar el registro. | Aprobado |
| Rechazar la acreditación antes de finalizar la actividad o cuando el QR corresponde a otra actividad. | Aprobado |
| Rechazar un código inexistente y la validación de un organizador que no corresponde a la actividad. | Aprobado |
| Rechazar una inscripción sin llegada verificada o cancelada. | Aprobado |

## Alcance del resultado

Las pruebas automatizadas comprueban las condiciones de acreditación y rechazo del repositorio. Las pruebas manuales comprobaron el flujo de lectura del QR, los mensajes en el navegador, la visualización de las horas y su persistencia.

## Comprobaciones manuales

Resultado comunicado por el equipo: todas las comprobaciones manuales fueron aprobadas.

| Comprobación | Estado |
| --- | --- |
| Generar y mostrar el QR de una actividad finalizada con llegada verificada. | Aprobada |
| Escanear el QR desde la pantalla del organizador y confirmar el mensaje mostrado. | Aprobada |
| Verificar que las horas aparezcan en progreso e historial tras la acreditación. | Aprobada |
| Repetir el escaneo y comprobar que las horas no aumenten; reiniciar la aplicación y confirmar que persisten. | Aprobada |
