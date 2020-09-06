call ..\java_localOverride.cmd
call ..\mvnw test -Dtest=!AllTests*
pause