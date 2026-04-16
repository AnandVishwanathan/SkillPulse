@echo off
echo Starting SkillPulse Backend...
echo.

cd backend\backend

echo Checking if port 8080 is available...
netstat -an | findstr :8080 > nul
if %errorlevel% == 0 (
    echo Port 8080 is already in use. Backend might already be running.
    echo If you want to restart, please stop the existing process first.
    pause
    exit /b 1
)

echo Starting Spring Boot application...
echo.
echo Backend will be available at: http://localhost:8080
echo API endpoints will be at: http://localhost:8080/api
echo.

call mvnw.cmd spring-boot:run

pause