FROM openjdk:11-jdk-stretch
VOLUME /tmp
ADD /target/intact-search-interactions-ws-1.1.0.jar intact-search-interactions-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/intact-search-interactions-ws.jar"]