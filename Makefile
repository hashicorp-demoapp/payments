build:
	./gradlew build

clean:
	./gradlew clean

run:
	./gradlew build
	java -jar build/libs/spring-boot-payments-0.0.15.jar

build_docker:
	docker build -t hashicorpdemoapp/payments:v0.0.15 .

push_docker: build_docker
	docker push hashicorpdemoapp/payments:v0.0.15
