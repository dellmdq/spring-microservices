FROM openjdk:11
VOLUME /tmp
#default port for zipkin is 9411
EXPOSE 9411
ADD ./zipkin-server-2.23.16-exec.jar zipkin-server.jar
ENTRYPOINT ["java","-jar","/zipkin-server.jar"]