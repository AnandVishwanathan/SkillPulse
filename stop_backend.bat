@echo off
echo Stopping SkillPulse Backend...
echo.

echo Finding processes using port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Killing process %%a
    taskkill /f /pid %%a
)

echo Backend stopped.
pause