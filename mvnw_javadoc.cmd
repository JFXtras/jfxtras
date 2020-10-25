call java_localOverride.cmd
call mvnw clean
call mvnw clean javadoc:javadoc -P asciidoc
pause