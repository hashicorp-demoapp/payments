# Payments

Dummy payments gateway written with Java SpringBoot

[![CircleCI](https://circleci.com/gh/hashicorp-demoapp/payments.svg?style=svg)](https://circleci.com/gh/hashicorp-demoapp/payments)  

Docker Hub Image: [https://hub.docker.com/repository/docker/hashicorpdemoapp/payments](https://hub.docker.com/repository/docker/hashicorpdemoapp/payments)

## Usage
Currently this API has a single endpoint at POST `/` <br>
There are three controllers you can configure to process a payment from the HashiCups app:

* [REST](src/main/java/payments/RestPaymentController.java)
* [Redis](src/main/java/payments/RedisPaymentController.java)
* [Database](src/main/java/payments/DBPaymentController.java)

See below for how to use each controller. You can override any of the [default property files](src/main/resources) using [external config for Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config).

The most simple option to override is place the files in the [current directory](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-application-property-files).

## Vault Encryption
Vault encryption is available for the Redis controller leveraging [transit](https://www.vaultproject.io/docs/secrets/transit), and the DB payment controller leveraging [transform](https://www.vaultproject.io/docs/secrets/transform).


You will need to provide a valid bootstrap file pointing to your Vault instance to enable the Vault integration. Below is an example for token authentication. See [the Spring Cloud Vault documentation](https://cloud.spring.io/spring-cloud-vault/reference/html/#vault.config.authentication) for all available auth methods.

bootstrap.yaml

```
spring:
  cloud:
    vault:
      enabled: true
      fail-fast: true
      authentication: TOKEN
      token: root
      host: localhost
      port: 8200
      scheme: http
```

### REST controller

This controller is configured by default and no additional steps are required.

```
curl -s -X POST --header "Content-Type: application/json" --data /
'{"name": "Gerry", "type": "mastercard", "number": "1234-1234-1234-1234", "expiry": "01/23", "cvc": "123"}' localhost:8080  | jq
{
  "message": "Payment processed successfully, card details returned for demo purposes, not for production",
  "id": "5cc26d60-bc28-4a22-ab59-853ef9b0c209",
  "card_plaintext": "1234-1234-1234-1234",
  "card_ciphertext": "Encryption Disabled"
}
```


### Redis controller

The Redis controller will enable when the `app.storage=redis` property is configured. Below is an example with Vault transit encryption enabled.

vault.sh:

```
vault secrets enable transit
vault write -f transit/keys/payments
```

application.properties:

```
app.storage=redis
app.encryption.enabled=true
app.encryption.path=transit
app.encryption.key=payments
spring.redis.host=localhost
spring.redis.port=6379
```

```
curl -s -X POST --header "Content-Type: application/json" --data '{"name": "Gerry", "type": "mastercard", "number": "1234-1234-1234-1234", "expiry": "01/23", "cvc": "123"}' localhost:8080  | jq
{
  "card_plaintext": "1234-1234-1234-1234",
  "message": "Payment processed successfully, card details returned for demo purposes, not for production",
  "id": "2de542c7-1f7f-4e81-b35a-7a099b23ca92",
  "card_ciphertext": "vault:v1:Z3HhB77mWXbJl4WaJMWq9vJ16lWGIm9TnWZ3bFsmbZ3YD7QwIx3cd6clMbSKRgM="
}
```

### DB controller

The DB controller will enable when the `app.storage=db` property is configured. Below is an example with Vault transit encryption enabled.
The default database for the controller is the [H2 embedded database](http://www.h2database.com/html/quickstart.html) and requires no additional configuration.
The console for the database is located at `/h2-console`. You can login with `sa` & `password` credentials.

vault.sh:

```
vault secrets enable transform
vault write transform/role/payments transformations=card-number
vault write transform/transformation/card-number \
        type=fpe \
        template="builtin/creditcardnumber" \
        tweak_source=internal \
        allowed_roles=payments
```

application.properties:

```
app.storage=db
app.encryption.enabled=true
app.encryption.path=transform
app.encryption.key=payments
```

```
curl -s -X POST --header "Content-Type: application/json" --data '{"name": "Gerry", "type": "mastercard", "number": "1234-1234-1234-1234", "expiry": "01/23", "cvc": "123"}' localhost:8080  | jq
{
  "card_plaintext": "1234-1234-1234-1234",
  "card_ciphertext": "9044-9922-3580-9604",
  "message": "Payment processed successfully, card details returned for demo purposes, not for production",
  "id": "1"
}
```

#### Postgres Support

Postgres is supported as an external DB. Below is an override configuration.

application.yml

```
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    open-in-view: false
    generate-ddl: true
    properties:
      hibernate:
        temp:
          use_jdbc_metadata_defaults: false

```
