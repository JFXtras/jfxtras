@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew clean shadow -x test --stacktrace --info
pause