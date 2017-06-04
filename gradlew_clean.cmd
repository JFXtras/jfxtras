@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew clean --info
rem call gradlew publishToMavenLocal -x test --info
pause