FROM openjdk:21-slim

ARG JAR_FILE=build/libs/travelly-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} travelly.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/travelly.jar"]
#ENTRYPOINT ["java", "-jar", "/travelly.jar"]