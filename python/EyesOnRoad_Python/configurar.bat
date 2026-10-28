@echo off
setlocal
cd /d "%~dp0"

echo =============================================
echo EyesOnRoad - Configuracao do ambiente Python
echo =============================================

where py >nul 2>nul
if errorlevel 1 (
  echo ERRO: o comando "py" nao foi encontrado.
  echo Instale o Python e marque a opcao para adicionar ao PATH.
  echo Depois feche e abra novamente este arquivo.
  pause
  exit /b 1
)

if not exist ".venv\Scripts\python.exe" (
  echo Criando ambiente virtual...
  py -m venv .venv
  if errorlevel 1 (
    echo ERRO ao criar o ambiente virtual.
    pause
    exit /b 1
  )
)

echo Atualizando pip...
".venv\Scripts\python.exe" -m pip install --upgrade pip
if errorlevel 1 (
  echo ERRO ao atualizar pip.
  pause
  exit /b 1
)

echo Instalando dependencias...
".venv\Scripts\python.exe" -m pip install -r requirements.txt
if errorlevel 1 (
  echo ERRO na instalacao. Confira a internet e a versao do Python.
  pause
  exit /b 1
)

echo.
echo Ambiente configurado. Agora execute iniciar.bat.
pause
