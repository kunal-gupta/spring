@echo off
REM Start the bi_dashboard Spring Boot application from the built JAR.
setlocal
set JAR_FILE=target\bi_dashboard.jar
if not exist "%JAR_FILE%" (
    echo ERROR: %JAR_FILE% not found. Run mvn package first.
    exit /b 1
)
java -jar "%JAR_FILE%"
endlocal
