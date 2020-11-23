FROM openjdk:12-alpine

COPY ./build/libs/spring-boot-payments-0.0.6.jar .

CMD ["java", "-jar", "./spring-boot-payments-0.0.6.jar"]
