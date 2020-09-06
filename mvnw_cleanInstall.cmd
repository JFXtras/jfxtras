call java_localOverride.cmd
call mvnw clean
call mvnw clean install -DskipTests -Dmaven.javadoc.skip=true
pause