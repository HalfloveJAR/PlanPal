@echo off
REM Load environment variables from .env
for /f "delims=" %%i in (.env) do set %%i

REM Define the classpath with the JSON library
set CLASSPATH=libs\json-20230618.jar

REM Compile the Java source files and include the classpath
javac -cp %CLASSPATH% -d out src\*.java

REM Run the main class with the classpath including both compiled classes and the JSON jar
java -cp out;%CLASSPATH% LoginPage
