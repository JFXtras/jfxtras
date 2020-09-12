call java_localOverride.cmd
call mvnw clean
call mvnw clean package -DskipTests -Dmaven.javadoc.skip=true
pause