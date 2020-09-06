call java_localOverride.cmd
set JAVA_OPTS="--add-exports javafx.graphics/com.sun.glass.ui=jfxtras.controls"
rem call mvnw test
rem TBEERNOT ResponsivePane tests break the others when run by maven
rem call mvnw test -DfailIfNoTests=false -Dtest=ResponsivePane*Test
rem call mvnw clean test -DfailIfNoTests=false -Dtest=!ResponsivePane*Test
call mvnw clean test -DfailIfNoTests=false
pause