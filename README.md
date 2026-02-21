# Spring Boot Visitor App

Backend API to log office visitors and track meeting history.

## Features

- Log a visit with visitor details.
- Reuse existing visitor by email or phone.
- Search visitor by email/phone and get last visit + total visits.
- Fetch complete meeting history for a visitor.
- Persist data in HSQLDB file database.

## Stack

- Java 17
- Spring Boot 3
- Spring Web + Spring Data JPA + Validation
- HSQLDB

## Run

```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080`.

## APIs

### 1) Log a new visit

`POST /api/visitors/visits`

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

### 2) Search visitor by email or phone

`GET /api/visitors/search?email=ravi@example.com`

`GET /api/visitors/search?phone=9876543210`

### 3) Get full meeting history by visitor id

`GET /api/visitors/{visitorId}/history`

## Notes

- Provide at least one identifier (`email` or `phone`) while logging a visit.
- If both email and phone are provided and map to different visitors, API returns `400`.
- DB file is created under `./data/visitor-db`.
