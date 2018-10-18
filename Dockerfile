FROM openjdk:12-alpine

COPY ./build/libs/spring-boot-payments-0.1.0.jar .

CMD ["java", "-jar", "./spring-boot-payments-0.1.0.jar"]