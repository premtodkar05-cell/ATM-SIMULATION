@echo off
title Push ATM Simulation to GitHub
echo =========================================================
echo Pushing ATM Simulation to GitHub:
echo https://github.com/premtodkar05-cell/ATM-SIMULATION.git
echo =========================================================

set PATH=%LOCALAPPDATA%\Programs\Git\cmd;%PATH%

cd /d "%~dp0"
git push -u origin main

echo.
if %ERRORLEVEL% EQU 0 (
    echo =========================================================
    echo [SUCCESS] Code pushed successfully to GitHub!
    echo Now open https://vercel.com/new and import the repo!
    echo =========================================================
) else (
    echo =========================================================
    echo [NOTE] If prompted above, sign in with your GitHub account.
    echo =========================================================
)

pause
