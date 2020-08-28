build:
	gradle build

clean:
	gradle clean

run:
	gradle build
	java -jar build/libs/spring-boot-payments-0.1.0.jar

build_docker:
	docker build -t hashicorpdemoapp/payments:latest .

push_docker: build_docker
	docker push hashicorpdemoapp/payments:latest
