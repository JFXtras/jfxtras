call java_localOverride.cmd
call mvnw clean release:prepare release:perform -DskipTests
pause