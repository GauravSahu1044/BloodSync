@echo off
echo Starting BloodSync Application...
echo.
echo Current Java version:
java -version
echo.
echo Using Maven wrapper to run the application...
echo.

REM Set JAVA_HOME to current Java installation
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot

REM Run the application using Maven wrapper
call mvnw.cmd spring-boot:run

pause 