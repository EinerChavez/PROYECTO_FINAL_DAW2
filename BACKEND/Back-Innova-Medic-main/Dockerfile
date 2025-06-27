# Usamos la imagen oficial de OpenJDK 17 de manera liviana
FROM openjdk:24-jdk-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR de la aplicación
COPY target/*.jar app.jar

# Exponemos el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]