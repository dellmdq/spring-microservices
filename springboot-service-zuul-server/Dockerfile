FROM openjdk:11
VOLUME /tmp
EXPOSE 8090
ADD ./target/springboot-service-zuul-server-0.0.1-SNAPSHOT.jar service-zuul-server.jar
ENTRYPOINT ["java","-jar","/service-zuul-server.jar"]