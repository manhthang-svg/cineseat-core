# Ticketing Backend Base

Production-oriented Spring Boot 4 / Java 21 base with JWT access tokens, rotating opaque refresh tokens, RBAC, Flyway, MySQL, Actuator and Docker.

## Security model

- Access tokens are short-lived signed JWTs with issuer, audience, expiry and JTI.
- Refresh tokens are 256-bit opaque values in an HttpOnly cookie. Only their SHA-256 digest is stored.
- Every login creates an independent token family, so multiple devices can remain signed in.
- Refresh is serialized with a database pessimistic lock. Reuse of an already rotated token revokes the whole family.
- Disabled, locked, expired and soft-deleted accounts cannot authenticate with an old access token.
- Refresh and logout require CSRF protection because they use a cookie.

## Run locally with Docker

1. Copy `.env.example` to `.env` and replace every placeholder. Generate the JWT key with `openssl rand -base64 32`.
2. Run `docker compose up --build`.
3. Health endpoint: `GET http://localhost:8080/actuator/health`.

For IDE development, copy `src/main/resources/application-local.example.yml` to `application-local.yml`, set `JWT_SECRET_KEY`, and start MySQL.

## Authentication flow

1. `POST /api/auth/register` creates a user and returns HTTP 201.
2. `POST /api/auth/login` returns an access token and sets the HttpOnly refresh cookie.
3. Call `GET /api/auth/csrf`; send the returned token in the `X-XSRF-TOKEN` header for refresh/logout.
4. `POST /api/auth/refresh-token` atomically rotates the refresh token.
5. `POST /api/auth/logout` requires Bearer access token and CSRF token, revokes the refresh token and clears its cookie.

Browser clients must use credentials mode (`credentials: "include"`) for cookie requests. Never store refresh tokens in local storage.

## Configuration

Production uses the `prod` profile and requires:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET_KEY` (Base64, at least 32 decoded bytes)
- `CORS_ALLOWED_ORIGINS` (comma-separated exact origins; wildcards are intentionally unsupported with credentials)

Optional settings include access/refresh expiry, database pool sizes, issuer and audience; see `application.yml` and `application-prod.yml`.

## Verification

Run `mvn clean verify` locally before committing changes. Database integration tests should use MySQL Testcontainers as domain modules are added.

## Operational endpoints

- `/actuator/health/liveness`
- `/actuator/health/readiness`
- `/actuator/prometheus` locally. Production exposes only health/info by default; expose metrics only on a private management network.

Swagger is enabled locally and disabled in production.
