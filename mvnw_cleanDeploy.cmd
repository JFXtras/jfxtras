call java_localOverride.cmd
call mvnw clean
call mvnw clean deploy -DskipTests -Dmaven.javadoc.skip=true

pause