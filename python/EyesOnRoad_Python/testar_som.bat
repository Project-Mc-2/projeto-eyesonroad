@echo off
setlocal
cd /d "%~dp0"
if exist ".venv\Scripts\python.exe" (
  ".venv\Scripts\python.exe" testar_som.py
) else (
  py testar_som.py
)
echo.
pause
