FROM tomcat:8-jdk8-openjdk

RUN sed -i 's/Connector port="8080"/Connector port="8083"/' /usr/local/tomcat/conf/server.xml
COPY target/TrainBook-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/TrainBook.war

# Expone el puerto 8080, que es el puerto predeterminado de Tomcat
EXPOSE 8083

# Comando para iniciar Tomcat cuando el contenedor se ejecute
# catalina.sh run inicia Tomcat en primer plano
CMD ["catalina.sh", "run"]