# SIMS API Documentation

## The spec is now generated from the code

`springdoc-openapi` (3.x, the Spring Boot 4 line) is on the classpath, so the
OpenAPI document is produced from the actual controllers, DTOs and Bean
Validation annotations. It cannot drift from the implementation the way a
hand-maintained yaml does.

With the app running on port 8081:

| What | Where |
|---|---|
| Swagger UI | http://localhost:8081/swagger-ui.html |
| OpenAPI JSON | http://localhost:8081/v3/api-docs |
| OpenAPI YAML | http://localhost:8081/v3/api-docs.yaml |

To save a snapshot for a client or for source control:

```bash
curl http://localhost:8081/v3/api-docs.yaml -o sims-openapi.yaml
```

Both paths are permitted in `SecurityConfig` while authentication is still being
built, so no token is needed to read them yet.

## SIMS-OpenAPI-Spec.yaml

That file is a Bruno collection export and predates the exam/fee/communication
redesign — it still documents the old flat-UUID responses and carries no schemas.
Treat the generated document above as the source of truth; keep the Bruno file
only if it is still in use as a request collection.

## Conventions the generated spec reflects

**Pagination.** List endpoints return a Spring `Page`:

```
GET /api/v1/exams?page=0&size=20&sort=createdAt,desc
```

The response carries `content`, `totalElements`, `totalPages`, `number`, `size`.

**Nested business objects.** Responses embed what a screen needs rather than bare
foreign keys — `subject`, `student`, `schoolClass`, `createdBy`, `updatedBy` are
objects with ids *and* names. The old flat `*Id` fields are still present but
marked `@Deprecated`; they come out once the React app has migrated.

**Errors.** Every business rule maps to a real status code via
`GlobalExceptionHandler`:

| Status | Meaning | Exception |
|---|---|---|
| 400 | cross-field request problem | `InvalidRequestException` |
| 400 | bean validation failure (with `fieldErrors`) | `MethodArgumentNotValidException` |
| 404 | unknown id | `ResourceNotFoundException` |
| 409 | uniqueness violation | `DuplicateResourceException` |
| 409 | state forbids the operation | `BusinessRuleViolationException` |

**Actor fields.** Requests currently carry `actorId` (admin) or `enteredById` /
`updatedById` (teacher) because authentication is still in progress. Each is
marked with a TODO in its DTO and will be replaced by the JWT principal.
