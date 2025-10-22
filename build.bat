@echo off
setlocal enabledelayedexpansion

set SRC=src
set OUT=out\classes

if exist out rmdir /s /q out
mkdir out 2>nul
mkdir %OUT% 2>nul

javac -encoding UTF-8 -d %OUT% -cp %SRC% %SRC%\com\example\crud\*.java
if errorlevel 1 goto :err

rem Tenta criar JAR se a ferramenta jar existir
where jar >nul 2>nul
if %errorlevel%==0 (
	jar cfe out\app.jar com.example.crud.Main -C %OUT% .
	if errorlevel 1 goto :err
	echo.
	echo Build concluido: out\app.jar
) else (
	echo.
	echo "jar" nao encontrado. Classes compiladas em out\classes. Voce ainda pode executar com run.bat.
)
exit /b 0

:err
echo.
echo Erro na compilacao/empacotamento. Verifique as mensagens acima.
exit /b 1
