FROM amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar",  "-Daws.region=eu-west-1", "-Daws.accessKeyId=", "-Daws.secretAccessKey=", "/app.jar"]
