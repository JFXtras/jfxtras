rem do all except javadoc (see above) and deploy
call java_localOverride.cmd
call mvnw clean
call mvnw clean deploy -DskipTests -Dmaven.javadoc.skip=true
if ERRORLEVEL 1 GOTO errorOccurred

rem asciidoclet was never upgraded to Java 9+, so we need Java 8 to generate javadoc
set JAVA_VERSION=8
call java_localOverride.cmd
call mvnw javadoc:jar deploy:deploy

:errorOccurred
pause