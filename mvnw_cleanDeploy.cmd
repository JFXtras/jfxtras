rem asciidoclet was never upgraded to Java 9+, so we need Java 8 to generate javadoc
rem set JAVA_VERSION=8
rem call java_localOverride.cmd
rem call mvnw clean javadoc:javadoc deploy:deploy
rem if ERRORLEVEL 1 GOTO errorOccurred
rem set JAVA_VERSION = ""

rem do all except javadoc (must be run by Java 8) and deploy
call java_localOverride.cmd
call mvnw clean deploy -DskipTests -Dmaven.javadoc.skip=true

:errorOccurred
pause