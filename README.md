# Proyecto-Horas-Beca

## Integrantes

- William García 261035
- Kembeck López 26636
- María Chávez 26187

## Análisis
### Requisitos Funcionales

| Prioridad | Funcionalidad                                                                                                                                                                                                                                                      |
| :-------: | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
|     1     | Mostrar oportunidades de horas beca con su información.                                                                                                                                                                                                            |
|     1     | Permitir a estudiantes aplicar a las oportunidades.                                                                                                                                                                                                                |
|     1     | Registrar los cupos restantes para una oportunidad y cerrar las aplicaciones cuando esta se llene.                                                                                                                                                                 |
|     2     | Mostrar progreso en las horas beca anuales de cada estudiante.                                                                                                                                                                                                     |
|     1     | Permitir a organizadores crear oportunidades de horas beca.                                                                                                                                                                                                        |
|     1     | Habilitar QR para registro de horas beca.                                                                                                                                                                                                                          |
|     1     | Escanear QR para registro de horas beca.                                                                                                                                                                                                                           |
|     3     | Mostrar/Adquirir información acerca de los convocadores de horas beca, con información estadística de cada cuanto suelen ofrecer oportunidades, cuantas horas beca suelen ofrecer, si ofrecen horas doble, y los horarios y días en que suelen ofrecer horas beca. |
|     1     | Iniciar sesión.                                                                                                                                                                                                                                                    |
|     3     | Acceder a información de contacto (correo y/o telefono y ubicación en la ubicación) del director de la carrera, o el departamento de ayudas financieras.                                                                                                           |
|     2     | Acceder a un historial o registro de las actividades realizadas y las horas beca ganadas.                                                                                                                                                                          |

### Clases

#### Clase Usuario

##### Descripción

Representa la información general de las personas que utilizan el sistema. Será la clase padre de Estudiante y Organizador, debido a que ambos comparten información como nombre, correo, teléfono y contraseña.

##### Atributos

- nombre: almacena el nombre completo del usuario.
- correo: almacena el correo utilizado para iniciar sesión y contactar al usuario.
- telefono: almacena el número telefónico del usuario.
- contrasena: almacena la contraseña utilizada para ingresar al sistema.
- carreraOCargo: almacena la carrera del estudiante o el cargo que desempeña el organizador.

##### Métodos

- Usuario(): inicializa un nuevo usuario.
- getNombre() y setNombre(): permiten consultar y modificar el nombre.
- getCorreo() y setCorreo(): permiten consultar y modificar el correo.
- getTelefono() y setTelefono(): permiten consultar y modificar el teléfono.
- getContrasena() y setContrasena(): permiten consultar y modificar la contraseña.
- getCarreraOCargo() y setCarreraOCargo(): permiten consultar y modificar la carrera o el cargo del usuario.
- validarContrasena(): comprueba si la contraseña ingresada coincide con la almacenada.
- mostrarInformacion(): devuelve la información general del usuario.

#### Clase Estudiante

##### Descripción

Representa a un estudiante becado que debe completar una cantidad determinada de horas beca. Hereda la información general de Usuario.

Esta clase almacena el progreso del estudiante, las actividades a las que aplicó y su historial de actividades realizadas.

##### Atributos

- carne: almacena el número de carné del estudiante.
- horasRequeridas: almacena la cantidad de horas beca que debe completar.
- horasRealizadas: almacena la cantidad de horas que ya fueron acreditadas.
- codigoQR: almacena el código utilizado para registrar sus horas.
- actividadesAplicadas: almacena las actividades a las que aplicó.
- historialActividades: almacena las actividades que ya realizó.
- cicloProgreso: almacena el ciclo académico al que corresponde el progreso de horas beca.

##### Métodos

- Estudiante(): inicializa un estudiante.
- Métodos get y set: permiten consultar y modificar sus atributos.
- aplicarActividad(): registra una actividad entre las oportunidades a las que aplicó.
- registrarHoras(): suma horas al total realizado.
- calcularHorasPendientes(): calcula cuántas horas le faltan por completar.
- calcularProgreso(): calcula el porcentaje de avance anual.
- agregarAlHistorial(): registra una actividad que el estudiante completó.
- getHistorialActividades(): devuelve las actividades realizadas.
- mostrarInformacion(): devuelve la información principal del estudiante.
- getCicloProgreso() y setCicloProgreso(): permiten consultar y modificar el ciclo académico del progreso.
- generarCodigoQR(): genera el código QR utilizado para registrar las horas beca.
- consultarProgresoAnual(): devuelve el progreso de horas beca correspondiente al ciclo establecido.

#### Clase Organizador

##### Descripción

Representa a la persona encargada de crear oportunidades de horas beca. Hereda los datos generales de Usuario y guarda las actividades que ha creado.

##### Atributos

