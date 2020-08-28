# Payments

Dummy payments gateway written with Java SpringBoot

Docker Hub Image: [https://hub.docker.com/repository/docker/hashicorpdemoapp/payments](https://hub.docker.com/repository/docker/hashicorpdemoapp/payments)

## Usage
Currently this API has a single endpoint


### POST /

#### Request

```json
{
  "name": "Gerry",
  "type": "mastercard",
  "number": "1234-1234-1234-1234",
  "expiry": "01/23",
  "cvc": "123"
}
```

#### Response

```
{
  "id": "12332-dfdf-123123-sd1-213gxb-3-4522",
  "message":"Sorry insufficient funds"
}

```