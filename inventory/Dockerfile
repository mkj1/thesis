FROM openjdk:14-jdk-alpine
EXPOSE 8080 8081 8082 8083
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]