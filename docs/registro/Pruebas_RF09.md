# Registro de pruebas RF-09

## Requisito

RF-09: Consultar Progreso e Historial.

## Fecha

Indicar la fecha en que se ejecutaron las pruebas.

## Pruebas automatizadas

Comando: `.\mvnw.cmd test`.

Resultado: 13 pruebas ejecutadas; 0 fallas; 0 errores; 0 omitidas. BUILD SUCCESS.

| Escenario | Resultado |
| --- | --- |
| Estudiante sin acreditaciones: cero horas, cero por ciento y meta pendiente completa. | Aprobado |
| Avance parcial: solo se suman horas del año y del estudiante consultados. | Aprobado |
| Meta completa: cero horas pendientes y barra al cien por ciento. | Aprobado |
| Exceso: porcentaje real superior al cien por ciento y barra limitada al cien por ciento. | Aprobado |
| Historial: años disponibles, orden por fecha y consulta del estudiante correspondiente. | Aprobado |

## Pruebas manuales

| Comprobación | Resultado |
| --- | --- |
| Inicio y Progreso muestran las mismas cifras actuales. | Aprobada |
| La barra se anima al entrar en ambas pantallas. | Aprobada |
| El historial cambia al seleccionar un año anterior. | Aprobada |
| La selección del historial conserva el progreso del año actual. | Aprobada |