# subscription

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Auth module roadmap

Domain: `SystemUser` (credentials, created first) → `User` (profile, always linked to a
`SystemUser`) → `RefreshToken` (1:N, one `SystemUser` can have several active sessions).

### Done

- Dependencies: Flyway, Elytron security (BCrypt), SmallRye JWT (build + verify),
  Scheduler.
- Migrations: `system_users`, `users`, `refresh_tokens` (Flyway, in that order).
- Entities + Panache repositories: `SystemUser`, `User` (`@OneToOne` to `SystemUser`),
  `RefreshToken` (`@ManyToOne` to `SystemUser`, self-referencing `replacedBy`).
- `SignupRequest`/`SignupResponse` DTOs (records).
- `POST /auth/signup` wired end-to-end (`AuthResource` → `AuthService`), creates
  `SystemUser` + `User` in one transaction, password hashed with BCrypt.

### In progress / next

- Fix: `user.lastName` not being set in `AuthService.signup` yet (will violate the
  `NOT NULL` constraint).
- Decide/finish `SignupResponse` payload.
- Login (`POST /auth/login`): validate password, lockout after failed attempts, issue
  JWT + first `refresh_tokens` row.
- Extract `issueTokens(SystemUser)` in `AuthService` (shared by login and signup) —
  signup should auto-login and return tokens too, no separate login call required
  right after creating the account.
- Refresh (`POST /auth/refresh`): rotation with reuse detection (family revocation on
  reuse of an already-rotated token).
- Logout (`POST /auth/logout`): revoke current or all refresh tokens for the account.
- Login rate limiting by IP (in-memory sliding window).
- `RefreshTokenCleanupJob` (`@Scheduled`) to purge expired/revoked tokens.
- Tests (`@QuarkusTest` + rest-assured), especially the refresh reuse-detection case.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/subscription-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly JAX-RS)
- Apache Kafka Client ([guide](https://quarkus.io/guides/kafka)): Connect to Apache Kafka with its native API
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplified JPA/Hibernate data access layer with active record and repository patterns
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)


[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
