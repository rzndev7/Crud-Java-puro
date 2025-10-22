@echo off
setlocal

if not exist out\app.jar (
  echo JAR nao encontrado. Tentando compilar e executar via classes...
  call build.bat
  if errorlevel 1 exit /b 1
)

if exist out\app.jar (
  java -jar out\app.jar
) else (
  if not exist out\classes (
    echo Nao ha classes compiladas em out\classes.
    exit /b 1
  )
  java -cp out\classes com.example.crud.Main
)
