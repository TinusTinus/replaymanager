@echo off

:: Startup script for the Ultimate Marvel vs Capcom 3 Replay Manager.
:: This script assumes that a valid javaw.exe, version 1.7 or later, is on the PATH.

set PWD="%CD%"

set LIB_DIR=%PWD%/../lib

set JAVA_OPTS=

set JAVA=javaw

start %JAVA% %JAVA_OPTS% -cp ;%LIB_DIR%/* nl.tinus.umvc3replayanalyser.gui.Umvc3ReplayManager