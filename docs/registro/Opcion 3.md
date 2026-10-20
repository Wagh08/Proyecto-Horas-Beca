## Java + Spring Boot + Flutter/Dart

Otra opción para desarrollar la actividad es utilizar Flutter y Dart para crear
la aplicación móvil, junto con Java y Spring Boot para construir el servidor.
Esta combinación permite desarrollar una aplicación compatible con Android e
iOS y mantener separadas la interfaz, las reglas del sistema y el almacenamiento
de la información.

Flutter es un framework de código abierto creado por Google para desarrollar
aplicaciones multiplataforma desde una misma base de código. Utiliza Dart como
lenguaje de programación y emplea componentes llamados widgets para construir
pantallas, formularios, botones, menús y barras de progreso. En el proyecto,
Flutter podría utilizarse para implementar el inicio de sesión, mostrar las
actividades disponibles, registrar inscripciones, consultar las horas acumuladas
y acceder a la cámara para leer códigos QR (Flutter, 2026a).

Dart es un lenguaje orientado a objetos que trabaja con clases, objetos, métodos,
interfaces y herencia. Aunque sería un lenguaje nuevo para el equipo, comparte
varios conceptos con Java. La dificultad principal se encontraría en aprender la
estructura de Flutter, el uso de widgets, la navegación entre pantallas y la
gestión del estado de la aplicación (Dart, 2026).

Java y Spring Boot se utilizarían para desarrollar el backend. Esta parte
recibiría las solicitudes de la aplicación móvil y se encargaría de administrar
los usuarios, las actividades, las inscripciones, las asistencias y el progreso
de las horas. La comunicación entre Flutter y Spring Boot se realizaría mediante
servicios REST, solicitudes HTTP y respuestas en formato JSON (Spring, s. f.).

Para una demostración académica, el servidor Spring Boot podría ejecutarse
temporalmente en una computadora. Los teléfonos se conectarían mediante una red
local mientras el servidor permanezca activo. Los datos podrían almacenarse
inicialmente en colecciones como ArrayList, aunque se perderían al cerrar el
programa. Si el proyecto creciera, este almacenamiento podría sustituirse por
una base de datos (Oracle, s. f.).

Flutter también permite acceder a la cámara mediante paquetes como
mobile_scanner, compatible con Android e iOS. Después de leer un código QR,
la aplicación enviaría su contenido al servidor. Spring Boot verificaría la
actividad, la inscripción del estudiante, la vigencia del código y la ausencia
de un registro anterior antes de acreditar las horas correspondientes
(Steenbakker, 2026).

La principal ventaja de esta alternativa es que permite utilizar gran parte del
mismo código para Android e iOS. También facilita la creación de interfaces
visuales consistentes y mantiene las validaciones importantes en un servidor
central. Sus limitaciones son la necesidad de aprender Dart y Flutter, realizar
algunas configuraciones específicas para cada plataforma y utilizar macOS para
preparar localmente la versión de iOS (Flutter, 2026a; Flutter, 2026b).

En conclusión, Java + Spring Boot + Flutter/Dart es una alternativa apropiada
cuando se desea crear una aplicación móvil para Android e iOS. Flutter se
encargaría de la interfaz y la interacción con los usuarios, mientras que
Spring Boot administraría la información y las reglas principales. Aunque
requiere aprender nuevas tecnologías, ofrece una estructura organizada y
permite ampliar el sistema posteriormente.

**Referencias**

Dart. (2026). *Classes*. Dart documentation.
[https://dart.dev/language/classes](https://dart.dev/language/classes)

Flutter. (2026a). *Build for and integrate with multiple platforms*.
Flutter documentation.
[https://docs.flutter.dev/platform-integration](https://docs.flutter.dev/platform-integration)

Flutter. (2026b). *Writing custom platform-specific code*.
Flutter documentation.
[https://docs.flutter.dev/platform-integration/platform-channels](https://docs.flutter.dev/platform-integration/platform-channels)

Oracle. (s. f.). *ArrayList Java SE 21 and JDK 21*.
Java Platform documentation.
[https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html)

Spring. (s. f.). *Building a RESTful Web Service*. Spring Guides.
[https://spring.io/guides/gs/rest-service/](https://spring.io/guides/gs/rest-service/)

Steenbakker, J. (2026). *mobile_scanner* (versión 7.4.1)
[Paquete de software]. pub.dev.
[https://pub.dev/packages/mobile_scanner](https://pub.dev/packages/mobile_scanner)
