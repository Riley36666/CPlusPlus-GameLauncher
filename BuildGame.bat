@echo off
title Building Java Game
echo ==========================================
echo        🚀 Building JavaGame
echo ==========================================
REM --- Delete old build if it exists ---
set "oldgame=..\My Game (WIP name).exe"
if exist "%oldgame%" (
    echo Deleting old build...
    del "%oldgame%"
)
REM --- Step 1: compile Java files ---
echo [1/5] Compiling Java source files...
cd Java
javac -d ../classes *.java
if errorlevel 1 (
    echo ❌ Java compilation failed!
    pause
    exit /b
)
cd ..

REM --- Step 2: build the JAR file ---
echo [2/5] Creating Game.jar...
jar cfm finishedjar/Game.jar Java/MANIFEST.MF -C classes . -C images .
if errorlevel 1 (
    echo ❌ Failed to create Game.jar!
    pause
    exit /b
)

REM --- Step 3: copy jar into launcher folder ---
echo [3/5] Copying JAR into Launcher...
copy /Y finishedjar\Game.jar Launcher\Game.jar >nul

REM --- Step 4: compile resources and launcher ---
echo [4/5] Compiling launcher executable...
cd Launcher
windres resources.rc -o resources.o
g++ launch.cpp resources.o -o "../My Game (WIP name)" -mwindows -ldwmapi -lmsimg32 -lgdi32 -lole32 -lcomctl32 -lstdc++fs
if errorlevel 1 (
    echo ❌ Launcher build failed!
    pause
    exit /b
)
cd ..

echo [5/5] ✅ Build complete!
echo ------------------------------------------
echo GameLauncher.exe is ready to run!
echo ------------------------------------------
pause