- departamento: almacena el departamento al que pertenece.
- historialActividades: almacena las actividades creadas.

##### Métodos

- Organizador(): inicializa al organizador.
- crearActividad(): crea una actividad indicando su nombre, descripción, fecha, hora y si ofrece horas dobles.
- guardarActividad(): guarda la actividad en el historial.
- getHistorialActividades(): devuelve las actividades creadas.
- mostrarInformacion(): devuelve la información del organizador.
- calcularFrecuenciaActividades(): calcula cada cuánto tiempo el organizador suele crear actividades.
- calcularPromedioHoras(): calcula la cantidad promedio de horas beca que ofrece.
- obtenerDiasFrecuentes(): obtiene los días en los que suele ofrecer actividades.
- obtenerHorariosFrecuentes(): obtiene los horarios en los que suele ofrecer actividades.
- calcularFrecuenciaHorasDobles(): calcula con qué frecuencia ofrece actividades con horas dobles.

#### Clase Actividad

##### Descripción

Representa a las actividades de horas beca disponibles para que los usuarios puedan realizarla.

##### Atributos

- nombre: nombre de la actividad.
- fecha: fecha de la actividad.
- horaInicio: hora en la que inicia la actividad.
- horaFin: hora en la que termina la actividad.
- descripcion: explica en qué consiste la actividad y detalla información importante.
- ubicacion: indica dónde se realiza la actividad.
- cantidadHoras: cantidad de horas que otorga la actividad.
- cupoMaximo: almacena la cantidad máxima de estudiantes que pueden participar.
- cuposRestantes: almacena la cantidad de espacios que todavía están disponibles.
- horasDobles: indica si la actividad acredita el doble de horas.
- estado: almacena si la actividad está abierta, llena, finalizada o cancelada.
- organizador: almacena el organizador responsable de la actividad.
- estudiantesInscritos: almacena los estudiantes inscritos en la actividad.

##### Métodos

- Actividad(): inicia una actividad.
- agregarEstudiante(): agrega un estudiante a la actividad.
- eliminarEstudiante(): elimina un estudiante de la actividad.
- verificarDisponibilidad(): comprueba si todavía quedan cupos disponibles.
- calcularHorasOtorgadas(): calcula las horas que recibirá el estudiante, tomando en cuenta si la actividad ofrece horas dobles.
- cerrarAplicaciones(): cierra automáticamente las aplicaciones cuando se llena la actividad.
- finalizarActividad(): cambia el estado de la actividad a finalizada.
- cancelarActividad(): cambia el estado de la actividad a cancelada.
- mostrarInformacion(): devuelve la información completa de la actividad.
- getCupoMaximo() y setCupoMaximo(): permiten consultar y modificar el cupo máximo.
- getCuposRestantes() y setCuposRestantes(): permiten consultar y modificar los cupos restantes.
- isHorasDobles() y setHorasDobles(): permiten consultar y modificar si la actividad ofrece horas dobles.
- getEstado() y setEstado(): permiten consultar y modificar el estado de la actividad.
- getOrganizador() y setOrganizador(): permiten consultar y asignar al organizador responsable.
- getEstudiantesInscritos() y setEstudiantesInscritos(): permiten consultar y modificar los estudiantes inscritos.
- calcularCuposRestantes(): calcula los cupos disponibles a partir del cupo máximo y los estudiantes inscritos.

#### Clase Vista

##### Descripción

Representa la interfaz mediante la cual los usuarios interactúan con el sistema.

##### Atributos

- scanner: permite recibir datos ingresados por el usuario.
- controlador: almacena la referencia al controlador.
- opcionSeleccionada: almacena la opción elegida en el menú.

##### Métodos

- Vista(): inicializa la interfaz.
- mostrarMenuPrincipal(): muestra las opciones principales.
- mostrarMenuEstudiante(): muestra las opciones del estudiante.
- mostrarMenuOrganizador(): muestra las opciones del organizador.
- solicitarCredenciales(): solicita el correo y la contraseña.
- mostrarActividades(): presenta las actividades disponibles.
- iniciar(): inicia y mantiene activos los menús.
- mostrarProgreso(): muestra el avance del estudiante en sus horas beca.
- mostrarHistorial(): muestra las actividades realizadas y las horas obtenidas.
- mostrarEstadisticasOrganizador(): muestra la información estadística de un organizador.
- mostrarInformacionDirectorCarrera(): muestra la información de contacto del director.
- mostrarInformacionAyudasFinancieras(): muestra la información de contacto de ayudas financieras.
- mostrarCodigoQR(): presenta el código QR del estudiante.
- solicitarEscaneoQR(): permite ingresar o escanear un código QR.
- mostrarMensajeAplicacionesCerradas(): informa que una actividad ya no acepta más aplicaciones.

#### Clase Controlador

##### Descripción

