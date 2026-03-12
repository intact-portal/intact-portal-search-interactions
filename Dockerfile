FROM alpine/java:11-jdk
VOLUME /tmp
ADD /target/intact-search-interactions-ws.jar intact-search-interactions-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/intact-search-interactions-ws.jar"]