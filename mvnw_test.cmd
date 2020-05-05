call java_localOverride.cmd
rem call mvnw test
rem TBEERNOT ResponsivePane tests break the others when run by maven
call mvnw test -DfailIfNoTests=false -Dtest=ResponsivePane*Test
call mvnw test -DfailIfNoTests=false -Dtest=!ResponsivePane*Test
pause