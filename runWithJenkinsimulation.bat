@echo off
cd %~dp0

REM Note: I was not able to make groovy load from multiple library dirs, when the libs are groovy and not jars.

echo prepare jenkinsfakelib folder by copying lib and jenkinssimulator into it
mkdir jenkinsfakelib
xcopy /E /Y lib\* jenkinsfakelib\
xcopy /E /Y jenkinssimulator\* jenkinsfakelib\

REM example: my/builds/masterPipelines
REM fakejenkins requires non-nested
set PARENT_FOLDER=exampleDir
set FAKE_JENKINS=true

echo start
groovy -cp jenkinsfakelib mini_status_generator.groovy

pause