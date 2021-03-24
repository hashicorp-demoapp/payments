FROM openjdk:12-alpine

COPY ./build/libs/spring-boot-payments-0.0.10.jar .

ENTRYPOINT ["java", "-jar", "./spring-boot-payments-0.0.10.jar"]
