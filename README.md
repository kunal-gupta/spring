# Visitor Logging App

## Use Case
This application is for offices/firms that want to log visitor entries digitally and avoid manual register-based record keeping.

It helps front-desk/admin teams:
- Register and login securely
- Log each visitor visit with purpose/notes
- Search visitors by phone/email
- View complete visit history for repeat visitors

## Why This App
- Removes dependency on paper registers
- Makes visitor history searchable in seconds
- Reduces duplicate/incorrect manual entries
- Keeps records persistent across app restarts

## Development Note
This codebase was developed entirely by Codex 5.3 in collaboration with Visual Studio Code.

## Tech Stack
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Web
- Spring Data JPA
- Bean Validation
- HSQLDB (file-based)
- React (served by Spring Boot)

## Main URLs
- App UI: `http://localhost:8080`
- Register: `http://localhost:8080/register`
- Login: `http://localhost:8080/login`
- Visit lookup: `http://localhost:8080/visit`

## How It Works (User Flow)
1. User registers from UI (`/register`) or logs in (`/login`).
2. JWT token is issued after successful auth.
3. User enters mobile/email in visit lookup screen.
4. App opens workspace with:
- Left: new visit log form
- Right: visitor summary + visit history
5. Visit is stored in DB and can be searched later.

## Run Locally (Single Command)
From project root:

```bash
mvn spring-boot:run
```

Open:

`http://localhost:8080`

## Database Location
Configured as:

`jdbc:hsqldb:file:./data/visitor-db`

So DB files are created in:
- `data/visitor-db.script`
- `data/visitor-db.properties`
- `data/visitor-db.log`

## API Highlights
- `POST /auth/register`
- `POST /auth/login`
- `POST /api/visitors/visits`
- `GET /api/visitors/search?email=...` or `?phone=...`
- `GET /api/visitors/{visitorId}/history`

## Distributable Build
Create shareable artifacts:

```bash
mvn clean package -DskipTests
```

Outputs:
- Runnable jar: `target/spring-boot-visitor-app-0.0.1-SNAPSHOT.jar`
- Distribution zip: `target/spring-boot-visitor-app-0.0.1-SNAPSHOT-distribution.zip`

ZIP includes:
- application jar
- `startup.bat`
- `README.txt`
- empty `data/` folder

## Run Packaged App
After unzip:
1. Ensure Java 21+ is installed
2. Run `startup.bat`
3. Open `http://localhost:8080`
