# Spring Boot application

This is a Spring Boot application built from the [schemas for the federated model](https://github.com/co-cddo/federated-api-model/tree/main/schemas) at the top level of this repository.

The [OpenAPI documentation](https://github.com/co-cddo/federated-api-model/blob/spring-boot-readme/schemas/v1alpha/openapi.yml) is also located in the `schemas` directory.

The app is deployed in a [sandbox environment on PaaS](https://federated-api-model-spring-boot-sandbox.london.cloudapps.digital). For example you can use the [/apis endpoint](https://federated-api-model-spring-boot-sandbox.london.cloudapps.digital/apis) to see metadata about other APIs.

## Building and running locally

To build the application locally run:

```sh
./gradlew clean build
```

To run the application locally run:

```sh
./gradlew bootRun
```

This will make the application available on port 8080.
