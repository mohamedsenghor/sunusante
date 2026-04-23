@echo off
setlocal enabledelayedexpansion

set "ROOT_DIR=%~dp0"
if "%ROOT_DIR:~-1%"=="\" set "ROOT_DIR=%ROOT_DIR:~0,-1%"
set "DEPLOY_FILE=%ROOT_DIR%\sunusante-gateway-service\src\main\docker\deploy.yml"

echo ==> Stopping local Java/Node services...
call :killPort 9040
call :killPort 9041
call :killPort 9042
call :killPort 9043
call :killPort 9060

echo ==> Stopping Docker infrastructure...
if exist "%DEPLOY_FILE%" (
  docker compose -f "%DEPLOY_FILE%" down
  if errorlevel 1 docker-compose -f "%DEPLOY_FILE%" down
) else (
  echo deploy.yml not found: %DEPLOY_FILE%
)

echo Done.
endlocal
exit /b 0

:killPort
set "PORT=%~1"
set "FOUND=0"
for /f "tokens=5" %%P in ('netstat -ano ^| findstr /r /c:":%PORT% .*LISTENING"') do (
  set "FOUND=1"
  echo Killing PID %%P on port %PORT%
  taskkill /PID %%P /T /F >nul 2>&1
)
if "%FOUND%"=="0" echo No process found on port %PORT%
exit /b 0

