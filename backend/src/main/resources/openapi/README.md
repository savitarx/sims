# OpenAPI Specifications

This directory contains OpenAPI 3.0 specifications for the SIMS backend APIs.

## Files

- **exam-subjects-openapi.yaml** — Exam Subjects API (subject mappings to exams)
- **fees-openapi.yaml** — Fees Management API (fees + student fee payment status)

## Using These Specs

### With Swagger UI

To view these specs in an interactive API explorer, you have several options:

#### Option 1: Online Swagger Editor
1. Go to https://editor.swagger.io
2. Click **File** → **Import URL**
3. Paste the path to your spec file (e.g., raw GitHub URL or local file served via HTTP)

#### Option 2: Local Swagger UI (Spring Boot Integration)

Add to `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

Then configure in `application.properties`:

```properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.urls[0].name=Exam Subjects
springdoc.swagger-ui.urls[0].url=/v3/api-docs/exam-subjects
springdoc.swagger-ui.urls[1].name=Fees
springdoc.swagger-ui.urls[1].url=/v3/api-docs/fees
```

Access at: `http://localhost:8081/swagger-ui.html`

### With Postman

1. **New Collection** → **API**
2. Choose **OpenAPI** as the specification format
3. Paste the contents of the spec file or upload it
4. Postman auto-generates all requests

### With Other Tools

- **Insomnia**: File → Import → Raw text → Paste spec
- **VS Code**: Install **REST Client** extension, use `@requests.http` files
- **ReDoc**: https://redoc.ly (for beautiful documentation)

## Spec Coverage

### Exam Subjects API

**Base path**: `/api/v1/exam-subjects`

- `GET /` — List all exam subjects
- `POST /` — Create exam subject mapping
- `GET /{id}` — Get exam subject by ID
- `PUT /{id}` — Update exam subject (blocked if exam is published)
- `DELETE /{id}` — Delete exam subject (blocked if exam is published)
- `GET /exam/{examId}` — List subjects for an exam

**Data Model**:
- `examSubjectId` (UUID, PK)
- `examId` (UUID, FK)
- `subjectId` (UUID, FK)
- `classId` (UUID, FK)
- `maxMarks` (int, ≥1)
- `createdAt`, `updatedAt` (Instant)

**Business Rules**:
- Cannot add subjects to a published exam → `409 Conflict`
- Cannot modify subjects of a published exam → `409 Conflict`
- Cannot delete subjects from a published exam → `409 Conflict`
- Duplicate mapping (same exam/subject/class) → `409 Conflict`

### Fees API

**Base path**: `/api/v1/fees`

- `GET /` — List all fee structures
- `POST /` — Create fee structure
- `GET /{id}` — Get fee by ID
- `PUT /{id}` — Update fee structure
- `DELETE /{id}` — Delete fee structure

**Data Model**:
- `feeId` (UUID, PK)
- `classId` (UUID, FK)
- `academicYearId` (UUID, FK)
- `termName` (string)
- `createdAt`, `updatedAt` (Instant)

### Student Fee Status API

**Base path**: `/api/v1/student-fee-status`

- `GET /` — List all student fee statuses
- `POST /` — Create fee status record
- `GET /{id}` — Get fee status by ID
- `PUT /{id}` — Update fee status (e.g., mark as PAID)
- `DELETE /{id}` — Delete fee status
- `GET /enrollment/{enrollmentId}` — Get all fees for a student

**Data Model**:
- `studentFeeStatusId` (UUID, PK)
- `enrollmentId` (UUID, FK)
- `feeId` (UUID, FK)
- `status` (enum: PAID | PARTIAL | NOT_PAID)
- `updatedById` (UUID, FK to Teacher)
- `createdAt`, `updatedAt` (Instant)

**Unique Constraint**:
- One row per (enrollmentId, feeId) pair → `409 Conflict` on duplicate

## Authentication

All endpoints require **Bearer JWT token** in the Authorization header:

```
Authorization: Bearer <jwt-token>
```

Obtain token from `/api/v1/auth/login` (not yet implemented).

## Error Responses

All errors return a standard format:

```json
{
  "timestamp": "2026-07-28T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/exam-subjects",
  "fieldErrors": {
    "maxMarks": "must be at least 1"
  }
}
```

### Status Codes

- **200 OK** — Successful GET, POST, PUT
- **204 No Content** — Successful DELETE
- **400 Bad Request** — Validation failed, see fieldErrors
- **401 Unauthorized** — Missing or invalid JWT token
- **404 Not Found** — Resource not found
- **409 Conflict** — Business rule violation (duplicate, state mismatch)
- **500 Internal Server Error** — Unexpected error

## Implementation Notes

### For Backend Developers

The OpenAPI specs are **documentation only** at this stage. They describe the intended API contract.

To auto-generate from code annotations, follow the Swagger UI section above.

### For Frontend/API Consumers

These specs represent the **contract you should build against**. The actual API will follow these contracts closely.

**Note**: `/api/v1/exams` is currently open in SecurityConfig for testing. `ExamSubjects` and Fees require JWT auth but may not have an authentication endpoint yet.

## Future Enhancements

- [ ] Add Spring Doc annotations (@Operation, @Schema) to controllers for auto-generation
- [ ] Include example request/response payloads in each endpoint
- [ ] Add rate-limiting headers and response examples
- [ ] Document batch operations (if planned)
- [ ] Add @deprecated markers for any old endpoints
