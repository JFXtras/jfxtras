call java_localOverride.cmd
call mvnw clean
call mvnw clean package -Dmaven.test.skip -Dmaven.javadoc.skip=true
pause