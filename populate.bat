@echo off
setlocal

set _CLASSPATH=./build/classes/
set _CLASSPATH=%JBOSS_DIST%/server/default/lib/mysql-connector-java-3.0.6-stable-bin.jar;%_CLASSPATH%

%JAVA_HOME%\bin\java -classpath "%_CLASSPATH%" edu.nyu.pdsg.tpcw.populate.Populate
