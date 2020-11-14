FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} vsm-online-users-monitor.jar
ENTRYPOINT ["java","-jar","/vsm-online-users-monitor.jar"]