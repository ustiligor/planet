FROM openjdk:8-alpine

COPY target/uberjar/planet.jar /planet/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/planet/app.jar"]
