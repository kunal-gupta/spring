# Spring Boot Visitor App

Backend API to log office visitors and track meeting history.

## Current Status

- Base package renamed to `com.visitorapp` (removed `example`).
- Uses Spring Data JPA repositories with derived query methods.
- Includes core APIs and additional reference APIs for learning query derivation.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- Lombok
- HSQLDB (file-based)

## Project Structure

- Main app: `src/main/java/com/visitorapp/VisitorAppApplication.java`
- Controller: `src/main/java/com/visitorapp/controller/VisitorController.java`
- Service: `src/main/java/com/visitorapp/service/VisitorService.java`
- Repositories:
  - `src/main/java/com/visitorapp/repository/VisitorRepository.java`
  - `src/main/java/com/visitorapp/repository/VisitRepository.java`
- Entities:
  - `src/main/java/com/visitorapp/model/Visitor.java`
  - `src/main/java/com/visitorapp/model/Visit.java`

## Run

```bash
mvn spring-boot:run
```

App URL: `http://localhost:8080`

## Database

- HSQLDB URL: `jdbc:hsqldb:file:./data/visitor-db`
- DB files are created under `./data`
- Schema update mode: `spring.jpa.hibernate.ddl-auto=update`

## Core APIs

1. Log visit  
`POST /api/visitors/visits`

Sample body:
```json
{
  "name": "Ravi Kumar",
  "address": "New Delhi",
  "phone": "9876543210",
  "email": "ravi@example.com",
  "designation": "Vendor",
  "purpose": "Product demo",
  "notes": "Requested follow-up next week"
}
```

2. Search visitor  
`GET /api/visitors/search?email=ravi@example.com`  
`GET /api/visitors/search?phone=9876543210`

3. Full visit history  
`GET /api/visitors/{visitorId}/history`

## Derived Query Reference APIs

These are added as practical examples for Spring Data derived method names.

- `GET /api/visitors/reference/by-phone?phone=9876543210`
- `GET /api/visitors/reference/by-email-and-phone?email=ravi@example.com&phone=9876543210`
- `GET /api/visitors/reference/exists-by-email?email=ravi@example.com`
- `DELETE /api/visitors/reference/by-phone?phone=9876543210`
- `GET /api/visitors/reference/by-ids?ids=1,2,3`
- `GET /api/visitors/reference/search-by-name?text=ravi`
- `GET /api/visitors/reference/without-email`
- `GET /api/visitors/reference/top5-by-name`
- `GET /api/visitors/reference/top10-latest`
- `GET /api/visitors/reference/{visitorId}/first-visit`
- `GET /api/visitors/reference/visits-between?from=2026-01-01T00:00:00&to=2026-12-31T23:59:59`

## Validation and Error Behavior

- At least one identifier (`email` or `phone`) is required while logging a visit.
- If provided email and phone map to different visitors, API returns `400`.
- Unknown visitor in search/history APIs returns `404`.
- `DELETE /reference/by-phone` returns `400` if visitor has existing visit history.
- `visits-between` expects ISO date-time format for `from` and `to`.
