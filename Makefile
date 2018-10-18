build:
	gradle build

clean:
	gradle clean

run:
	gradle build
	java -jar build/libs/spring-boot-payments-0.1.0.jar

build_docker:
	docker build -t nicholasjackson/emojify-payments:latest .

push_docker: build_docker
	docker push nicholasjackson/emojify-payments:latest
