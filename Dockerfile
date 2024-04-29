# Usar la imagen base de Java 17
FROM openjdk:17-jdk-alpine

# Variable de entorno para pasar el puerto que Spring Boot usará
ARG PORT=8080
ENV SERVER_PORT=$PORT

# Exponer el puerto en el que corre la aplicación
EXPOSE $PORT

LABEL maintainer="Alonso S"

# Añadir el archivo jar ejecutable
COPY target/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
