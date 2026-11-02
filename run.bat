@echo off
title Classroom ATM Simulation
echo ===================================================
echo Starting Classroom ATM Simulation...
echo ===================================================

set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.21.21-hotspot
set PATH=%JAVA_HOME%\bin;%LOCALAPPDATA%\Programs\Maven\apache-maven-3.9.9\bin;%PATH%

cd /d "%~dp0"
call mvn exec:java

pause
