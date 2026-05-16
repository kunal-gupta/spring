#!/usr/bin/env bash
# Start the bi_dashboard Spring Boot application from the built JAR.
JAR_FILE="target/bi_dashboard.jar"
if [ ! -f "$JAR_FILE" ]; then
  echo "ERROR: $JAR_FILE not found. Run mvn package first."
  exit 1
fi
java -jar "$JAR_FILE"
