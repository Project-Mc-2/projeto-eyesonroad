@echo off
setlocal
cd /d "%~dp0"
if not exist ".venv\Scripts\python.exe" (
  echo O ambiente ainda nao foi configurado. Execute configurar.bat primeiro.
  pause
  exit /b 1
)
".venv\Scripts\python.exe" main.py
if errorlevel 1 (
  echo.
  echo O programa terminou com erro. Leia a mensagem acima para identificar o problema.
  pause
)
