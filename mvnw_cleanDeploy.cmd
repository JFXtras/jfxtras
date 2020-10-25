call java_localOverride.cmd
call mvnw clean
call mvnw deploy -DskipTests -P sources-as-javadoc-jar

pause