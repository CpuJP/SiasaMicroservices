FROM amazoncorretto:17-alpine

# Establecer el idioma en español
ENV LANG es_ES.UTF-8

# Instalar otras fuentes si es necesario
RUN apk add --update ttf-dejavu ttf-liberation ttf-droid && rm -rf /var/cache/apk/*

# Copiar la aplicación
COPY target/siasa-reportes-1.1-BETA.jar java-app.jar

# Agregar la fuente Arial Black al classpath de la JVM
COPY src/main/resources/ariblk.ttf /usr/share/fonts/
COPY src/main/resources/arial.ttf /usr/share/fonts/

# Configurar la JVM para usar la fuente Arial Black
ENV JAVA_TOOL_OPTIONS="-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Djava.awt.headless=true -Dawt.headless=true -Dawt.font.desktophints=awt -Djava.awt.fonts=/usr/share/fonts/"

# Punto de entrada de la aplicación
ENTRYPOINT ["java", "-jar", "java-app.jar"]
