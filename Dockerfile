FROM openjdk:8u111-jdk-alpine
VOLUME /tmp
ADD /target/intact-search-interactions-ws-1.0.7-SNAPSHOT.jar intact-search-interactions-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/intact-search-interactions-ws.jar"]