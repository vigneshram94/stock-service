FROM openjdk:8
ADD target/stock-service.jar stock-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "stock-service.jar"]