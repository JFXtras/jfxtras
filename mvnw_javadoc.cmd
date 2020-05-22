rem asciidoclet was never upgraded to Java 9+, so we need Java 8 to generate javadoc
set JAVA_VERSION=8
call java_localOverride.cmd
call mvnw clean javadoc:javadoc
set JAVA_VERSION = ""
pause