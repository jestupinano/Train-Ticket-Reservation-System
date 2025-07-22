# ---- STAGE 1: build ----
FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q -DskipTests clean verify || true
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---- STAGE 2: runtime ----
FROM tomcat:8.5-jdk8-temurin
# opcional: eliminar apps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*
# Copiar WAR
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Configuración opcional de timezone/locale
ENV TZ=America/Bogota

EXPOSE 8080
