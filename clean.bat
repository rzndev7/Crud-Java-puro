@echo off
setlocal
if exist out rmdir /s /q out
if exist data\customers.csv (
  rem Mantemos os dados por padrao; descomente abaixo para limpar tambem
  rem del /q data\customers.csv
)
echo Limpeza concluida.
