# Registro de pruebas RF-07

## Proyecto

Horas Beca

## Requisito

RF-07: Confirmar y verificar la llegada.

## Fecha de las pruebas

26 de octubre de 2026.

## Entorno

Aplicación web con datos persistidos en SQLite.

## Pruebas manuales

Resultado comunicado por el equipo: todas las comprobaciones de RF-07 se completaron correctamente.

| Comprobación | Resultado |
| --- | --- |
| Mostrar al estudiante su inscripción en la actividad. | Aprobada |
| Impedir el aviso de llegada antes de iniciar la actividad. | Aprobada |
| Permitir al estudiante inscrito avisar su llegada durante la actividad. | Aprobada |
| Guardar `LLEGADA_AVISADA` y `aviso_llegada_en` en SQLite. | Aprobada |
| Mostrar al organizador la lista real de participantes de su actividad. | Aprobada |
| Mostrar al organizador el aviso de llegada pendiente de verificación. | Aprobada |
| Permitir al organizador de la actividad verificar una llegada avisada. | Aprobada |
| Guardar `LLEGADA_VERIFICADA` y `verificado_en` en SQLite. | Aprobada |
| Rechazar un segundo intento de verificación de la misma llegada. | Aprobada |
| Impedir que un organizador ajeno consulte o verifique llegadas de otra actividad. | Aprobada |

## Alcance

Este registro corresponde a las comprobaciones manuales de llegada y verificación de RF-07. Las pruebas de inscripción y cancelación corresponden a RF-06; las de generación, lectura y acreditación mediante QR corresponden a RF-08.