Coordina la comunicación entre la vista y el sistema. Recibe las solicitudes del usuario y ejecuta las operaciones correspondientes.

##### Atributos

- sistema: almacena la referencia al sistema.
- vista: almacena la referencia a la interfaz.
- usuarioActual: almacena al usuario que inició sesión.

##### Métodos

- Controlador(): inicializa el controlador.
- iniciarSesion(): verifica las credenciales.
- cerrarSesion(): finaliza la sesión actual.
- consultarActividades(): obtiene las actividades disponibles.
- aplicarActividad(): registra a un estudiante en una actividad.
- crearActividad(): solicita la creación de una actividad.
- registrarAsistencia(): registra la asistencia del estudiante.
- consultarProgreso(): solicita al sistema el progreso del estudiante.
- consultarHistorial(): solicita al sistema el historial del estudiante.
- consultarEstadisticasOrganizador(): solicita las estadísticas de un organizador.
- consultarInformacionDirectorCarrera(): solicita la información de contacto del director.
- consultarInformacionAyudasFinancieras(): solicita la información de contacto de ayudas financieras.
- generarCodigoQR(): solicita la generación del código QR del estudiante.
- escanearCodigoQR(): recibe el código QR escaneado y solicita el registro de las horas.

#### Clase Sistema

##### Descripción

Administra la información y las operaciones principales del programa de horas beca. Almacena usuarios, actividades y registros de asistencia.

##### Atributos

- usuarios: almacena los usuarios registrados.
- actividades: almacena las oportunidades de horas beca.
- asistencias: almacena los registros de asistencia.
- usuarioActivo: almacena al usuario que inició sesión.
- nombreDirectorCarrera: almacena el nombre del director de la carrera.
- correoDirectorCarrera: almacena el correo electrónico del director de la carrera.
- telefonoDirectorCarrera: almacena el teléfono del director de la carrera.
- ubicacionDirectorCarrera: almacena la ubicación de la oficina del director.
- correoAyudasFinancieras: almacena el correo del departamento de ayudas financieras.
- telefonoAyudasFinancieras: almacena el teléfono del departamento de ayudas financieras.
- ubicacionAyudasFinancieras: almacena la ubicación del departamento de ayudas financieras.

##### Métodos

- Sistema(): inicializa las colecciones del sistema.
- registrarUsuario(): agrega un nuevo usuario.
- iniciarSesion(): valida el correo y la contraseña.
- cerrarSesion(): finaliza la sesión activa.
- agregarActividad(): registra una nueva actividad.
- buscarActividad(): busca una actividad.
- obtenerActividadesDisponibles(): devuelve las actividades con cupos.
- aplicarEstudiante(): registra a un estudiante en una actividad.
- registrarAsistenciaQR(): registra asistencia mediante un código QR.
- acreditarHoras(): suma las horas obtenidas al estudiante.
- getNombreDirectorCarrera() y setNombreDirectorCarrera(): permiten consultar y modificar el nombre del director.
- getCorreoDirectorCarrera() y setCorreoDirectorCarrera(): permiten consultar y modificar el correo del director.
- getTelefonoDirectorCarrera() y setTelefonoDirectorCarrera(): permiten consultar y modificar el teléfono del director.
- getUbicacionDirectorCarrera() y setUbicacionDirectorCarrera(): permiten consultar y modificar la ubicación del director.
- getCorreoAyudasFinancieras() y setCorreoAyudasFinancieras(): permiten consultar y modificar el correo de ayudas financieras.
- getTelefonoAyudasFinancieras() y setTelefonoAyudasFinancieras(): permiten consultar y modificar el teléfono de ayudas financieras.
- getUbicacionAyudasFinancieras() y setUbicacionAyudasFinancieras(): permiten consultar y modificar la ubicación de ayudas financieras.
- buscarEstudiantePorQR(): busca al estudiante relacionado con un código QR.
- validarRegistroDuplicado(): comprueba que las horas de una actividad no hayan sido registradas anteriormente.
- obtenerEstadisticasOrganizador(): obtiene la información estadística de un organizador.
- obtenerInformacionDirectorCarrera(): devuelve la información de contacto del director.
- obtenerInformacionAyudasFinancieras(): devuelve la información de contacto de ayudas financieras.
- consultarHistorialEstudiante(): obtiene las actividades realizadas por un estudiante.
- consultarProgresoEstudiante(): obtiene el progreso de horas beca de un estudiante.

#### Relación entre las clases

Las clases Estudiante y Organizador tienen una relación de herencia con su clase padre, que sería Usuario. A su vez, tanto Estudiante como Organizador tienen una relación de asociación con la clase Actividad. Las clases Usuario y Actividad tienen una relación de agregación con la clase Sistema. La clase Controlador está asociada con Vista, Usuario y Sistema, mientras que mantiene una relación de dependencia con las demás clases de tipo modelo.
