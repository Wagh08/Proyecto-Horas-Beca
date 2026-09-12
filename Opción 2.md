# Java + Android + Spring Boot

Una opción para desarrollar el sistema de horas beca es utilizar Java tanto para la aplicación Android como para el servidor mediante Spring Boot. De esta manera, el equipo podría utilizar el mismo lenguaje de programación en ambas partes del proyecto y aprovechar los conocimientos de programación orientada a objetos que ya posee. Android admite el desarrollo de aplicaciones con código fuente Java, aunque actualmente Google recomienda Kotlin para proyectos Android nuevos (Android Developers, 2026a).

La aplicación Android se encargaría principalmente de la interacción con el usuario. En ella se desarrollarían las pantallas de inicio de sesión, los paneles para estudiantes y organizadores, la visualización de convocatorias, los formularios, la barra de progreso de horas y el acceso a funciones del dispositivo como la cámara. Android Studio es el entorno de desarrollo oficial para Android y permite compilar y ejecutar las aplicaciones tanto en dispositivos físicos como virtuales (Android Developers, 2026b).

Spring Boot funcionaría como el servidor central del sistema y también estaría programado en Java. Se encargaría de recibir las solicitudes realizadas desde los teléfonos y administrar la información de los usuarios, actividades, inscripciones, asistencias y horas acumuladas. Spring permite crear controladores que reciben solicitudes HTTP y pueden devolver información en formatos como JSON, facilitando la comunicación entre la aplicación Android y el servidor (Spring, 2026a).

Para la presentación del proyecto, el servidor no necesitaría estar instalado en una computadora especializada. Spring Boot permite ejecutar un servidor HTTP integrado, por lo que una computadora del equipo podría ejecutar temporalmente el sistema mientras los teléfonos Android se conectan a ella mediante la misma red local. Al finalizar la presentación, el servidor podría detenerse, ya que no sería necesario mantenerlo disponible permanentemente para la presentación de nuestro proyecto académico (Spring, 2026a).

Esta tecnología además, permitiría que los datos usuarios sean almacenados de forma temporal para las primeras pruebas y que posteriormente se guarden en la tecnología de persistencia de datos que nuestro equipo decida implementar.

La aplicación Android y el servidor tendrían responsabilidades diferentes. Android se encargaría de mostrar la información, administrar la navegación entre pantallas, recibir datos de formularios, utilizar la cámara y comunicarse con el servidor. En cambio, Spring Boot contendría las reglas principales del sistema, como verificar la disponibilidad de una actividad, comprobar las inscripciones, registrar asistencias, acreditar horas y calcular el progreso de los estudiantes. Esta separación permite que la información compartida por diferentes teléfonos sea administrada desde un único sistema central (Spring, 2026a).

El código QR podría utilizarse para registrar la asistencia a las actividades. El organizador habilitaría o generaría un código asociado con una actividad y el estudiante lo escanearía utilizando la cámara de su teléfono Android. Android dispone de APIs y bibliotecas que permiten trabajar con la cámara, y ML Kit proporciona una API de lectura de códigos de barras compatible con códigos QR y con ejemplos de implementación en Java (Android Developers, 2026c; Google for Developers, 2026).

Después de escanear el código QR, la aplicación Android enviaría la información obtenida al servidor. Spring Boot podría comprobar que la actividad exista, que el código corresponda a una actividad válida, que el estudiante se encuentre inscrito y que su asistencia no haya sido registrada anteriormente. Solamente después de realizar estas comprobaciones el servidor acreditaría las horas correspondientes, evitando que cada teléfono modifique por sí mismo la información principal del sistema.

La comunicación entre Android y Spring Boot se realizaría mediante solicitudes HTTP. Durante una demostración en una red local, los teléfonos podrían conectarse utilizando la dirección IP de la computadora que ejecuta el servidor. Sin embargo, Android restringe de manera predeterminada el tráfico HTTP sin cifrar en aplicaciones dirigidas a versiones modernas del sistema, por lo que puede ser necesario configurar explícitamente la seguridad de red para una demostración local o utilizar HTTPS si el sistema evolucionara hacia una implementación real (Android Developers, 2026d).

