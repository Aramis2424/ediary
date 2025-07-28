@echo off
REM === Настройки ===
set RESULTS_FILE=.\results\results.jtl
set REPORT_DIR=.\results\report

REM === Очистка предыдущего отчёта (если есть) ===
if not exist .\results mkdir .\results
if exist %REPORT_DIR% rmdir /s /q %REPORT_DIR%

REM === Генерация HTML-отчёта ===
echo Generate html-report...
jmeter -g %RESULTS_FILE% -o %REPORT_DIR%

echo Done!
pause
