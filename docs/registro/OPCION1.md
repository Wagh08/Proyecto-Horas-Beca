# Java + Spring Boot + Thymeleaf

Una opción para desarrollar el sistema de horas beca es utilizar Java y Spring Boot junto con Thymeleaf. Java se usaría para programar las clases y las reglas del sistema, mientras que Thymeleaf permitiría crear las páginas que utilizarán los estudiantes y organizadores.

Spring Boot se encargaría de recibir las solicitudes y administrar la información de los usuarios, las actividades, las inscripciones, las asistencias y las horas acumuladas. Para la presentación del proyecto, el servidor podría ejecutarse desde una computadora y los demás dispositivos se conectarían por medio de una red local (Spring, 2026a; Spring, 2026b).

Thymeleaf permitiría crear las páginas de inicio de sesión, los paneles de cada usuario, los formularios para publicar actividades y la barra de progreso de las horas. Estas páginas pueden adaptarse para que funcionen correctamente en computadoras, tabletas y teléfonos (Thymeleaf, 2024).

El proyecto seguiría utilizando programación orientada a objetos. Las clases Usuario, Estudiante, Organizador, Actividad, Inscripción y Asistencia podrían conservarse a partir del UML existente. También se podrían aplicar conceptos como herencia, encapsulamiento, abstracción y polimorfismo (Oracle, s. f.).

Durante las primeras pruebas, la información podría almacenarse en ArrayList. Esto evitaría tener que configurar una base de datos, aunque los datos se perderían al cerrar el servidor. Si el proyecto creciera, las listas podrían sustituirse por una base de datos sin cambiar todo el funcionamiento del sistema (Oracle, 2025).

El código QR serviría para registrar la asistencia de los estudiantes. Después de escanearlo, la página enviaría el código al servidor. Spring Boot comprobaría que la actividad exista, que el estudiante esté inscrito y que la asistencia no haya sido registrada anteriormente antes de acreditar las horas.

Para usar la cámara desde el navegador se necesita una conexión segura. Por esta razón, durante la presentación sería recomendable utilizar HTTPS. También se podría incluir una opción para ingresar el código manualmente en caso de que la cámara no funcione (MDN Web Docs, 2026a; MDN Web Docs, 2026b).

La ventaja de esta alternativa es que los usuarios podrían ingresar desde distintos dispositivos sin instalar una aplicación. Además, el equipo podría continuar trabajando principalmente con Java y aprovechar las clases que ya fueron diseñadas.

En conclusión, Java, Spring Boot y Thymeleaf permiten desarrollar un sistema web sencillo para administrar las horas beca. Thymeleaf mostraría las páginas y formularios, mientras que Spring Boot manejaría la información y las reglas principales. Esta organización también permitiría agregar una base de datos más adelante.

## Referencias

* MDN Web Docs. (2026a). [MediaDevices: getUserMedia() method](https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia).

* MDN Web Docs. (2026b). [Features restricted to secure contexts](https://developer.mozilla.org/en-US/docs/Web/Security/Defenses/Secure_Contexts/features_restricted_to_secure_contexts).

* Oracle. (2025). [ArrayList Java SE 21 and JDK 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html).

* Oracle. (s. f.). [Object-oriented programming concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html).

* Spring. (2026a). [Servlet web applications](https://docs.spring.io/spring-boot/reference/web/servlet.html).

* Spring. (2026b). [Building an application with Spring Boot](https://spring.io/guides/gs/spring-boot/).

* Thymeleaf. (2024). [Tutorial: Thymeleaf + Spring 3.1](https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.pdf).