Una ventaja importante de esta alternativa es que Java tendría presencia tanto en la aplicación móvil como en el servidor. En Android se utilizaría para programar las pantallas, eventos, navegación, comunicación con el servidor y acceso a determinadas funciones del dispositivo, mientras que en Spring Boot se utilizaría para implementar las clases del UML, la programación orientada a objetos y las reglas principales del sistema. Esto permitiría que Java fuera el lenguaje de programación predominante en todo el proyecto (Android Developers, 2026a; Spring, 2026a).

Otra ventaja es el acceso directo a las características propias de Android. La aplicación podría utilizar la cámara, administrar permisos y ejecutar funcionalidades específicas del dispositivo de una manera más integrada que una aplicación web. Además, las aplicaciones Android pueden compilarse como paquetes instalables para realizar pruebas y posteriormente distribuirse mediante los mecanismos disponibles para la plataforma (Android Developers, 2026e).

Como desventaja, esta alternativa requiere desarrollar y mantener dos componentes: la aplicación Android y el servidor Spring Boot. Aunque ambos utilizan Java, el equipo tendría que aprender conceptos específicos del desarrollo Android, como actividades, interfaces, permisos, Gradle y el ciclo de vida de las aplicaciones, además de aprender cómo crear servicios y controladores con Spring Boot. Android Studio también incorpora herramientas como el emulador y el sistema de compilación basado en Gradle, por lo que el entorno de desarrollo puede resultar más exigente que el necesario para una aplicación web sencilla (Android Developers, 2026b; Android Developers, 2026f).

Para computadoras con recursos limitados, el equipo podría evitar utilizar constantemente el emulador de Android y realizar las pruebas directamente en teléfonos físicos. Android Studio permite ejecutar los proyectos tanto en dispositivos virtuales como físicos, por lo que utilizar un teléfono conectado durante el desarrollo puede reducir la necesidad de ejecutar un dispositivo Android virtual en la computadora (Android Developers, 2026g).

Otra desventaja importante es que una aplicación Android desarrollada de esta manera no funcionaría directamente en iPhone. Si posteriormente fuera necesario incorporar iOS, el servidor Java y Spring Boot podría conservarse porque seguiría administrando la lógica y los datos de manera centralizada, pero sería necesario desarrollar una nueva aplicación cliente compatible con iOS. Por esta razón, esta alternativa resulta apropiada cuando el objetivo principal es Android y no se requiere desde el inicio una aplicación móvil multiplataforma.

En conclusión, Java, Android y Spring Boot permiten desarrollar un sistema móvil en el que Java tenga un papel central tanto en la aplicación como en el servidor. Android proporcionaría la interfaz, navegación, cámara y lectura del código QR, mientras que Spring Boot manejaría las clases, la información compartida y las reglas principales del sistema. Esta alternativa permitiría aprovechar gran parte del UML y de los conocimientos de programación orientada a objetos existentes, a cambio de una mayor complejidad de desarrollo y de limitar inicialmente la aplicación a dispositivos Android.

## Referencias

* Android Developers. (2026a). [Java versions in Android builds](https://developer.android.com/build/jdks).

* Android Developers. (2026b). [Meet Android Studio](https://developer.android.com/studio/intro).

* Android Developers. (2026c). [Camera API](https://developer.android.com/media/camera/camera-deprecated/camera-api).

* Android Developers. (2026d). [Network security configuration](https://developer.android.com/privacy-and-security/security-config).

* Android Developers. (2026e). [Prepare your app for release](https://developer.android.com/studio/publish/preparing).

* Android Developers. (2026f). [Gradle build overview](https://developer.android.com/build/gradle-build-overview).

* Android Developers. (2026g). [Create a project](https://developer.android.com/studio/projects/create-project).

* Google for Developers. (2026). [Scan barcodes with ML Kit on Android](https://developers.google.com/ml-kit/vision/barcode-scanning/android).

* Oracle. (s. f.). [Object-oriented programming concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html).

* Spring. (2026a). [Servlet web applications](https://docs.spring.io/spring-boot/reference/web/servlet.html).
