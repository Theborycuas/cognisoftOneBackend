# Usa la imagen oficial de OpenJDK
FROM eclipse-temurin:17-jdk

# Define el directorio de trabajo
WORKDIR /app

# Copia el jar generado al contenedor
COPY target/*.jar app.jar

# Exponer el puerto (Railway lo ignora, pero es buena práctica)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
