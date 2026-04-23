@echo off
setlocal enabledelayedexpansion

set "ROOT_DIR=%~dp0"
if "%ROOT_DIR:~-1%"=="\" set "ROOT_DIR=%ROOT_DIR:~0,-1%"

set "LOG_DIR=%ROOT_DIR%\logs"
set "DEPLOY_FILE=%ROOT_DIR%\sunusante-gateway-service\src\main\docker\deploy.yml"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo ==> Demarrage infrastructure Docker (Registry, Kafka, Zookeeper)...
docker compose -f "%DEPLOY_FILE%" up -d

echo ==> Demarrage sunusante-patient-service...
start "patient-service" cmd /c ""cd /d "%ROOT_DIR%\sunusante-patient-service" && mvnw.cmd -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run > "%LOG_DIR%\patient.log" 2>&1""

echo ==> Demarrage sunusante-dmp-service...
start "dmp-service" cmd /c ""cd /d "%ROOT_DIR%\sunusante-dmp-service" && mvnw.cmd -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run > "%LOG_DIR%\dmp.log" 2>&1""

echo ==> Demarrage sunusante-audit-service...
start "audit-service" cmd /c ""cd /d "%ROOT_DIR%\sunusante-audit-service" && mvnw.cmd -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run > "%LOG_DIR%\audit.log" 2>&1""

echo ==> Demarrage sunusante-gateway-service...
start "gateway-service" cmd /c ""cd /d "%ROOT_DIR%\sunusante-gateway-service" && mvnw.cmd -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run > "%LOG_DIR%\gateway.log" 2>&1""

echo.
echo Demarrage lance.
echo Logs: %LOG_DIR%
echo Gateway:  http://localhost:9040
echo Patient:  http://localhost:9041
echo DMP:      http://localhost:9042
echo Audit:    http://localhost:9043
echo Registry: http://localhost:8761


